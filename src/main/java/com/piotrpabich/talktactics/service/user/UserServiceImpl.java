package com.piotrpabich.talktactics.service.user;

import com.piotrpabich.talktactics.common.PageResult;
import com.piotrpabich.talktactics.dto.user.*;
import com.piotrpabich.talktactics.dto.user.req.DeleteFriendDto;
import com.piotrpabich.talktactics.dto.user.req.FriendInvitationRequest;
import com.piotrpabich.talktactics.dto.user.req.UpdatePasswordReqDto;
import com.piotrpabich.talktactics.dto.user.res.FriendInvitationDto;
import com.piotrpabich.talktactics.dto.user_course.UserCourseDto;
import com.piotrpabich.talktactics.dto.user_course.UserCourseQueryCriteria;
import com.piotrpabich.talktactics.exception.BadRequestException;
import com.piotrpabich.talktactics.exception.EntityExistsException;
import com.piotrpabich.talktactics.exception.EntityNotFoundException;
import com.piotrpabich.talktactics.entity.*;
import com.piotrpabich.talktactics.repository.FriendInvitationRepository;
import com.piotrpabich.talktactics.repository.UserRepository;
import com.piotrpabich.talktactics.service.user_course.UserCourseService;
import com.piotrpabich.talktactics.util.AuthUtil;
import com.piotrpabich.talktactics.util.Constants;
import com.piotrpabich.talktactics.util.PageUtil;
import com.piotrpabich.talktactics.util.QueryHelp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;

import static com.piotrpabich.talktactics.util.EmailValidator.isValidEmail;
import static com.piotrpabich.talktactics.util.Utils.getJsonPropertyFieldMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final FriendInvitationRepository friendInvitationRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserCourseService userCourseService;


//  PUBLIC
    @Override
    public PageResult<UserDto> queryAll(
            UserQueryCriteria criteria,
            Pageable pageable,
            User requester
    ) {
        AuthUtil.validateIfUserAdmin(requester);
        return queryAll(criteria, pageable);
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
        if(!userRepository.existsById(id)) {
            throw new EntityNotFoundException(User.class, "id", String.valueOf(id));
        }

        User user = getUserById(id);
        if(user.getRole().toString().equals(Constants.ADMIN)) {
            throw new BadCredentialsException("Cannot delete admin user");
        }
        userRepository.deleteById(id);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public User updateUser(long id, Map<String, Object> fields, User requester) {
        User user = getUserById(id);
        AuthUtil.validateIfUserHimselfOrAdmin(requester, user);
        validateFields(fields);

        Map<String, String> fieldMap = getJsonPropertyFieldMap(UpdateUserDto.class);

        fields.forEach((key, value) -> {
            String fieldName = fieldMap.get(key);
            if (fieldName == null) {
                throw new BadRequestException("Illegal fields given: " + key);
            }
            Field field = ReflectionUtils.findField(User.class, fieldName);
            if (field == null) {
                throw new BadRequestException("Field not found: " + fieldName);
            }
            field.setAccessible(true);
            ReflectionUtils.setField(field, user, value);
        });

        return userRepository.save(user);
    }
    private void validateFields(Map<String, Object> fields) {
        if(fields.containsKey("email")) {
            String email = (String) fields.get("email");
            if(!isValidEmail(email)) {
                throw new BadCredentialsException(Constants.EMAIL_FORBIDDEN_VALUES_EXCEPTION);
            }
        }
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
        Pageable pageable = PageRequest.of(0, 500, Sort.by(Sort.Order.desc("points")));
        UserCourseQueryCriteria criteria = UserCourseQueryCriteria.builder().userIds(Set.of(id)).build();
        List<UserCourseDto> userCourses = userCourseService.queryAll(criteria, pageable).content();

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
            throw new BadRequestException(Constants.NOT_FRIENDS_EXCEPTION);
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

    private PageResult<UserDto> queryAll(
            UserQueryCriteria criteria,
            Pageable pageable
    ) {
        Page<User> page = userRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(UserDto::from));
    }

    private void validatePassword(User user, UpdatePasswordReqDto req) {
        if(req.oldPassword().isBlank() || req.newPassword().isBlank() || req.repeatNewPassword().isBlank()) {
            throw new BadRequestException(Constants.ALL_FIELDS_REQUIRED);
        }
        if(passwordEncoder.matches(user.getPassword(), req.oldPassword())) {
            throw new BadRequestException(Constants.INVALID_PASSWORD_EXCEPTION);
        }
        if(!req.newPassword().equals(req.repeatNewPassword())) {
            throw new BadRequestException(Constants.THE_SAME_PASSWORD_EXCEPTION);
        }
        if(passwordEncoder.matches(user.getPassword(), req.newPassword())) {
            throw new BadRequestException(Constants.DUPLICATED_PASSWORD_EXCEPTION);
        }
    }

    private void validateFriendInvitationRequest(User user1, User user2) {
        if(user1.equals(user2)) {
            throw new BadRequestException(Constants.SAME_USER_EXCEPTION);
        }
        if(user1.getFriends().contains(user2)) {
            throw new BadRequestException(Constants.ALREADY_FRIENDS_EXCEPTION);
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
