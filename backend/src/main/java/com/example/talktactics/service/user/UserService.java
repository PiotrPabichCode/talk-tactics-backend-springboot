package com.example.talktactics.service.user;

import com.example.talktactics.dto.user.UpdatePasswordDto;
import com.example.talktactics.dto.user.UpdateUserDto;
import com.example.talktactics.exception.UserNotFoundException;
import com.example.talktactics.entity.*;
import com.example.talktactics.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.example.talktactics.util.EmailValidator.isValidEmail;
import static com.example.talktactics.util.Utils.isEmptyString;

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

    public User updateUser(UpdateUserDto updateUserDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User user = userRepository.findById(updateUserDto.getId())
                .orElseThrow(() -> new UserNotFoundException(updateUserDto.getId()));

        validateCurrentUser(currentUsername, updateUserDto, authentication);
        validateRoleChange(user, updateUserDto);
        validateUpdateUserFields(updateUserDto);
        updateUserData(user, updateUserDto);

        return userRepository.save(user);
    }

    private void validateCurrentUser(String currentUsername, UpdateUserDto updateUserDto, Authentication authentication) {
        if (!currentUsername.equalsIgnoreCase(updateUserDto.getLogin()) &&
                authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .anyMatch(role -> role.equalsIgnoreCase(Role.USER.toString()))) {
            throw new IllegalStateException("Not enough authorities");
        }
    }

    private void validateRoleChange(User user, UpdateUserDto updateUserDto) {
        if (user.getRole() == Role.ADMIN && updateUserDto.getRole() == Role.USER) {
            throw new IllegalStateException("Cannot downgrade admin user to regular user.");
        }
    }

    private void validateUpdateUserFields(UpdateUserDto updateUserDto) {
        if (isEmptyString(updateUserDto.getLogin()) || isEmptyString(updateUserDto.getEmail()) ||
                isEmptyString(updateUserDto.getFirstName()) || isEmptyString(updateUserDto.getLastName())) {
            throw new RuntimeException("Fields cannot be empty");
        }
    }

    private void updateUserData(User user, UpdateUserDto updateUserDto) {
        user.setLogin(updateUserDto.getLogin());
        user.setRole(updateUserDto.getRole());
        user.setEmail(updateUserDto.getEmail());
        user.setFirstName(updateUserDto.getFirstName());
        user.setLastName(updateUserDto.getLastName());
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

    private boolean validatePassword(UpdatePasswordDto updatePasswordDto) {
        return passwordEncoder.matches(updatePasswordDto.getOldPassword(), updatePasswordDto.getCurrentPassword()) &&
                updatePasswordDto.getNewPassword().equals(updatePasswordDto.getRepeatNewPassword());
    }

    public User updatePassword(Long id, UpdatePasswordDto updatePasswordDto) {
        if((isEmptyString(updatePasswordDto.getOldPassword())) || (isEmptyString(updatePasswordDto.getNewPassword()) || (isEmptyString(updatePasswordDto.getRepeatNewPassword())))) {
            throw new IllegalArgumentException("All fields must be entered");
        }
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        updatePasswordDto.setCurrentPassword(user.getPassword());
        if(!validatePassword(updatePasswordDto)) {
            throw new IllegalArgumentException("Invalid password");
        }
        user.setPassword(passwordEncoder.encode(updatePasswordDto.getNewPassword()));
        return userRepository.save(user);
    }

}
