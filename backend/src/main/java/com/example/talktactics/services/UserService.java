package com.example.talktactics.services;

import com.example.talktactics.exceptions.UserNotFoundException;
import com.example.talktactics.models.UpdatePassword;
import com.example.talktactics.models.User;
import com.example.talktactics.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.example.talktactics.utils.EmailValidator.isValidEmail;
import static com.example.talktactics.utils.Utils.isEmptyString;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public User updateFirstName(Long id, String firstName) {
        if(isEmptyString(firstName)) {
            throw new IllegalArgumentException("First name cannot be empty");
        }
        User user = repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        user.setFirstName(firstName);
        return repository.save(user);
    }

    public User updateLastName(Long id, String lastName) {
        if(isEmptyString(lastName)) {
            throw new IllegalArgumentException("Last name cannot be empty");
        }
        User user = repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        user.setLastName(lastName);
        return repository.save(user);
    }

    public User updateEmail(Long id, String email) {
        if(isEmptyString(email)) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if(!isValidEmail(email)) {
            throw new IllegalArgumentException("Email has forbidden characters");
        }
        User user = repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        user.setLastName(email);
        return repository.save(user);
    }

    private boolean validatePassword(UpdatePassword updatePassword) {
        return passwordEncoder.matches(updatePassword.getOldPassword(), updatePassword.getCurrentPassword()) &&
                updatePassword.getNewPassword().equals(updatePassword.getRepeatNewPassword());
    }

    public User updatePassword(Long id, UpdatePassword updatePassword) {
        if((isEmptyString(updatePassword.getOldPassword())) || (isEmptyString(updatePassword.getNewPassword()) || (isEmptyString(updatePassword.getRepeatNewPassword())))) {
            throw new IllegalArgumentException("All fields must be entered");
        }
        User user = repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        updatePassword.setCurrentPassword(user.getPassword());
        if(!validatePassword(updatePassword)) {
            throw new IllegalArgumentException("Invalid password");
        }
        user.setPassword(passwordEncoder.encode(updatePassword.getNewPassword()));
        return repository.save(user);
    }
}
