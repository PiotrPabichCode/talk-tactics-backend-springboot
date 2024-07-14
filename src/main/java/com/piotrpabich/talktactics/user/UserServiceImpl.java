package com.piotrpabich.talktactics.user;

import com.piotrpabich.talktactics.auth.AuthConstants;
import com.piotrpabich.talktactics.common.PageResult;
import com.piotrpabich.talktactics.user.dto.*;
import com.piotrpabich.talktactics.user.dto.req.DeleteFriendDto;
import com.piotrpabich.talktactics.user.dto.req.FriendInvitationRequest;
import com.piotrpabich.talktactics.user.dto.req.UpdatePasswordReqDto;
import com.piotrpabich.talktactics.user.dto.res.FriendInvitationDto;
import com.piotrpabich.talktactics.user_course.dto.UserCourseDto;
import com.piotrpabich.talktactics.exception.BadRequestException;
import com.piotrpabich.talktactics.exception.EntityExistsException;
import com.piotrpabich.talktactics.exception.EntityNotFoundException;
import com.piotrpabich.talktactics.user.entity.FriendInvitation;
import com.piotrpabich.talktactics.user.entity.User;
import com.piotrpabich.talktactics.auth.AuthUtil;
import com.piotrpabich.talktactics.common.util.PageUtil;
import com.piotrpabich.talktactics.common.QueryHelp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;

import static com.piotrpabich.talktactics.common.util.Utils.getJsonPropertyFieldMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final FriendInvitationRepository friendInvitationRepository;
    private final PasswordEncoder passwordEncoder;


