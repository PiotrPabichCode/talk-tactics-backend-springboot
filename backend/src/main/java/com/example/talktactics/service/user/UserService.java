package com.example.talktactics.service.user;

import com.example.talktactics.dto.user.UpdatePasswordDto;
import com.example.talktactics.dto.user.UpdateUserDto;
import com.example.talktactics.exception.UserNotFoundException;
import com.example.talktactics.entity.*;
import com.example.talktactics.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;

import static com.example.talktactics.util.EmailValidator.isValidEmail;
import static com.example.talktactics.util.Utils.getJsonPropertyFieldMap;
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
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

//    Admin panel - CRUD
    public void deleteUser(Long id) {
        if(!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
    }

    public User getUserByUsernameAndValidateCredentials(String username) {
        User user = getUserByUsername(username).orElseThrow(() -> new UserNotFoundException("User %s not found".formatted(username)));;
        validateCredentials(user);
        return user;
    }

    public User updateUser(Long id, Map<String, Object> fields) {
        System.out.println(fields);
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        validateCredentials(user);
        validateFields(fields);

        Map<String, String> fieldMap = getJsonPropertyFieldMap(UpdateUserDto.class);

        fields.forEach((key, value) -> {
            String fieldName = fieldMap.get(key);
            if (fieldName == null) {
                throw new IllegalStateException("Illegal fields given: " + key);
            }
            Field field = ReflectionUtils.findField(User.class, fieldName);
            if (field == null) {
                throw new IllegalStateException("Field not found: " + fieldName);
            }
            field.setAccessible(true);
            ReflectionUtils.setField(field, user, value);
        });

        return userRepository.save(user);
    }

    private void validateCredentials(User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        if(user.getRole() != Role.ADMIN && !user.getUsername().equals(username)) {
            throw new IllegalStateException("Not enough authorities");
        }
    }

    public void validateFields(Map<String, Object> fields) {
        if(fields.containsKey("email")) {
            String email = (String) fields.get("email");
            if(!isValidEmail(email)) {
                throw new IllegalArgumentException("Email has forbidden characters");
            }
        }
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
