package com.piotrpabich.talktactics.user;

import com.piotrpabich.talktactics.user.dto.*;
import com.piotrpabich.talktactics.user.friend.FriendInvitationType;
import com.piotrpabich.talktactics.user.entity.User;
import com.piotrpabich.talktactics.user.friend.dto.DeleteFriendRequest;
import com.piotrpabich.talktactics.user.friend.dto.FriendInvitationRequest;
import com.piotrpabich.talktactics.user.friend.dto.FriendInvitationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserFacade {

    private final UserService userService;

    Page<UserDto> queryAll(
            final UserQueryCriteria criteria,
            final Pageable pageable
    ) {
        return userService.queryAll(criteria, pageable);
    }

    UserDto getUserByUuid(final UUID uuid, final User requester) {
        final var user = userService.getUserByUuid(uuid, requester);
        return UserDto.of(user);
    }

    UserDto getUserByUsername(final String username, final User requester) {
        final var user = userService.getUserByUsername(username, requester);
        return UserDto.of(user);
    }

    UserProfileDto getUserProfileByUserUuid(final UUID userUuid) {
        return userService.getUserProfileByUserUuid(userUuid);
    }

    List<UserProfilePreviewDto> getUserProfiles() {
        return userService.getUserProfiles();
    }

    List<UserProfilePreviewDto> getUserFriends(final UUID userUuid, final User requester) {
        return userService.getUserFriends(userUuid, requester);
    }

    void handleFriendInvitationRequest(
            final FriendInvitationRequest request,
            final User requester
    ) {
        userService.handleFriendInvitationRequest(request, requester);
    }

    void updateUser(
            final UUID userUuid,
            final UpdateUserRequest request,
            final User requester
    ) {
        userService.updateUser(userUuid, request, requester);
    }

    void deleteUser(final UUID userUuid, final User requester) {
        userService.deleteUser(userUuid, requester);
    }

    void deleteFriend(
            final DeleteFriendRequest request,
            final User requester
    ) {
        userService.deleteFriend(request, requester);
    }

    List<FriendInvitationResponse> getFriendInvitations(
            final UUID userUuid,
            final FriendInvitationType friendInvitationType,
            final User requester
    ) {
        return userService.getFriendInvitations(userUuid, friendInvitationType, requester);
    }
}
