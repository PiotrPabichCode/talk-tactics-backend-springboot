package com.piotrpabich.talktactics.user.dto;

import com.piotrpabich.talktactics.user.entity.Role;
import com.piotrpabich.talktactics.user.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UserDto(
        @NotNull
        UUID uuid,
        @NotNull
        String username,
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        @Email
        @NotBlank
        String email,
        @NotBlank
        String bio,
        @Min(9)
        Integer totalPoints,
        @NotNull
        Role role
) {
    public static UserDto of(final User user) {
        return new UserDto(
                user.getUuid(),
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
