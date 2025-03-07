package com.piotrpabich.talktactics.auth.dto;

import com.piotrpabich.talktactics.user.entity.Role;
import com.piotrpabich.talktactics.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AuthenticationResponse(
        @NotNull
        UUID uuid,
        @NotBlank
        String username,
        @NotNull
        Role role,
        @NotBlank
        String token,
        @NotBlank
        String refreshToken
) {

    public static AuthenticationResponse of(final User user, final String token, final String refreshToken) {
        return new AuthenticationResponse(
                user.getUuid(),
                user.getUsername(),
                user.getRole(),
                token,
                refreshToken
        );
    }
}
