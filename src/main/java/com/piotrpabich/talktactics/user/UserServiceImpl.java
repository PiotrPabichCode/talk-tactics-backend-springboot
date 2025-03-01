package com.piotrpabich.talktactics.user;

import com.piotrpabich.talktactics.auth.AuthConstants;
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
import com.piotrpabich.talktactics.common.QueryHelp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.piotrpabich.talktactics.auth.AuthUtil.validateIfUserAdmin;
import static com.piotrpabich.talktactics.auth.AuthUtil.validateIfUserHimselfOrAdmin;
import static java.util.Comparator.comparingInt;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final FriendInvitationRepository friendInvitationRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Page<UserDto> queryAll(
            final UserQueryCriteria criteria,
            final Pageable pageable
    ) {
        return userRepository.findAll(getUsersSpecification(criteria), pageable)
                .map(UserDto::of);
    }

    @Override
    public User getUserByUuid(final UUID userUuid, final User requester) {
        final var user = getUserByUuid(userUuid);
        validateIfUserHimselfOrAdmin(requester, user);
        return user;
    }

    @Override
    public User getUserByUuid(final UUID userUuid) {
        return userRepository.findByUuid(userUuid)
                .orElseThrow(() -> new EntityNotFoundException(User.class, "uuid", String.valueOf(userUuid)));
    }

    @Override
    public User getUserByUsername(final String username, final User requester) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(User.class, "username", username));
    }

    @Override
    public void deleteUser(final UUID userUuid, final User requester) {
        validateIfUserAdmin(requester);
        final var user = getUserByUuid(userUuid);
        if(AuthConstants.ADMIN.equals(user.getRole().toString())) {
            throw new BadCredentialsException("Cannot delete admin user");
        }
        userRepository.deleteByUuid(userUuid);
    }

    @Override
    public void updateUser(
            final UUID userUuid,
            final UpdateUserRequest request,
            final User requester
    ) {
        final var user = getUserByUuid(userUuid);
        validateIfUserHimselfOrAdmin(requester, user);
        userRepository.save(user);
    }

    @Override
    public void updatePassword(
            final UpdatePasswordRequest updatePasswordRequest,
            final User requester
    ) {
        final var user = getUserByUuid(updatePasswordRequest.userUuid());
        validateIfUserHimselfOrAdmin(requester, user);
        validatePassword(user, updatePasswordRequest);
        user.setPassword(passwordEncoder.encode(updatePasswordRequest.newPassword()));
        userRepository.save(user);
    }

    @Override
    public List<UserProfilePreviewDto> getUserProfiles() {
        return userRepository.findAll().stream()
                .sorted(comparingInt(User::getTotalPoints).reversed())
                .map(UserProfilePreviewDto::of)
                .toList();
    }

    @Override
    public UserProfileDto getUserProfileByUserUuid(final UUID userUuid) {
        final var user = getUserByUuid(userUuid);
        final var userProfile = UserProfilePreviewDto.of(user);
        final var userCourses = user.getUserCourses()
                .stream()
                .map(UserCourseDto::of)
                .toList();

        return UserProfileDto.of(userProfile, userCourses);
    }

    @Override
    public List<UserProfilePreviewDto> getUserFriends(final UUID userUuid, final User requester) {
        final var user = getUserByUuid(userUuid);
        validateIfUserHimselfOrAdmin(requester, user);

        return user.getFriends().stream()
                .sorted(comparingInt(User::getTotalPoints).reversed())
                .map(UserProfilePreviewDto::of)
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
        final var user = getUserByUuid(request.userUuid());
        validateIfUserHimselfOrAdmin(requester, user);
        final var friend = getUserByUuid(request.friendUuid());

        if(!user.getFriends().contains(friend)) {
            throw new BadRequestException(UserConstants.NOT_FRIENDS_EXCEPTION);
        }

        user.getFriends().remove(friend);
        friend.getFriends().remove(user);

        userRepository.saveAll(List.of(user, friend));
    }

    @Override
    public List<FriendInvitationResponse> getReceivedFriendInvitations(
            final UUID userUuid,
            final Boolean withDetails,
            final User requester
    ) {
        final var user = getUserByUuid(userUuid);
        validateIfUserHimselfOrAdmin(requester, user);

        return user.getReceivedFriendInvitations().stream()
                .map(friendInvitation -> friendInvitation.toFriendInvitationDto(withDetails))
                .toList();
    }

    @Override
    public List<FriendInvitationResponse> getSentFriendInvitations(
            final UUID userUuid,
            final Boolean withDetails,
            final User requester
    ) {
        final var user = getUserByUuid(userUuid);
        validateIfUserHimselfOrAdmin(requester, user);

        return user.getSentFriendInvitations().stream()
                .map(friendInvitation -> friendInvitation.toFriendInvitationDto(withDetails))
                .toList();
    }

    private Specification<User> getUsersSpecification(final UserQueryCriteria criteria) {
        return (root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder);
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
        if(user1.getUuid().equals(user2.getUuid())) {
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
        final var sender = getUserByUuid(request.senderUuid());
        validateIfUserHimselfOrAdmin(requester, sender);
        final var receiver = getUserByUuid(request.receiverUuid());

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
        final var receiver = getUserByUuid(request.receiverUuid());
        validateIfUserHimselfOrAdmin(requester, receiver);
        final var sender = getUserByUuid(request.senderUuid());

        validateFriendInvitationRequest(receiver, sender);
        final var friendInvitation = getFriendInvitation(receiver.getUuid(), sender.getUuid());

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
        final var receiver = getUserByUuid(request.receiverUuid());
        validateIfUserHimselfOrAdmin(requester, receiver);
        final var sender = getUserByUuid(request.senderUuid());

        validateFriendInvitationRequest(receiver, sender);

        final var friendInvitation = getFriendInvitation(receiver.getUuid(), sender.getUuid());
        friendInvitationRepository.delete(friendInvitation);

        // Add notification to sender
    }

    private void deleteSentFriendInvitation(
            final FriendInvitationRequest request,
            final User requester
    ) {
        final var sender = getUserByUuid(request.senderUuid());
        validateIfUserHimselfOrAdmin(requester, sender);
        final var receiver = getUserByUuid(request.receiverUuid());

        validateFriendInvitationRequest(sender, receiver);

        final var friendInvitation = getFriendInvitation(sender.getUuid(), receiver.getUuid());
        friendInvitationRepository.delete(friendInvitation);
    }

    private void checkIfFriendInvitationExists(final User user1, final User user2) {
        final var exists = friendInvitationRepository.existsFriendInvitation(user1.getUuid(), user2.getUuid());
        if (exists) {
            throw new EntityExistsException(
                    FriendInvitation.class,
                    "(user1Uuid, user2Uuid)",
                    String.format("(%s, %s)", user1.getUuid(), user2.getUuid())
            );
        }
    }

    private FriendInvitation getFriendInvitation(final UUID user1Uuid, final UUID user2Uuid) {
        return friendInvitationRepository.findFriendInvitation(user1Uuid, user2Uuid)
                .orElseThrow(() -> new EntityNotFoundException(
                        FriendInvitation.class,
                        "(user1Uuid, user2Uuid)",
                        String.format("(%s, %s)", user1Uuid, user2Uuid)
                ));
    }
}
