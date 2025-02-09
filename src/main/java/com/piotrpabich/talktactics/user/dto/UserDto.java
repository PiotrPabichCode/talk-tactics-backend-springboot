package com.piotrpabich.talktactics.user.dto;

import com.piotrpabich.talktactics.user.entity.Role;
import com.piotrpabich.talktactics.user.entity.User;

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
    public static UserDto from(final User user) {
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
