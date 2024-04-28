package com.example.talktactics.service.user;

import com.example.talktactics.dto.user.UpdateUserDto;
import com.example.talktactics.dto.user.UserProfileDto;
import com.example.talktactics.dto.user.UserProfilePreviewDto;
import com.example.talktactics.dto.user.req.DeleteFriendDto;
import com.example.talktactics.dto.user.req.FriendInvitationRequest;
import com.example.talktactics.dto.user.req.UpdatePasswordReqDto;
import com.example.talktactics.dto.user.res.FriendInvitationDto;
import com.example.talktactics.dto.user_course.UserCourseDetailsDto;
import com.example.talktactics.exception.UserRuntimeException;
import com.example.talktactics.entity.*;
import com.example.talktactics.repository.FriendInvitationRepository;
import com.example.talktactics.repository.UserRepository;
import com.example.talktactics.service.user_course.UserCourseService;
import com.example.talktactics.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final FriendInvitationRepository friendInvitationRepository;
    private final UserCourseService userCourseService;
    private final PasswordEncoder passwordEncoder;


//  PUBLIC
    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }
    @Override
    public List<User> getUsers() {
        validateAdmin();
        return userRepository.findAll();
    }
    @Override
    public User getUserById(long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserRuntimeException(Constants.USER_NOT_FOUND_EXCEPTION));
    }
    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UserRuntimeException(Constants.USER_NOT_FOUND_EXCEPTION));
    }
    @Override
    public void deleteUser(long id) {
        validateAdmin();
        if(!userRepository.existsById(id)) {
            throw new UserRuntimeException(Constants.USER_NOT_FOUND_EXCEPTION);
        }

        User user = getUserById(id);
        if(user.getRole().toString().equals(Constants.ADMIN)) {
            throw new UserRuntimeException("Cannot delete admin user");
        }
        userRepository.deleteById(id);
    }
    @Override
    public User updateUser(long id, Map<String, Object> fields) {
        User user = getUserById(id);
        validateCredentials(user);
        validateFields(fields);

        Map<String, String> fieldMap = getJsonPropertyFieldMap(UpdateUserDto.class);

        fields.forEach((key, value) -> {
            String fieldName = fieldMap.get(key);
            if (fieldName == null) {
                throw new UserRuntimeException("Illegal fields given: " + key);
            }
            Field field = ReflectionUtils.findField(User.class, fieldName);
            if (field == null) {
                throw new UserRuntimeException("Field not found: " + fieldName);
            }
            field.setAccessible(true);
            ReflectionUtils.setField(field, user, value);
        });

        return userRepository.save(user);
    }
    @Override
    public void validateAdmin() {
        if(!isAdmin()) {
            throw new UserRuntimeException(Constants.NOT_ENOUGH_AUTHORITIES_EXCEPTION);
        }
    }
    @Override
    public void validateCredentials(User user) {
        if(!isAdmin() && !isCurrentUser(user)) {
            throw new UserRuntimeException(Constants.NOT_ENOUGH_AUTHORITIES_EXCEPTION);
        }
    }

    @Override
    public void validateFields(Map<String, Object> fields) {
        if(fields.containsKey("email")) {
            String email = (String) fields.get("email");
            if(!isValidEmail(email)) {
                throw new UserRuntimeException(Constants.EMAIL_FORBIDDEN_VALUES_EXCEPTION);
            }
        }
    }
    @Override
    public User updatePassword(UpdatePasswordReqDto req) {
        User user = getUserById(req.getId());

        validateCredentials(user);
        validatePassword(user, req);

        user.setPassword(passwordEncoder.encode(req.getNewPassword()));
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
        UserProfilePreviewDto userProfile = getUserById(id).toUserProfilePreviewDto();
        List<UserCourseDetailsDto> userCourses = userCourseService.getAllByUserId(id);

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
    public void sendFriendInvitation(FriendInvitationRequest request) {
        User sender = getUserById(request.getSenderId());
        validateCredentials(sender);
        User receiver = getUserById(request.getReceiverId());

        validateFriendInvitationRequest(sender, receiver);
        checkIfFriendInvitationExists(sender, receiver);

        FriendInvitation friendInvitation = FriendInvitation.builder()
                .sender(sender)
                .receiver(receiver)
                .build();

        friendInvitationRepository.save(friendInvitation);

        // Add notitfication to receiver
    }

    @Override
    public void acceptFriendInvitation(FriendInvitationRequest request) {
        User receiver = getUserById(request.getReceiverId());
        validateCredentials(receiver);
        User sender = getUserById(request.getSenderId());

        validateFriendInvitationRequest(receiver, sender);
        FriendInvitation friendInvitation = getFriendInvitation(receiver, sender);

        receiver.getFriends().add(sender);
        sender.getFriends().add(receiver);

        userRepository.saveAll(List.of(receiver, sender));
        friendInvitationRepository.delete(friendInvitation);

        // Add notification to sender
    }

    @Override
    public void rejectFriendInvitation(FriendInvitationRequest request) {
        User receiver = getUserById(request.getReceiverId());
        validateCredentials(receiver);
        User sender = getUserById(request.getSenderId());

        validateFriendInvitationRequest(receiver, sender);

        FriendInvitation friendInvitation = getFriendInvitation(receiver, sender);
        friendInvitationRepository.delete(friendInvitation);

        // Add notification to sender
    }

    @Override
    public void deleteFriend(DeleteFriendDto request) {
        User user = getUserById(request.getUserId());
        validateCredentials(user);
        User friend = getUserById(request.getFriendId());

        if(!user.getFriends().contains(friend)) {
            throw new UserRuntimeException(Constants.NOT_FRIENDS_EXCEPTION);
        }

        user.getFriends().remove(friend);
        friend.getFriends().remove(user);

        userRepository.saveAll(List.of(user, friend));
    }

    @Override
    public void deleteSentFriendInvitation(FriendInvitationRequest request) {
        User sender = getUserById(request.getSenderId());
        validateCredentials(sender);
        User receiver = getUserById(request.getReceiverId());

        validateFriendInvitationRequest(sender, receiver);

        FriendInvitation friendInvitation = getFriendInvitation(sender, receiver);
        friendInvitationRepository.delete(friendInvitation);
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
        if(req.getOldPassword().isBlank() || req.getNewPassword().isBlank() || req.getRepeatNewPassword().isBlank()) {
            throw new UserRuntimeException(Constants.ALL_FIELDS_REQUIRED);
        }
        if(passwordEncoder.matches(user.getPassword(), req.getOldPassword())) {
            throw new UserRuntimeException(Constants.INVALID_PASSWORD_EXCEPTION);
        }
        if(!req.getNewPassword().equals(req.getRepeatNewPassword())) {
            throw new UserRuntimeException(Constants.THE_SAME_PASSWORD_EXCEPTION);
        }
        if(passwordEncoder.matches(user.getPassword(), req.getNewPassword())) {
            throw new UserRuntimeException(Constants.DUPLICATED_PASSWORD_EXCEPTION);
        }
    }

    private void validateFriendInvitationRequest(User user1, User user2) {
        if(user1.equals(user2)) {
            throw new UserRuntimeException(Constants.SAME_USER_EXCEPTION);
        }
        if(user1.getFriends().contains(user2)) {
            throw new UserRuntimeException(Constants.ALREADY_FRIENDS_EXCEPTION);
        }
    }

    private void checkIfFriendInvitationExists(User user1, User user2) {
        boolean exists = friendInvitationRepository.existsFriendInvitationByUserIds(user1.getId(), user2.getId());
        if(exists) {
            throw new UserRuntimeException(Constants.FRIEND_INVITATION_EXISTS_EXCEPTION);
        }
    }

    private FriendInvitation getFriendInvitation(User user1, User user2) {
        return friendInvitationRepository.findFriendInvitationByUserIds(user1.getId(), user2.getId())
                .orElseThrow(() -> new UserRuntimeException(Constants.FRIEND_INVITATION_NOT_FOUND_EXCEPTION));
    }

}
