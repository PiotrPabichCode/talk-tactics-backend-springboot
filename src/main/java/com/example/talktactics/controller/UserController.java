package com.example.talktactics.controller;

import com.example.talktactics.dto.user.req.UpdatePasswordReqDto;
import com.example.talktactics.entity.*;
import com.example.talktactics.exception.UserRuntimeException;
import com.example.talktactics.service.user.UserServiceImpl;
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
    private final UserServiceImpl userServiceImpl;

    @PostMapping("/create")
    public ResponseEntity<User> create(@RequestBody User user) {
        try {
            return ResponseEntity.ok(userServiceImpl.createUser(user));
        } catch(UserRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getUsers() {
        try {
            return ResponseEntity.ok(userServiceImpl.getUsers());
        } catch(UserRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(userServiceImpl.getUserById(id));
        } catch (UserRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
    @GetMapping("/username/{username}")
    public ResponseEntity<User> findByUsername(@PathVariable String username) {
        try {
            return ResponseEntity.ok(userServiceImpl.getUserByUsername(username));
        } catch (UserRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PatchMapping("/id/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody Map<String, Object> fields) {
        try {
            return ResponseEntity.ok(userServiceImpl.updateUser(id, fields));
        } catch (UserRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/id/{id}")
    public void deleteUser(@PathVariable Long id) {
        try {
            userServiceImpl.deleteUser(id);
        } catch(UserRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/password")
    public ResponseEntity<User> updatePassword(@RequestBody UpdatePasswordReqDto request) {
        try {
            return ResponseEntity.ok(userServiceImpl.updatePassword(request));
        } catch (UserRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }
}
