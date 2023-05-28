package com.example.talktactics.controllers;

import com.example.talktactics.auth.RegisterRequest;
import com.example.talktactics.exceptions.AnswerNotFoundException;
import com.example.talktactics.exceptions.UserNotFoundException;
import com.example.talktactics.models.*;
import com.example.talktactics.repositories.AnswerRepository;
import com.example.talktactics.repositories.UserRepository;
import com.example.talktactics.services.UserService;
import org.hibernate.sql.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @PostMapping("/user")
    User newUser(@RequestBody User newUser) {
        return userRepository.save(newUser);
    }

    @GetMapping("/users")
    List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/user/{id}")
    User getUserById(@PathVariable Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
//        user.setPassword(passwordEncoder.);
        return user;
    }

    @PutMapping("/update_user")
    User updateUser(@RequestBody UpdateUser updateUser) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User user = userRepository.findById(updateUser.getId())
                .orElseThrow(() -> new UserNotFoundException(updateUser.getId()));
        if(!currentUsername.equals(updateUser.getLogin())
                && authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).anyMatch(role -> role.equals(Role.USER.toString()))) {
            throw new IllegalStateException("Not enough authorities");
        }
        if(user.getRole() == Role.ADMIN && updateUser.getRole() == Role.USER) {
            throw new IllegalStateException("Cannot downgrade admin user to regular user.");
        }
        validateUpdateUser(updateUser);
        System.out.println(updateUser);
        user.setLogin(updateUser.getLogin());
        user.setRole(updateUser.getRole());
        user.setEmail(updateUser.getEmail());
        user.setFirstName(updateUser.getFirstName());
        user.setLastName(updateUser.getLastName());
        user.setPassword(passwordEncoder.encode(updateUser.getPassword()));
        updatePassword(user, updateUser);

        return userRepository.save(user);
    }

    private boolean isEmpty(String value) {
        return value == null || value.isEmpty();
    }
    private void validateUpdateUser(UpdateUser request) {
        if(isEmpty(request.getLogin()) || isEmpty(request.getPassword()) || isEmpty(request.getRepeatPassword())
                || isEmpty(request.getEmail()) || isEmpty(request.getFirstName()) || isEmpty(request.getLastName())) {
            throw new RuntimeException("Fields cannot be empty");
        }
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already used");
        }
        if(!request.getPassword().equals(request.getRepeatPassword())) {
            throw new RuntimeException("Passwords must match each other");
        }
    }

    private void updatePassword(User user, UpdateUser updateUser) {
        if(isEmpty(updateUser.getPassword()) && isEmpty(updateUser.getOldPassword()) && isEmpty(updateUser.getRepeatPassword())) {
            if(updateUser.getOldPassword().equals(user.getPassword()) && updateUser.getPassword().equals(updateUser.getRepeatPassword())) {
                user.setPassword(passwordEncoder.encode(updateUser.getPassword()));
            }
        }
    }



    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/user/{id}")
    String deleteUser(@PathVariable Long id) {
        if(!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
        return "User with id " + id + " deleted.";
    }

    @GetMapping("/users/by/login/{login}")
    public Optional<User> findByLogin(@PathVariable String login) {
        return userRepository.findByLogin(login);
    }

    @GetMapping("/users/login/{userName}")
    public List<User> getUsersByUserName(@PathVariable String userName) {
        return userRepository.findByUserNameContaining(userName);
    }

    // User editing details
    @PutMapping("/user/{userID}/firstName")
    public User updateFirstName(@PathVariable Long userID, @RequestBody String firstName) {
        return userService.updateFirstName(userID, firstName);
    }

    @PutMapping("/user/{userID}/lastName")
    public User updateLastName(@PathVariable Long userID, @RequestBody String lastName) {
        return userService.updateLastName(userID, lastName);
    }

    @PutMapping("/user/{userID}/email")
    public User updateEmail(@PathVariable Long userID, @RequestBody String email) {
        return userService.updateEmail(userID, email);
    }

    @PutMapping("/user/{userID}/password")
    public User updatePassword(@PathVariable Long userID, @RequestBody UpdatePassword updatePassword) {
        System.out.println(updatePassword);
        return userService.updatePassword(userID, updatePassword);
    }
}
