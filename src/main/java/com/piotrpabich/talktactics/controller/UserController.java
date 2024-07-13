package com.piotrpabich.talktactics.controller;

import com.piotrpabich.talktactics.common.PageResult;
import com.piotrpabich.talktactics.dto.user.UserDto;
import com.piotrpabich.talktactics.dto.user.UserProfileDto;
import com.piotrpabich.talktactics.dto.user.UserProfilePreviewDto;
import com.piotrpabich.talktactics.dto.user.UserQueryCriteria;
import com.piotrpabich.talktactics.dto.user.req.DeleteFriendDto;
import com.piotrpabich.talktactics.dto.user.req.FriendInvitationRequest;
import com.piotrpabich.talktactics.dto.user.req.UpdatePasswordReqDto;
import com.piotrpabich.talktactics.dto.user.res.FriendInvitationDto;
import com.piotrpabich.talktactics.entity.*;
import com.piotrpabich.talktactics.service.auth.AuthenticationService;
import com.piotrpabich.talktactics.service.user.UserService;
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
@Tag(name = "Users", description = "Users management APIs")
public class UserController {
    private final UserService userService;
    private final AuthenticationService authenticationService;
    @GetMapping("/all")
    public ResponseEntity<PageResult<UserDto>> queryUsers(
            UserQueryCriteria criteria,
            Pageable pageable
    ) {
        return ResponseEntity.ok(userService.queryAll(criteria, pageable));
    }

    @GetMapping("/profiles")
    public ResponseEntity<List<UserProfilePreviewDto>> getUserProfiles() {
        return ResponseEntity.ok(userService.getUserProfiles());
    }

    @GetMapping("/profiles/{userId}")
    public ResponseEntity<UserProfileDto> getUserProfileById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserProfileById(userId));
    }

    @GetMapping("/id/{id}/friends")
    public ResponseEntity<List<UserProfilePreviewDto>> getFriends(
            @PathVariable Long id,
            final HttpServletRequest request
    ) {
        User requester = authenticationService.getUserFromRequest(request);
        return ResponseEntity.ok(userService.getFriends(id, requester));
    }

    @PostMapping("/friend-invitation")
    public ResponseEntity<Object> handleFriendInvitation(
            @RequestBody FriendInvitationRequest friendInvitationRequest,
            final HttpServletRequest request
    ) {
        User requester = authenticationService.getUserFromRequest(request);
        userService.handleFriendInvitationRequest(friendInvitationRequest, requester);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete-friend")
    public ResponseEntity<Object> deleteFriend(
            @RequestBody DeleteFriendDto friendDeleteRequest,
            final HttpServletRequest request
    ) {
        User requester = authenticationService.getUserFromRequest(request);
        userService.deleteFriend(friendDeleteRequest, requester);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/id/{id}/received-friend-invitations")
    public ResponseEntity<List<FriendInvitationDto>> getReceivedFriendInvitations(
            @PathVariable Long id,
            @RequestParam(required = false) Boolean withDetails,
            final HttpServletRequest request
    ) {
        User requester = authenticationService.getUserFromRequest(request);
        return ResponseEntity.ok(userService.getReceivedFriendInvitations(id, withDetails, requester));
    }

    @GetMapping("/id/{id}/sent-friend-invitations")
    public ResponseEntity<List<FriendInvitationDto>> getSentFriendInvitations(
            @PathVariable Long id,
            @RequestParam(required = false) Boolean withDetails,
            final HttpServletRequest request
    ) {
        User requester = authenticationService.getUserFromRequest(request);
        return ResponseEntity.ok(userService.getSentFriendInvitations(id, withDetails, requester));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<User> getUserById(
            @PathVariable Long id,
            final HttpServletRequest request
    ) {
        User requester = authenticationService.getUserFromRequest(request);
        return ResponseEntity.ok(userService.getUserById(id, requester));
    }
    @GetMapping("/username/{username}")
    public ResponseEntity<User> findByUsername(
            @PathVariable String username,
            final HttpServletRequest request
    ) {
        User requester = authenticationService.getUserFromRequest(request);
        return ResponseEntity.ok(userService.getUserByUsername(username, requester));
    }

    @PatchMapping("/id/{id}")
    public ResponseEntity<Object> updateUser(
            @PathVariable Long id,
            @RequestBody Map<String, Object> fields,
            final HttpServletRequest request
    ) {
        User requester = authenticationService.getUserFromRequest(request);
        userService.updateUser(id, fields, requester);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<Object> deleteUser(
            @PathVariable Long id,
            final HttpServletRequest request
    ) {
        User requester = authenticationService.getUserFromRequest(request);
        userService.deleteUser(id, requester);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/password")
    public ResponseEntity<Object> updatePassword(
            @RequestBody UpdatePasswordReqDto updatePasswordRequest,
            final HttpServletRequest request
    ) {
        User requester = authenticationService.getUserFromRequest(request);
        userService.updatePassword(updatePasswordRequest, requester);
        return ResponseEntity.noContent().build();
    }
}
