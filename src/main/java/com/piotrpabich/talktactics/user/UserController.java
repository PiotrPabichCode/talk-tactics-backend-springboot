package com.piotrpabich.talktactics.user;

import com.piotrpabich.talktactics.common.PageResult;
import com.piotrpabich.talktactics.user.dto.UserDto;
import com.piotrpabich.talktactics.user.dto.UserProfileDto;
import com.piotrpabich.talktactics.user.dto.UserProfilePreviewDto;
import com.piotrpabich.talktactics.user.dto.UserQueryCriteria;
import com.piotrpabich.talktactics.user.dto.DeleteFriendRequest;
import com.piotrpabich.talktactics.user.dto.FriendInvitationRequest;
import com.piotrpabich.talktactics.user.dto.UpdatePasswordRequest;
import com.piotrpabich.talktactics.user.dto.FriendInvitationResponse;
import com.piotrpabich.talktactics.auth.AuthenticationService;
import com.piotrpabich.talktactics.user.entity.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.piotrpabich.talktactics.common.AppConst.API_V1;
import static com.piotrpabich.talktactics.common.AppConst.USERS_PATH;

@RestController
@AllArgsConstructor
@RequestMapping(API_V1 + USERS_PATH)
@Tag(name = "UserController", description = "Users management APIs")
public class UserController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    @GetMapping("/all")
    public ResponseEntity<PageResult<UserDto>> queryUsers(
            final UserQueryCriteria criteria,
            final Pageable pageable
    ) {
        return ResponseEntity.ok(userService.queryAll(criteria, pageable));
    }

    @GetMapping("/profiles")
    public ResponseEntity<List<UserProfilePreviewDto>> getUserProfiles() {
        return ResponseEntity.ok(userService.getUserProfiles());
    }

    @GetMapping("/profiles/{userId}")
    public ResponseEntity<UserProfileDto> getUserProfileById(
            @PathVariable final Long userId
    ) {
        return ResponseEntity.ok(userService.getUserProfileById(userId));
    }

    @GetMapping("/id/{id}/friends")
    public ResponseEntity<List<UserProfilePreviewDto>> getFriends(
            @PathVariable final Long id,
            final HttpServletRequest request
    ) {
        final var requester = authenticationService.getUserFromRequest(request);
        return ResponseEntity.ok(userService.getFriends(id, requester));
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

    @GetMapping("/id/{id}/received-friend-invitations")
    public ResponseEntity<List<FriendInvitationResponse>> getReceivedFriendInvitations(
            @PathVariable final Long id,
            @RequestParam(required = false) final Boolean withDetails,
            final HttpServletRequest request
    ) {
        final var requester = authenticationService.getUserFromRequest(request);
        return ResponseEntity.ok(userService.getReceivedFriendInvitations(id, withDetails, requester));
    }

    @GetMapping("/id/{id}/sent-friend-invitations")
    public ResponseEntity<List<FriendInvitationResponse>> getSentFriendInvitations(
            @PathVariable final Long id,
            @RequestParam(required = false) final Boolean withDetails,
            final HttpServletRequest request
    ) {
        final var requester = authenticationService.getUserFromRequest(request);
        return ResponseEntity.ok(userService.getSentFriendInvitations(id, withDetails, requester));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<User> getUserById(
            @PathVariable final Long id,
            final HttpServletRequest request
    ) {
        final var requester = authenticationService.getUserFromRequest(request);
        return ResponseEntity.ok(userService.getUserById(id, requester));
    }
    @GetMapping("/username/{username}")
    public ResponseEntity<User> findByUsername(
            @PathVariable final String username,
            final HttpServletRequest request
    ) {
        final var requester = authenticationService.getUserFromRequest(request);
        return ResponseEntity.ok(userService.getUserByUsername(username, requester));
    }

    @PatchMapping("/id/{id}")
    public ResponseEntity<Void> updateUser(
            @PathVariable final Long id,
            @RequestBody final Map<String, Object> fields,
            final HttpServletRequest request
    ) {
        final var requester = authenticationService.getUserFromRequest(request);
        userService.updateUser(id, fields, requester);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable final Long id,
            final HttpServletRequest request
    ) {
        final var requester = authenticationService.getUserFromRequest(request);
        userService.deleteUser(id, requester);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/password")
    public ResponseEntity<Void> updatePassword(
            @RequestBody final UpdatePasswordRequest updatePasswordRequest,
            final HttpServletRequest request
    ) {
        final var requester = authenticationService.getUserFromRequest(request);
        userService.updatePassword(updatePasswordRequest, requester);
        return ResponseEntity.noContent().build();
    }
}
