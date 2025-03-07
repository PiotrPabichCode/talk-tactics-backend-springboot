package com.piotrpabich.talktactics.user;

import com.piotrpabich.talktactics.auth.AuthConstants;
import com.piotrpabich.talktactics.exception.NotFoundException;
import com.piotrpabich.talktactics.user.dto.*;
import com.piotrpabich.talktactics.user.dto.DeleteFriendRequest;
import com.piotrpabich.talktactics.user.dto.FriendInvitationRequest;
import com.piotrpabich.talktactics.user.dto.FriendInvitationResponse;
import com.piotrpabich.talktactics.user.entity.FriendInvitationType;
import com.piotrpabich.talktactics.user_course.dto.UserCourseDto;
import com.piotrpabich.talktactics.exception.BadRequestException;
import com.piotrpabich.talktactics.user.entity.FriendInvitation;
import com.piotrpabich.talktactics.user.entity.User;
import com.piotrpabich.talktactics.common.QueryHelp;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.piotrpabich.talktactics.auth.AuthUtil.validateIfUserAdmin;
import static com.piotrpabich.talktactics.auth.AuthUtil.validateIfUserHimselfOrAdmin;
import static java.util.Comparator.comparingInt;

@Service
@Log4j2
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FriendInvitationService friendInvitationService;

    Page<UserDto> queryAll(
            final UserQueryCriteria criteria,
            final Pageable pageable
    ) {
        return userRepository.findAll(getUsersSpecification(criteria), pageable)
                .map(UserDto::of);
    }

    User getUserByUuid(final UUID userUuid, final User requester) {
        final var user = getUserByUuid(userUuid);
        validateIfUserHimselfOrAdmin(requester, user);
        return user;
    }

    public User getUserByUuid(final UUID userUuid) {
        return userRepository.findByUuid(userUuid)
                .orElseThrow(() -> new NotFoundException(String.format("User with uuid: %s was not found", userUuid)));
    }

    public User getUserByUuidWithFriends(final UUID userUuid) {
        return userRepository.findByUuidWithFriends(userUuid)
                .orElseThrow(() -> new NotFoundException(String.format("User with uuid: %s was not found", userUuid)));
    }

    public User getUserByUsername(final String username, final User requester) {
        final var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(String.format("User with username: %s was not found", username)));
        validateIfUserHimselfOrAdmin(requester, user);
        return user;
    }

    void deleteUser(final UUID userUuid, final User requester) {
        validateIfUserAdmin(requester);
        final var user = getUserByUuid(userUuid);
        if (AuthConstants.ADMIN.equals(user.getRole().toString())) {
            throw new BadCredentialsException("Cannot delete admin user");
        }
        userRepository.delete(user);
    }

    void updateUser(
            final UUID userUuid,
            final UpdateUserRequest request,
            final User requester
    ) {
        final var user = getUserByUuid(userUuid);
        validateIfUserHimselfOrAdmin(requester, user);
        final var updatedUser = UpdateUserRequest.of(user, request);
        userRepository.save(updatedUser);
    }

    public List<UserProfilePreviewDto> getUserProfiles() {
        return userRepository.findAll().stream()
                .sorted(comparingInt(User::getTotalPoints).reversed())
                .map(UserProfilePreviewDto::of)
                .toList();
    }

    UserProfileDto getUserProfileByUserUuid(final UUID userUuid) {
        final var user = userRepository.findByUuidWithCourses(userUuid)
                .orElseThrow(() -> new NotFoundException(String.format("User with uuid: %s was not found", userUuid)));
        final var userProfile = UserProfilePreviewDto.of(user);
        final var userCourses = user.getUserCourses()
                .stream()
                .map(UserCourseDto::of)
                .toList();

        return UserProfileDto.of(userProfile, userCourses);
    }

    public List<UserProfilePreviewDto> getUserFriends(final UUID userUuid, final User requester) {
        final var user = getUserByUuidWithFriends(userUuid);
        validateIfUserHimselfOrAdmin(requester, user);

        return user.getFriends().stream()
                .sorted(comparingInt(User::getTotalPoints).reversed())
                .map(UserProfilePreviewDto::of)
                .toList();
    }

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

    @Transactional
    void deleteFriend(
            final DeleteFriendRequest request,
            final User requester
    ) {
        final var user = getUserByUuidWithFriends(request.userUuid());
        validateIfUserHimselfOrAdmin(requester, user);
        final var friend = getUserByUuidWithFriends(request.friendUuid());

        if (!user.getFriends().contains(friend)) {
            throw new BadRequestException(UserConstants.NOT_FRIENDS_EXCEPTION);
        }
        user.removeFriend(friend);
        userRepository.saveAll(List.of(user, friend));
    }

    List<FriendInvitationResponse> getFriendInvitations(
            final UUID userUuid,
            final FriendInvitationType friendInvitationType,
            final User requester
    ) {
        final var user = getUserByUuid(userUuid);
        validateIfUserHimselfOrAdmin(requester, user);
        return friendInvitationService.getFriendInvitations(user.getId(), friendInvitationType).stream()
                .map(FriendInvitationResponse::of)
                .toList();
    }

    private Specification<User> getUsersSpecification(final UserQueryCriteria criteria) {
        return (root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder);
    }

    private void validateFriendInvitationRequest(final User user1, final User user2) {
        if (user1.getUuid().equals(user2.getUuid())) {
            throw new BadRequestException(UserConstants.SAME_USER_EXCEPTION);
        }
        if (user1.getFriends().contains(user2)) {
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
        friendInvitationService.existsFriendInvitation(sender.getUuid(), receiver.getUuid());
        friendInvitationService.save(new FriendInvitation(sender, receiver));

        // Add notitfication to receiver
    }

    private void acceptFriendInvitation(
            final FriendInvitationRequest request,
            final User requester
    ) {
        final var receiver = getUserByUuidWithFriends(request.receiverUuid());
        validateIfUserHimselfOrAdmin(requester, receiver);
        final var sender = getUserByUuidWithFriends(request.senderUuid());
        validateFriendInvitationRequest(receiver, sender);
        sender.addFriend(receiver);
        userRepository.saveAll(List.of(receiver, sender));
        friendInvitationService.delete(sender.getUuid(), receiver.getUuid());

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
        friendInvitationService.delete(sender.getUuid(), receiver.getUuid());

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
        friendInvitationService.delete(sender.getUuid(), receiver.getUuid());
    }

}
