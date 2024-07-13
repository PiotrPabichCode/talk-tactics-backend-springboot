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
import com.piotrpabich.talktactics.service.user.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    @GetMapping("/all")
    public ResponseEntity<PageResult<UserDto>> queryUsers(UserQueryCriteria criteria, Pageable pageable) {
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
    public ResponseEntity<List<UserProfilePreviewDto>> getFriends(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getFriends(id));
    }

    @PostMapping("/friend-invitation")
    public ResponseEntity<Object> handleFriendInvitation(@RequestBody FriendInvitationRequest request) {
        userService.handleFriendInvitationRequest(request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete-friend")
    public ResponseEntity<Object> deleteFriend(@RequestBody DeleteFriendDto request) {
        userService.deleteFriend(request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/id/{id}/received-friend-invitations")
    public ResponseEntity<List<FriendInvitationDto>> getReceivedFriendInvitations(
            @PathVariable Long id,
            @RequestParam(required = false) Boolean withDetails
    ) {
        return ResponseEntity.ok(userService.getReceivedFriendInvitations(id, withDetails));
    }

    @GetMapping("/id/{id}/sent-friend-invitations")
    public ResponseEntity<List<FriendInvitationDto>> getSentFriendInvitations(
            @PathVariable Long id,
            @RequestParam(required = false) Boolean withDetails
    ) {
        return ResponseEntity.ok(userService.getSentFriendInvitations(id, withDetails));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }
    @GetMapping("/username/{username}")
    public ResponseEntity<User> findByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    @PatchMapping("/id/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable Long id, @RequestBody Map<String, Object> fields) {
        userService.updateUser(id, fields);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/password")
    public ResponseEntity<Object> updatePassword(@RequestBody UpdatePasswordReqDto request) {
        userService.updatePassword(request);
        return ResponseEntity.noContent().build();
    }
}
