package com.piotrpabich.talktactics.user;

import com.piotrpabich.talktactics.auth.AuthenticationService;
import com.piotrpabich.talktactics.user.dto.*;
import com.piotrpabich.talktactics.user.entity.FriendInvitationType;
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
@Tag(name = "UserController")
public class UserController {

    private final UserFacade userFacade;
    private final AuthenticationService authenticationService;

    @GetMapping
    public ResponseEntity<Page<UserDto>> queryUsers(
            final UserQueryCriteria criteria,
            final Pageable pageable
    ) {
        return ResponseEntity.ok(userFacade.queryAll(criteria, pageable));
    }

    @GetMapping("/profiles")
    public ResponseEntity<List<UserProfilePreviewDto>> getUserProfiles() {
        return ResponseEntity.ok(userFacade.getUserProfiles());
    }

    @GetMapping("/profiles/{userUuid}")
    public ResponseEntity<UserProfileDto> getUserProfileByUserUuid(
            @PathVariable UUID userUuid
    ) {
        return ResponseEntity.ok(userFacade.getUserProfileByUserUuid(userUuid));
    }

    @GetMapping("/{userUuid}/friends")
    public ResponseEntity<List<UserProfilePreviewDto>> getUserFriends(
            @PathVariable final UUID userUuid,
            final HttpServletRequest request
    ) {
        final var requester = authenticationService.getUserFromRequest(request);
        return ResponseEntity.ok(userFacade.getUserFriends(userUuid, requester));
    }

    @PostMapping("/friend-invitation")
    public ResponseEntity<Void> handleFriendInvitation(
            @RequestBody @Valid final FriendInvitationRequest friendInvitationRequest,
            final HttpServletRequest request
    ) {
        final var requester = authenticationService.getUserFromRequest(request);
        userFacade.handleFriendInvitationRequest(friendInvitationRequest, requester);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/friend")
    public ResponseEntity<Void> deleteFriend(
            @RequestBody @Valid final DeleteFriendRequest friendDeleteRequest,
            final HttpServletRequest request
    ) {
        final var requester = authenticationService.getUserFromRequest(request);
        userFacade.deleteFriend(friendDeleteRequest, requester);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userUuid}/friend-invitations")
    public ResponseEntity<List<FriendInvitationResponse>> getReceivedFriendInvitations(
            @PathVariable final UUID userUuid,
            @RequestParam final FriendInvitationType type,
            final HttpServletRequest request
    ) {
        final var requester = authenticationService.getUserFromRequest(request);
        return ResponseEntity.ok(userFacade.getFriendInvitations(userUuid, type, requester));
    }

    @GetMapping("/{userUuid}")
    public ResponseEntity<UserDto> getUserByUuid(
            @PathVariable final UUID userUuid,
            final HttpServletRequest request
    ) {
        final var requester = authenticationService.getUserFromRequest(request);
        return ResponseEntity.ok(userFacade.getUserByUuid(userUuid, requester));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserDto> getUserByUsername(
            @PathVariable final String username,
            final HttpServletRequest request
    ) {
        final var requester = authenticationService.getUserFromRequest(request);
        return ResponseEntity.ok(userFacade.getUserByUsername(username, requester));
    }

    @PatchMapping("/{userUuid}")
    public ResponseEntity<Void> updateUser(
            @PathVariable final UUID userUuid,
            @RequestBody final UpdateUserRequest updateUserRequest,
            final HttpServletRequest request
    ) {
        final var requester = authenticationService.getUserFromRequest(request);
        userFacade.updateUser(userUuid, updateUserRequest, requester);
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping("/{userUuid}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable final UUID userUuid,
            final HttpServletRequest request
    ) {
        final var requester = authenticationService.getUserFromRequest(request);
        userFacade.deleteUser(userUuid, requester);
        return ResponseEntity.noContent().build();
    }
}
