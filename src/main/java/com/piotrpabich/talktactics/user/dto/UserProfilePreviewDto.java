package com.piotrpabich.talktactics.user.dto;

import jakarta.validation.constraints.NotNull;

public record UserProfilePreviewDto(
        @NotNull
        Long id,
        @NotNull
        String firstName,
        @NotNull
        String lastName,
        Integer totalPoints,
        String bio
) {
}
