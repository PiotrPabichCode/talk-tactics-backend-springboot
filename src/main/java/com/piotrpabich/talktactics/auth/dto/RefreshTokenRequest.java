package com.piotrpabich.talktactics.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(
        @NotBlank
        String username,
        @NotBlank
        String refreshToken
) {
}
