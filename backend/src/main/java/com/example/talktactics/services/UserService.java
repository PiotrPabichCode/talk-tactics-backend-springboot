package com.example.talktactics.services;

import com.example.talktactics.exceptions.UserNotFoundException;
import com.example.talktactics.models.Role;
import com.example.talktactics.models.UpdatePassword;
import com.example.talktactics.models.UpdateUser;
import com.example.talktactics.models.User;
import com.example.talktactics.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.talktactics.utils.EmailValidator.isValidEmail;
import static com.example.talktactics.utils.Utils.isEmptyString;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User createUser(User user) {
        return userRepository.save(user);
    }
    public List<User> getUsers() {
        return userRepository.findAll();
    }
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }
    public Optional<User> getUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }
    public List<User> filterUsersByUsername(String username) {
        return userRepository.findByUsername(username);
    }

//    Admin panel - CRUD
    public void deleteUser(Long id) {
        if(!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
    }

    public User updateUser(UpdateUser updateUser) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User user = userRepository.findById(updateUser.getId())
                .orElseThrow(() -> new UserNotFoundException(updateUser.getId()));

        validateCurrentUser(currentUsername, updateUser, authentication);
        validateRoleChange(user, updateUser);
        validateUpdateUserFields(updateUser);
        updateUserData(user, updateUser);
        updatePassword(user, updateUser);

        return userRepository.save(user);
    }

    private void validateCurrentUser(String currentUsername, UpdateUser updateUser, Authentication authentication) {
        if (!currentUsername.equalsIgnoreCase(updateUser.getLogin()) &&
                authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .anyMatch(role -> role.equalsIgnoreCase(Role.USER.toString()))) {
            throw new IllegalStateException("Not enough authorities");
        }
    }

    private void validateRoleChange(User user, UpdateUser updateUser) {
        if (user.getRole() == Role.ADMIN && updateUser.getRole() == Role.USER) {
            throw new IllegalStateException("Cannot downgrade admin user to regular user.");
        }
    }

    private void validateUpdateUserFields(UpdateUser updateUser) {
        if (isEmptyString(updateUser.getLogin()) || isEmptyString(updateUser.getPassword()) ||
                isEmptyString(updateUser.getRepeatPassword()) || isEmptyString(updateUser.getEmail()) ||
                isEmptyString(updateUser.getFirstName()) || isEmptyString(updateUser.getLastName())) {
            throw new RuntimeException("Fields cannot be empty");
        }

        if (userRepository.existsByEmail(updateUser.getEmail())) {
            throw new RuntimeException("Email already used");
        }

        if (!updateUser.getPassword().equals(updateUser.getRepeatPassword())) {
            throw new RuntimeException("Passwords must match each other");
        }
    }

    private void updateUserData(User user, UpdateUser updateUser) {
        user.setLogin(updateUser.getLogin());
        user.setRole(updateUser.getRole());
        user.setEmail(updateUser.getEmail());
        user.setFirstName(updateUser.getFirstName());
        user.setLastName(updateUser.getLastName());
        user.setPassword(passwordEncoder.encode(updateUser.getPassword()));
    }

    private void updatePassword(User user, UpdateUser updateUser) {
        if (!isEmptyString(updateUser.getOldPassword()) && !isEmptyString(updateUser.getPassword()) &&
                !isEmptyString(updateUser.getRepeatPassword()) &&
                updateUser.getOldPassword().equals(user.getPassword()) &&
                updateUser.getPassword().equals(updateUser.getRepeatPassword())) {
            user.setPassword(passwordEncoder.encode(updateUser.getPassword()));
        }
    }


//    User panel - Update

    public User updateFirstName(Long id, String firstName) {
        if(isEmptyString(firstName)) {
            throw new IllegalArgumentException("First name cannot be empty");
        }
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        user.setFirstName(firstName);
        return userRepository.save(user);
    }

    public User updateLastName(Long id, String lastName) {
        if(isEmptyString(lastName)) {
            throw new IllegalArgumentException("Last name cannot be empty");
        }
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        user.setLastName(lastName);
        return userRepository.save(user);
    }

    public User updateEmail(Long id, String email) {
        if(isEmptyString(email)) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if(!isValidEmail(email)) {
            throw new IllegalArgumentException("Email has forbidden characters");
        }
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        user.setEmail(email);
        return userRepository.save(user);
    }

    private boolean validatePassword(UpdatePassword updatePassword) {
        return passwordEncoder.matches(updatePassword.getOldPassword(), updatePassword.getCurrentPassword()) &&
                updatePassword.getNewPassword().equals(updatePassword.getRepeatNewPassword());
    }

    public User updatePassword(Long id, UpdatePassword updatePassword) {
        if((isEmptyString(updatePassword.getOldPassword())) || (isEmptyString(updatePassword.getNewPassword()) || (isEmptyString(updatePassword.getRepeatNewPassword())))) {
            throw new IllegalArgumentException("All fields must be entered");
        }
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        updatePassword.setCurrentPassword(user.getPassword());
        if(!validatePassword(updatePassword)) {
            throw new IllegalArgumentException("Invalid password");
        }
        user.setPassword(passwordEncoder.encode(updatePassword.getNewPassword()));
        return userRepository.save(user);
    }


}
