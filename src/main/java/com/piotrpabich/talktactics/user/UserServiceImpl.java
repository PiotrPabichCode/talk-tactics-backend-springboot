package com.piotrpabich.talktactics.user;

import com.piotrpabich.talktactics.auth.AuthConstants;
import com.piotrpabich.talktactics.common.PageResult;
import com.piotrpabich.talktactics.user.dto.*;
import com.piotrpabich.talktactics.user.dto.DeleteFriendRequest;
import com.piotrpabich.talktactics.user.dto.FriendInvitationRequest;
import com.piotrpabich.talktactics.user.dto.UpdatePasswordRequest;
import com.piotrpabich.talktactics.user.dto.FriendInvitationResponse;
import com.piotrpabich.talktactics.user_course.dto.UserCourseDto;
import com.piotrpabich.talktactics.exception.BadRequestException;
import com.piotrpabich.talktactics.exception.EntityExistsException;
import com.piotrpabich.talktactics.exception.EntityNotFoundException;
import com.piotrpabich.talktactics.user.entity.FriendInvitation;
import com.piotrpabich.talktactics.user.entity.User;
import com.piotrpabich.talktactics.common.util.PageUtil;
import com.piotrpabich.talktactics.common.QueryHelp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.util.*;

import static com.piotrpabich.talktactics.auth.AuthUtil.validateIfUserAdmin;
import static com.piotrpabich.talktactics.auth.AuthUtil.validateIfUserHimselfOrAdmin;
import static com.piotrpabich.talktactics.common.util.Utils.getJsonPropertyFieldMap;
import static com.piotrpabich.talktactics.user.dto.UserProfileDto.toUserProfileDto;
import static com.piotrpabich.talktactics.util.EmailValidator.isValidEmail;
import static java.util.Comparator.comparingInt;
import static org.springframework.util.ReflectionUtils.setField;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final FriendInvitationRepository friendInvitationRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public PageResult<UserDto> queryAll(
            final UserQueryCriteria criteria,
            final Pageable pageable
    ) {
        final var page = userRepository.findAll(getUsersSpecification(criteria), pageable);
        return PageUtil.toPage(page.map(UserDto::from));
    }

    @Override
    public User getUserById(final Long id, final User requester) {
        final var user = getUserById(id);
        validateIfUserHimselfOrAdmin(requester, user);
        return user;
    }

    @Override
    public User getUserById(final Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class, "id", String.valueOf(id)));
    }
    @Override
    public User getUserByUsername(final String username, final User requester) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(User.class, "username", username));
    }
    @Override
    @Transactional
    public void deleteUser(
            final Long id,
            final User requester
    ) {
        validateIfUserAdmin(requester);
        final var user = getUserById(id);
        if(AuthConstants.ADMIN.equals(user.getRole().toString())) {
            throw new BadCredentialsException("Cannot delete admin user");
        }
        userRepository.deleteById(id);
    }
    @Override
    @Transactional
    public void updateUser(
            final Long id,
            final Map<String, Object> newValues,
            final User requester
    ) {
        final var user = getUserById(id);
        validateIfUserHimselfOrAdmin(requester, user);
        validateUpdateUserRequest(user, newValues);
        userRepository.save(user);
    }
    @Override
    @Transactional
    public void updatePassword(
            final UpdatePasswordRequest updatePasswordRequest,
            final User requester
    ) {
        final var user = getUserById(updatePasswordRequest.id());
        validateIfUserHimselfOrAdmin(requester, user);
        validatePassword(user, updatePasswordRequest);
        user.setPassword(passwordEncoder.encode(updatePasswordRequest.newPassword()));
        userRepository.save(user);
    }

    @Override
    public List<UserProfilePreviewDto> getUserProfiles() {
        return userRepository.findAll().stream()
                .sorted(comparingInt(User::getTotalPoints).reversed())
                .map(User::toUserProfilePreviewDto)
                .toList();
    }

    @Override
    public UserProfileDto getUserProfileById(final Long id) {
        final var user = getUserById(id);
        final var userProfile = user.toUserProfilePreviewDto();
        final var userCourses = user.getUserCourses()
                .stream()
                .map(UserCourseDto::from)
                .toList();

        return toUserProfileDto(userProfile, userCourses);
    }

    @Override
    public List<UserProfilePreviewDto> getFriends(
            final Long id,
            final User requester
    ) {
        final var user = getUserById(id);
        validateIfUserHimselfOrAdmin(requester, user);

        return user.getFriends().stream()
                .sorted(comparingInt(User::getTotalPoints).reversed())
                .map(User::toUserProfilePreviewDto)
                .toList();
    }

    @Override
    @Transactional
    public void handleFriendInvitationRequest(
            final FriendInvitationRequest request,
            final User requester
    ) {
        switch (request.action()) {
            case SEND -> sendFriendInvitation(request, requester);
            case ACCEPT -> acceptFriendInvitation(request, requester);
            case REJECT -> rejectFriendInvitation(request, requester);
            case DELETE -> deleteSentFriendInvitation(request, requester);
            default -> throw new BadRequestException("Invalid action: " + request.action());
        }
    }

    @Override
    @Transactional
    public void deleteFriend(
            final DeleteFriendRequest request,
            final User requester
    ) {
        final var user = getUserById(request.userId());
        validateIfUserHimselfOrAdmin(requester, user);
        final var friend = getUserById(request.friendId());

        if(!user.getFriends().contains(friend)) {
            throw new BadRequestException(UserConstants.NOT_FRIENDS_EXCEPTION);
        }

        user.getFriends().remove(friend);
        friend.getFriends().remove(user);

        userRepository.saveAll(List.of(user, friend));
    }

    @Override
    public List<FriendInvitationResponse> getReceivedFriendInvitations(
            final Long id,
            final Boolean withDetails,
            final User requester
    ) {
        final var user = getUserById(id);
        validateIfUserHimselfOrAdmin(requester, user);

        return user.getReceivedFriendInvitations().stream()
                .map(friendInvitation -> friendInvitation.toFriendInvitationDto(withDetails))
                .toList();
    }

    @Override
    public List<FriendInvitationResponse> getSentFriendInvitations(
            final Long id,
            final Boolean withDetails,
            final User requester
    ) {
        final var user = getUserById(id);
        validateIfUserHimselfOrAdmin(requester, user);

        return user.getSentFriendInvitations().stream()
                .map(friendInvitation -> friendInvitation.toFriendInvitationDto(withDetails))
                .toList();
    }

    private Specification<User> getUsersSpecification(final UserQueryCriteria criteria) {
        return (root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder);
    }

    private void validateUpdateUserRequest(
            final User user,
            final Map<String, Object> newValues
    ) {
        if(newValues.containsKey("email")) {
            final var email = (String) newValues.get("email");
            if(!isValidEmail(email)) {
                throw new BadCredentialsException(AuthConstants.EMAIL_FORBIDDEN_VALUES_EXCEPTION);
            }
        }

        final var fieldMap = getJsonPropertyFieldMap(UpdateUserDto.class);

        newValues.forEach((key, value) -> {
            final var fieldName = fieldMap.get(key);
            if (fieldName == null) {
                throw new BadRequestException("Illegal new values given: " + key);
            }
            final var field = ReflectionUtils.findField(User.class, fieldName);
            if (field == null) {
                throw new BadRequestException("Field not found: " + fieldName);
            }
            field.setAccessible(true);
            setField(field, user, value);
        });
    }
    private void validatePassword(
            final User user,
            final UpdatePasswordRequest request
    ) {
        if(request.oldPassword().isBlank() || request.newPassword().isBlank() || request.repeatNewPassword().isBlank()) {
            throw new BadRequestException(AuthConstants.ALL_FIELDS_REQUIRED);
        }
        if(passwordEncoder.matches(user.getPassword(), request.oldPassword())) {
            throw new BadRequestException(AuthConstants.INVALID_PASSWORD_EXCEPTION);
        }
        if(!request.newPassword().equals(request.repeatNewPassword())) {
            throw new BadRequestException(AuthConstants.THE_SAME_PASSWORD_EXCEPTION);
        }
        if(passwordEncoder.matches(user.getPassword(), request.newPassword())) {
            throw new BadRequestException(AuthConstants.DUPLICATED_PASSWORD_EXCEPTION);
        }
    }

    private void validateFriendInvitationRequest(
            final User user1,
            final User user2
    ) {
        if(user1.equals(user2)) {
            throw new BadRequestException(UserConstants.SAME_USER_EXCEPTION);
        }
        if(user1.getFriends().contains(user2)) {
            throw new BadRequestException(UserConstants.ALREADY_FRIENDS_EXCEPTION);
        }
    }

    private void sendFriendInvitation(
            final FriendInvitationRequest request,
            final User requester
    ) {
        final var sender = getUserById(request.senderId());
        validateIfUserHimselfOrAdmin(requester, sender);
        final var receiver = getUserById(request.receiverId());

        validateFriendInvitationRequest(sender, receiver);
        checkIfFriendInvitationExists(sender, receiver);

        final var friendInvitation = FriendInvitation.builder()
                .sender(sender)
                .receiver(receiver)
                .build();

        friendInvitationRepository.save(friendInvitation);

        // Add notitfication to receiver
    }

    private void acceptFriendInvitation(
            final FriendInvitationRequest request,
            final User requester
    ) {
        final var receiver = getUserById(request.receiverId());
        validateIfUserHimselfOrAdmin(requester, receiver);
        final var sender = getUserById(request.senderId());

        validateFriendInvitationRequest(receiver, sender);
        final var friendInvitation = getFriendInvitation(receiver, sender);

        receiver.getFriends().add(sender);
        sender.getFriends().add(receiver);

        userRepository.saveAll(List.of(receiver, sender));
        friendInvitationRepository.delete(friendInvitation);

        // Add notification to sender
    }

    private void rejectFriendInvitation(
            final FriendInvitationRequest request,
            final User requester
    ) {
        final var receiver = getUserById(request.receiverId());
        validateIfUserHimselfOrAdmin(requester, receiver);
        final var sender = getUserById(request.senderId());

        validateFriendInvitationRequest(receiver, sender);

        final var friendInvitation = getFriendInvitation(receiver, sender);
        friendInvitationRepository.delete(friendInvitation);

        // Add notification to sender
    }

    private void deleteSentFriendInvitation(
            final FriendInvitationRequest request,
            final User requester
    ) {
        final var sender = getUserById(request.senderId());
        validateIfUserHimselfOrAdmin(requester, sender);
        final var receiver = getUserById(request.receiverId());

        validateFriendInvitationRequest(sender, receiver);

        final var friendInvitation = getFriendInvitation(sender, receiver);
        friendInvitationRepository.delete(friendInvitation);
    }

    private void checkIfFriendInvitationExists(
            final User user1,
            final User user2
    ) {
        final var exists = friendInvitationRepository.existsFriendInvitationByUserIds(user1.getId(), user2.getId());
        if(exists) {
            throw new EntityExistsException(FriendInvitation.class, "(user1 id, user2 id)" , String.format("(%d, %d)", user1.getId(), user2.getId()));
        }
    }

    private FriendInvitation getFriendInvitation(
            final User user1,
            final User user2
    ) {
        return friendInvitationRepository.findFriendInvitationByUserIds(user1.getId(), user2.getId())
                .orElseThrow(() -> new EntityNotFoundException(FriendInvitation.class, "(user1 id, user2 id)", String.format("(%d, %d)", user1.getId(), user2.getId())));
    }
}
