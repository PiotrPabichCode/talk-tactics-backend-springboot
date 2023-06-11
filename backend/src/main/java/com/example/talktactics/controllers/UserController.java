package com.example.talktactics.controllers;

import com.example.talktactics.DTOs.UpdatePassword;
import com.example.talktactics.DTOs.UpdateUser;
import com.example.talktactics.models.*;
import com.example.talktactics.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    @PostMapping("/users")
    User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping("/users")
    List<User> getAllUsers() {
        return userService.getUsers();
    }
    @GetMapping("/users/login/{username}")
    public List<User> filterUsersByUsername(@PathVariable String username) {
        return userService.filterUsersByUsername(username);
    }

    @GetMapping("/users/{id}")
    User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }
    @GetMapping("/users/by/login/{login}")
    public Optional<User> findByLogin(@PathVariable String login) {
        return userService.getUserByLogin(login);
    }

//    Admin panel
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update_user")
    User updateUser(@RequestBody UpdateUser updateUser) {
        return userService.updateUser(updateUser);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/users/{id}")
    void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    // User editing details
    @PutMapping("/users/{userID}/firstName")
    public User updateFirstName(@PathVariable Long userID, @RequestBody String firstName) {
        return userService.updateFirstName(userID, firstName);
    }

    @PutMapping("/users/{userID}/lastName")
    public User updateLastName(@PathVariable Long userID, @RequestBody String lastName) {
        return userService.updateLastName(userID, lastName);
    }

    @PutMapping("/users/{userID}/email")
    public User updateEmail(@PathVariable Long userID, @RequestBody String email) {
        return userService.updateEmail(userID, email);
    }

    @PutMapping("/users/{userID}/password")
    public User updatePassword(@PathVariable Long userID, @RequestBody UpdatePassword updatePassword) {
        return userService.updatePassword(userID, updatePassword);
    }
}