//  PUBLIC
    @Override
    public PageResult<UserDto> queryAll(
            UserQueryCriteria criteria,
            Pageable pageable
    ) {
        Page<User> page = userRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(UserDto::from));
    }
    @Override
    public User getUserById(long id, User requester) {
        User user = getUserById(id);
        AuthUtil.validateIfUserHimselfOrAdmin(requester, user);
        return user;
    }

    @Override
    public User getUserById(long id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(User.class, "id", String.valueOf(id)));
    }
    @Override
    public User getUserByUsername(String username, User requester) {
        return userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException(User.class, "username", username));
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(
            long id,
            User requester
    ) {
        AuthUtil.validateIfUserAdmin(requester);
        User user = getUserById(id);
        if(AuthConstants.ADMIN.equals(user.getRole().toString())) {
            throw new BadCredentialsException("Cannot delete admin user");
        }
        userRepository.deleteById(id);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public User updateUser(long id, Map<String, Object> newValues, User requester) {
        User user = getUserById(id);
        AuthUtil.validateIfUserHimselfOrAdmin(requester, user);
        validateUpdateUserRequest(user, newValues);

        return userRepository.save(user);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public User updatePassword(
            UpdatePasswordReqDto updatePasswordRequest,
            User requester
    ) {
        User user = getUserById(updatePasswordRequest.id());
        AuthUtil.validateIfUserHimselfOrAdmin(requester, user);
        validatePassword(user, updatePasswordRequest);

        user.setPassword(passwordEncoder.encode(updatePasswordRequest.newPassword()));
        return userRepository.save(user);
    }

    @Override
    public List<UserProfilePreviewDto> getUserProfiles() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .sorted(Comparator.comparingInt(User::getTotalPoints).reversed())
                .map(User::toUserProfilePreviewDto)
                .toList();
    }

    @Override
    public UserProfileDto getUserProfileById(Long id) {
        User user = getUserById(id);
        UserProfilePreviewDto userProfile = user.toUserProfilePreviewDto();
        final var userCourses = user.getUserCourses().stream().map(UserCourseDto::from).toList();

        return UserProfileDto.toUserProfileDto(userProfile, userCourses);
    }

    @Override
    public List<UserProfilePreviewDto> getFriends(Long id, User requester) {
        User user = getUserById(id);
        AuthUtil.validateIfUserHimselfOrAdmin(requester, user);

        return user.getFriends().stream()
                .sorted(Comparator.comparingInt(User::getTotalPoints).reversed())
                .map(User::toUserProfilePreviewDto)
                .toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleFriendInvitationRequest(FriendInvitationRequest request, User requester) {
        switch (request.action()) {
            case SEND -> sendFriendInvitation(request, requester);
            case ACCEPT -> acceptFriendInvitation(request, requester);
            case REJECT -> rejectFriendInvitation(request, requester);
            case DELETE -> deleteSentFriendInvitation(request, requester);
            default -> throw new BadRequestException("Invalid action: " + request.action());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFriend(
            DeleteFriendDto request,
            User requester
    ) {
        User user = getUserById(request.userId());
        AuthUtil.validateIfUserHimselfOrAdmin(requester, user);
        User friend = getUserById(request.friendId());

        if(!user.getFriends().contains(friend)) {
            throw new BadRequestException(UserConstants.NOT_FRIENDS_EXCEPTION);
        }

        user.getFriends().remove(friend);
        friend.getFriends().remove(user);

        userRepository.saveAll(List.of(user, friend));
    }

    @Override
    public List<FriendInvitationDto> getReceivedFriendInvitations(
            Long id,
            Boolean withDetails,
            User requester
    ) {
        User user = getUserById(id);
        AuthUtil.validateIfUserHimselfOrAdmin(requester, user);

        return user.getReceivedFriendInvitations().stream()
                .map(friendInvitation -> friendInvitation.toFriendInvitationDto(withDetails))
                .toList();
    }

    @Override
    public List<FriendInvitationDto> getSentFriendInvitations(
            Long id,
            Boolean withDetails,
            User requester
    ) {
        User user = getUserById(id);
        AuthUtil.validateIfUserHimselfOrAdmin(requester, user);

        return user.getSentFriendInvitations().stream()
                .map(friendInvitation -> friendInvitation.toFriendInvitationDto(withDetails))
                .toList();
    }

    //  PRIVATE
    private void validateUpdateUserRequest(User user, Map<String, Object> newValues) {
        if(newValues.containsKey("email")) {
            String email = (String) newValues.get("email");
//            if(!isValidEmail(email)) {
//                throw new BadCredentialsException(AuthConstants.EMAIL_FORBIDDEN_VALUES_EXCEPTION);
//            }
        }

        Map<String, String> fieldMap = getJsonPropertyFieldMap(UpdateUserDto.class);

        newValues.forEach((key, value) -> {
            String fieldName = fieldMap.get(key);
            if (fieldName == null) {
                throw new BadRequestException("Illegal new values given: " + key);
            }
            Field field = ReflectionUtils.findField(User.class, fieldName);
            if (field == null) {
                throw new BadRequestException("Field not found: " + fieldName);
            }
            field.setAccessible(true);
            ReflectionUtils.setField(field, user, value);
        });
    }
    private void validatePassword(User user, UpdatePasswordReqDto req) {
        if(req.oldPassword().isBlank() || req.newPassword().isBlank() || req.repeatNewPassword().isBlank()) {
            throw new BadRequestException(AuthConstants.ALL_FIELDS_REQUIRED);
        }
        if(passwordEncoder.matches(user.getPassword(), req.oldPassword())) {
            throw new BadRequestException(AuthConstants.INVALID_PASSWORD_EXCEPTION);
        }
        if(!req.newPassword().equals(req.repeatNewPassword())) {
            throw new BadRequestException(AuthConstants.THE_SAME_PASSWORD_EXCEPTION);
        }
        if(passwordEncoder.matches(user.getPassword(), req.newPassword())) {
            throw new BadRequestException(AuthConstants.DUPLICATED_PASSWORD_EXCEPTION);
        }
    }

    private void validateFriendInvitationRequest(User user1, User user2) {
        if(user1.equals(user2)) {
            throw new BadRequestException(UserConstants.SAME_USER_EXCEPTION);
        }
        if(user1.getFriends().contains(user2)) {
            throw new BadRequestException(UserConstants.ALREADY_FRIENDS_EXCEPTION);
        }
    }

    private void sendFriendInvitation(FriendInvitationRequest request, User requester) {
        User sender = getUserById(request.senderId());
        AuthUtil.validateIfUserHimselfOrAdmin(requester, sender);
        User receiver = getUserById(request.receiverId());

        validateFriendInvitationRequest(sender, receiver);
        checkIfFriendInvitationExists(sender, receiver);

        FriendInvitation friendInvitation = FriendInvitation.builder()
                .sender(sender)
                .receiver(receiver)
                .build();

        friendInvitationRepository.save(friendInvitation);

        // Add notitfication to receiver
    }

    private void acceptFriendInvitation(FriendInvitationRequest request, User requester) {
        User receiver = getUserById(request.receiverId());
        AuthUtil.validateIfUserHimselfOrAdmin(requester, receiver);
        User sender = getUserById(request.senderId());

        validateFriendInvitationRequest(receiver, sender);
        FriendInvitation friendInvitation = getFriendInvitation(receiver, sender);

        receiver.getFriends().add(sender);
        sender.getFriends().add(receiver);

        userRepository.saveAll(List.of(receiver, sender));
        friendInvitationRepository.delete(friendInvitation);

        // Add notification to sender
    }

    private void rejectFriendInvitation(FriendInvitationRequest request, User requester) {
        User receiver = getUserById(request.receiverId());
        AuthUtil.validateIfUserHimselfOrAdmin(requester, receiver);
        User sender = getUserById(request.senderId());

        validateFriendInvitationRequest(receiver, sender);

        FriendInvitation friendInvitation = getFriendInvitation(receiver, sender);
        friendInvitationRepository.delete(friendInvitation);

        // Add notification to sender
    }

    private void deleteSentFriendInvitation(FriendInvitationRequest request, User requester) {
        User sender = getUserById(request.senderId());
        AuthUtil.validateIfUserHimselfOrAdmin(requester, sender);
        User receiver = getUserById(request.receiverId());

        validateFriendInvitationRequest(sender, receiver);

        FriendInvitation friendInvitation = getFriendInvitation(sender, receiver);
        friendInvitationRepository.delete(friendInvitation);
    }

    private void checkIfFriendInvitationExists(User user1, User user2) {
        boolean exists = friendInvitationRepository.existsFriendInvitationByUserIds(user1.getId(), user2.getId());
        if(exists) {
            throw new EntityExistsException(FriendInvitation.class, "(user1 id, user2 id)" , String.format("(%d, %d)", user1.getId(), user2.getId()));
        }
    }

    private FriendInvitation getFriendInvitation(User user1, User user2) {
        return friendInvitationRepository.findFriendInvitationByUserIds(user1.getId(), user2.getId())
                .orElseThrow(() -> new EntityNotFoundException(FriendInvitation.class, "(user1 id, user2 id)", String.format("(%d, %d)", user1.getId(), user2.getId())));
    }

}
