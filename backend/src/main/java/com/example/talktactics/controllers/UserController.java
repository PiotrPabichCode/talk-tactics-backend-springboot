package com.example.talktactics.controllers;

import com.example.talktactics.exceptions.AnswerNotFoundException;
import com.example.talktactics.exceptions.UserNotFoundException;
import com.example.talktactics.models.*;
import com.example.talktactics.repositories.AnswerRepository;
import com.example.talktactics.repositories.UserRepository;
import org.hibernate.sql.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update_user")
    User updateUser(@RequestBody UpdateUser updateUser) {
        User user = userRepository.findById(updateUser.getId())
                .orElseThrow(() -> new UserNotFoundException(updateUser.getId()));
        if(user.getRole() == Role.ADMIN && updateUser.getRole() == Role.USER) {
            throw new IllegalStateException("Cannot downgrade admin user to regular user.");
        }
        System.out.println(updateUser);
        user.setLogin(updateUser.getLogin());
        user.setRole(updateUser.getRole());
        user.setEmail(updateUser.getEmail());
        user.setFirstName(updateUser.getFirstName());
        user.setLastName(updateUser.getLastName());
        updatePassword(user, updateUser);

        return userRepository.save(user);
    }

    private void updatePassword(User user, UpdateUser updateUser) {
        if(updateUser.getPassword() != null && updateUser.getOldPassword() != null && updateUser.getRepeatPassword() != null) {
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
}
