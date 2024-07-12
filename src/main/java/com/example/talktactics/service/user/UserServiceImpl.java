package com.example.talktactics.service.user;

import com.example.talktactics.common.PageResult;
import com.example.talktactics.dto.user.*;
import com.example.talktactics.dto.user.req.DeleteFriendDto;
import com.example.talktactics.dto.user.req.FriendInvitationRequest;
import com.example.talktactics.dto.user.req.UpdatePasswordReqDto;
import com.example.talktactics.dto.user.res.FriendInvitationDto;
import com.example.talktactics.dto.user_course.UserCourseDto;
import com.example.talktactics.dto.user_course.UserCourseQueryCriteria;
import com.example.talktactics.exception.BadRequestException;
import com.example.talktactics.exception.EntityExistsException;
import com.example.talktactics.exception.EntityNotFoundException;
import com.example.talktactics.entity.*;
import com.example.talktactics.repository.FriendInvitationRepository;
import com.example.talktactics.repository.UserRepository;
import com.example.talktactics.service.user_course.UserCourseService;
import com.example.talktactics.util.Constants;
import com.example.talktactics.util.PageUtil;
import com.example.talktactics.util.QueryHelp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;

import static com.example.talktactics.util.EmailValidator.isValidEmail;
import static com.example.talktactics.util.Utils.getJsonPropertyFieldMap;

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
    public PageResult<UserDto> queryAll(UserQueryCriteria criteria, Pageable pageable) {
        validateAdmin();
        Page<User> page = userRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(UserDto::from));
    }
    @Override
    public User getUserById(long id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(User.class, "id", String.valueOf(id)));
    }
    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException(User.class, "username", username));
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(long id) {
        validateAdmin();
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
    public User updateUser(long id, Map<String, Object> fields) {
        User user = getUserById(id);
        validateCredentials(user);
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
    @Override
    public void validateAdmin() {
        if(!isAdmin()) {
            throw new BadCredentialsException(Constants.NOT_ENOUGH_AUTHORITIES_EXCEPTION);
        }
    }
    @Override
    public void validateCredentials(User user) {
        if(!isAdmin() && !isCurrentUser(user)) {
            throw new BadCredentialsException(Constants.NOT_ENOUGH_AUTHORITIES_EXCEPTION);
        }
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
    public User updatePassword(UpdatePasswordReqDto req) {
        User user = getUserById(req.id());

        validateCredentials(user);
        validatePassword(user, req);

        user.setPassword(passwordEncoder.encode(req.newPassword()));
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
    public List<UserProfilePreviewDto> getFriends(Long id) {
        User user = getUserById(id);
        validateCredentials(user);

        return user.getFriends().stream()
                .sorted(Comparator.comparingInt(User::getTotalPoints).reversed())
                .map(User::toUserProfilePreviewDto)
                .toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleFriendInvitationRequest(FriendInvitationRequest request) {
        switch (request.action()) {
            case SEND -> sendFriendInvitation(request);
            case ACCEPT -> acceptFriendInvitation(request);
            case REJECT -> rejectFriendInvitation(request);
            case DELETE -> deleteSentFriendInvitation(request);
            default -> throw new BadRequestException("Invalid action: " + request.action());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFriend(DeleteFriendDto request) {
        User user = getUserById(request.userId());
        validateCredentials(user);
        User friend = getUserById(request.friendId());

        if(!user.getFriends().contains(friend)) {
            throw new BadRequestException(Constants.NOT_FRIENDS_EXCEPTION);
        }

        user.getFriends().remove(friend);
        friend.getFriends().remove(user);

        userRepository.saveAll(List.of(user, friend));
    }

    @Override
    public List<FriendInvitationDto> getReceivedFriendInvitations(Long id, Boolean withDetails) {
        User user = getUserById(id);
        validateCredentials(user);

        return user.getReceivedFriendInvitations().stream()
                .map(friendInvitation -> friendInvitation.toFriendInvitationDto(withDetails))
                .toList();
    }

    @Override
    public List<FriendInvitationDto> getSentFriendInvitations(Long id, Boolean withDetails) {
        User user = getUserById(id);
        validateCredentials(user);

        return user.getSentFriendInvitations().stream()
                .map(friendInvitation -> friendInvitation.toFriendInvitationDto(withDetails))
                .toList();
    }

    //  PRIVATE
    private boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals(Constants.ADMIN));
    }


    private boolean isCurrentUser(User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName().equals(user.getUsername());
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

    private void sendFriendInvitation(FriendInvitationRequest request) {
        User sender = getUserById(request.senderId());
        validateCredentials(sender);
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

    private void acceptFriendInvitation(FriendInvitationRequest request) {
        User receiver = getUserById(request.receiverId());
        validateCredentials(receiver);
        User sender = getUserById(request.senderId());

        validateFriendInvitationRequest(receiver, sender);
        FriendInvitation friendInvitation = getFriendInvitation(receiver, sender);

        receiver.getFriends().add(sender);
        sender.getFriends().add(receiver);

        userRepository.saveAll(List.of(receiver, sender));
        friendInvitationRepository.delete(friendInvitation);

        // Add notification to sender
    }

    private void rejectFriendInvitation(FriendInvitationRequest request) {
        User receiver = getUserById(request.receiverId());
        validateCredentials(receiver);
        User sender = getUserById(request.senderId());

        validateFriendInvitationRequest(receiver, sender);

        FriendInvitation friendInvitation = getFriendInvitation(receiver, sender);
        friendInvitationRepository.delete(friendInvitation);

        // Add notification to sender
    }

    private void deleteSentFriendInvitation(FriendInvitationRequest request) {
        User sender = getUserById(request.senderId());
        validateCredentials(sender);
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
