package com.piotrpabich.talktactics.user.dto;

import com.piotrpabich.talktactics.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UserProfilePreviewDto(
        @NotNull
        UUID uuid,
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        @NotNull
        Integer totalPoints,
        @NotBlank
        String bio
) {

    public static UserProfilePreviewDto of(final User user) {
        return new UserProfilePreviewDto(
                user.getUuid(),
                user.getFirstName(),
                user.getLastName(),
                user.getTotalPoints(),
                user.getBio()
        );
    }
}
