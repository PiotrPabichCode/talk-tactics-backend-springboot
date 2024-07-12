package com.example.talktactics.dto.user;

import com.example.talktactics.entity.Role;
import com.example.talktactics.entity.User;

public record UserDto(
        Long id,
        String username,
        String firstName,
        String lastName,
        String email,
        String bio,
        Integer totalPoints,
        Role role
) {
    public static UserDto from(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getBio(),
                user.getTotalPoints(),
                user.getRole()
        );
    }
}
