package com.piotrpabich.talktactics.user;

import com.piotrpabich.talktactics.user.dto.*;
import com.piotrpabich.talktactics.auth.AuthenticationService;
import com.piotrpabich.talktactics.user.entity.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.piotrpabich.talktactics.common.AppConst.API_V1;
import static com.piotrpabich.talktactics.common.AppConst.USERS_PATH;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_V1 + USERS_PATH)
@Tag(name = "UserController", description = "Users management APIs")
public class UserController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    @GetMapping
    public ResponseEntity<Page<UserDto>> queryUsers(
            final UserQueryCriteria criteria,
            final Pageable pageable
    ) {
        return ResponseEntity.ok(userService.queryAll(criteria, pageable));
    }

    @GetMapping("/profiles")
    public ResponseEntity<List<UserProfilePreviewDto>> getUserProfiles() {
        return ResponseEntity.ok(userService.getUserProfiles());
    }

    @GetMapping("/profiles/{userUuid}")
    public ResponseEntity<UserProfileDto> getUserProfileByUserUuid(
            @PathVariable UUID userUuid
    ) {
        return ResponseEntity.ok(userService.getUserProfileByUserUuid(userUuid));
    }

    @GetMapping("/{userUuid}/friends")
    public ResponseEntity<List<UserProfilePreviewDto>> getUserFriends(
            @PathVariable final UUID userUuid,
            final HttpServletRequest request
    ) {
        final var requester = authenticationService.getUserFromRequest(request);
        return ResponseEntity.ok(userService.getUserFriends(userUuid, requester));
    }

    @PostMapping("/friend-invitation")
    public ResponseEntity<Void> handleFriendInvitation(
            @RequestBody final FriendInvitationRequest friendInvitationRequest,
            final HttpServletRequest request
    ) {
        final var requester = authenticationService.getUserFromRequest(request);
        userService.handleFriendInvitationRequest(friendInvitationRequest, requester);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete-friend")
    public ResponseEntity<Void> deleteFriend(
            @RequestBody DeleteFriendRequest friendDeleteRequest,
            final HttpServletRequest request
    ) {
        final var requester = authenticationService.getUserFromRequest(request);
        userService.deleteFriend(friendDeleteRequest, requester);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userUuid}/received-friend-invitations")
    public ResponseEntity<List<FriendInvitationResponse>> getReceivedFriendInvitations(
            @PathVariable final UUID userUuid,
            @RequestParam(required = false) final Boolean withDetails,
            final HttpServletRequest request
    ) {
        final var requester = authenticationService.getUserFromRequest(request);
        return ResponseEntity.ok(userService.getReceivedFriendInvitations(userUuid, withDetails, requester));
    }

    @GetMapping("/{userUuid}/sent-friend-invitations")
    public ResponseEntity<List<FriendInvitationResponse>> getSentFriendInvitations(
            @PathVariable final UUID userUuid,
            @RequestParam(required = false) final Boolean withDetails,
            final HttpServletRequest request
    ) {
        final var requester = authenticationService.getUserFromRequest(request);
        return ResponseEntity.ok(userService.getSentFriendInvitations(userUuid, withDetails, requester));
    }

    @GetMapping("/{userUuid}")
    public ResponseEntity<User> getUserByUuid(
            @PathVariable final UUID userUuid,
            final HttpServletRequest request
    ) {
        final var requester = authenticationService.getUserFromRequest(request);
        return ResponseEntity.ok(userService.getUserByUuid(userUuid, requester));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(
            @PathVariable final String username,
            final HttpServletRequest request
    ) {
        final var requester = authenticationService.getUserFromRequest(request);
        return ResponseEntity.ok(userService.getUserByUsername(username, requester));
    }

    @PatchMapping("/{userUuid}")
    public ResponseEntity<Void> updateUser(
            @PathVariable final UUID userUuid,
            @RequestBody final UpdateUserRequest updateUserRequest,
            final HttpServletRequest request
    ) {
        final var requester = authenticationService.getUserFromRequest(request);
        userService.updateUser(userUuid, updateUserRequest, requester);
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping("/{userUuid}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable final UUID userUuid,
            final HttpServletRequest request
    ) {
        final var requester = authenticationService.getUserFromRequest(request);
        userService.deleteUser(userUuid, requester);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/password")
    public ResponseEntity<Void> updatePassword(
            @Valid @RequestBody final UpdatePasswordRequest updatePasswordRequest,
            final HttpServletRequest request
    ) {
        final var requester = authenticationService.getUserFromRequest(request);
        userService.updatePassword(updatePasswordRequest, requester);
        return ResponseEntity.noContent().build();
    }
}
