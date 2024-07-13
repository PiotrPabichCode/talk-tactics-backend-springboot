package com.piotrpabich.talktactics.dto.user;

import com.piotrpabich.talktactics.entity.Role;
import com.piotrpabich.talktactics.entity.User;

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
