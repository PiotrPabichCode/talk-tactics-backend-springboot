package com.example.talktactics.controller;

import com.example.talktactics.dto.user.UserProfileDto;
import com.example.talktactics.dto.user.UserProfilePreviewDto;
import com.example.talktactics.dto.user.req.DeleteFriendDto;
import com.example.talktactics.dto.user.req.FriendInvitationRequest;
import com.example.talktactics.dto.user.req.FriendRequestDto;
import com.example.talktactics.dto.user.req.UpdatePasswordReqDto;
import com.example.talktactics.dto.user.res.FriendInvitationDto;
import com.example.talktactics.entity.*;
import com.example.talktactics.exception.UserRuntimeException;
import com.example.talktactics.service.user.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = {"http://localhost:3000", "https://talk-tactics-frontend.vercel.app/"}, allowCredentials = "true")
@Tag(name = "Users", description = "Users management APIs")
public class UserController {
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<User> create(@RequestBody User user) {
        try {
            return ResponseEntity.ok(userService.createUser(user));
        } catch(UserRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getUsers() {
        try {
            return ResponseEntity.ok(userService.getUsers());
        } catch(UserRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/profiles")
    public ResponseEntity<List<UserProfilePreviewDto>> getUserProfiles() {
        try {
            return ResponseEntity.ok(userService.getUserProfiles());
        } catch(UserRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/profiles/{id}")
    public ResponseEntity<UserProfileDto> getUserProfileById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(userService.getUserProfileById(id));
        } catch(UserRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/id/{id}/friends")
    public ResponseEntity<List<UserProfilePreviewDto>> getFriends(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(userService.getFriends(id));
        } catch (UserRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/send-friend-invitation")
    public void sendFriendInvitation(@RequestBody FriendInvitationRequest request) {
        try {
            userService.sendFriendInvitation(request);
        } catch (UserRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/accept-friend-invitation")
    public void acceptFriendInvitation(@RequestBody FriendInvitationRequest request) {
        try {
            userService.acceptFriendInvitation(request);
        } catch (UserRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/reject-friend-invitation")
    public void rejectFriendInvitation(@RequestBody FriendInvitationRequest request) {
        try {
            userService.rejectFriendInvitation(request);
        } catch (UserRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/delete-friend")
    public void deleteFriend(@RequestBody DeleteFriendDto request) {
        try {
            userService.deleteFriend(request);
        } catch (UserRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/id/{id}/received-friend-invitations")
    public ResponseEntity<List<FriendInvitationDto>> getReceivedFriendInvitations(
            @PathVariable Long id,
            @RequestParam(required = false) Boolean withDetails
    ) {
        try {
            return ResponseEntity.ok(userService.getReceivedFriendInvitations(id, withDetails));
        } catch (UserRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/id/{id}/sent-friend-invitations")
    public ResponseEntity<List<FriendInvitationDto>> getSentFriendInvitations(
            @PathVariable Long id,
            @RequestParam(required = false) Boolean withDetails
    ) {
        try {
            return ResponseEntity.ok(userService.getSentFriendInvitations(id, withDetails));
        } catch (UserRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
    @DeleteMapping("/delete-sent-friend-invitation")
    public void deleteSentFriendInvitation(@RequestBody FriendInvitationRequest request) {
        try {
            userService.deleteSentFriendInvitation(request);
        } catch (UserRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(userService.getUserById(id));
        } catch (UserRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
    @GetMapping("/username/{username}")
    public ResponseEntity<User> findByUsername(@PathVariable String username) {
        try {
            return ResponseEntity.ok(userService.getUserByUsername(username));
        } catch (UserRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PatchMapping("/id/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody Map<String, Object> fields) {
        try {
            return ResponseEntity.ok(userService.updateUser(id, fields));
        } catch (UserRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/id/{id}")
    public void deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
        } catch(UserRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/password")
    public ResponseEntity<User> updatePassword(@RequestBody UpdatePasswordReqDto request) {
        try {
            return ResponseEntity.ok(userService.updatePassword(request));
        } catch (UserRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }
}
