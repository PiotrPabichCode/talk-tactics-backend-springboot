package com.example.talktactics.controller;

import com.example.talktactics.dto.user.UpdatePasswordDto;
import com.example.talktactics.dto.user.UpdateUserDto;
import com.example.talktactics.entity.*;
import com.example.talktactics.service.user.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name = "Users", description = "Users management APIs")
public class UserController {
    private final UserService userService;

    @PostMapping()
    public ResponseEntity<User> create(@RequestBody User user) {
        return ResponseEntity.ok(userService.createUser(user));
    }

    @GetMapping("/{id}")
    User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }
    @GetMapping("/username/{username}")
    public Optional<User> findByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

//    Admin panel
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update_user")
    public ResponseEntity<User> updateUser(@RequestBody UpdateUserDto updateUserDto) {
        return ResponseEntity.ok(userService.updateUser(updateUserDto));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    // User editing details
    @PutMapping("/{userID}/firstName")
    public User updateFirstName(@PathVariable Long userID, @RequestBody String firstName) {
        return userService.updateFirstName(userID, firstName);
    }

    @PutMapping("/{userID}/lastName")
    public User updateLastName(@PathVariable Long userID, @RequestBody String lastName) {
        return userService.updateLastName(userID, lastName);
    }

    @PutMapping("/{userID}/email")
    public User updateEmail(@PathVariable Long userID, @RequestBody String email) {
        return userService.updateEmail(userID, email);
    }

    @PutMapping("/{userID}/password")
    public User updatePassword(@PathVariable Long userID, @RequestBody UpdatePasswordDto updatePasswordDto) {
        return userService.updatePassword(userID, updatePasswordDto);
    }
}
