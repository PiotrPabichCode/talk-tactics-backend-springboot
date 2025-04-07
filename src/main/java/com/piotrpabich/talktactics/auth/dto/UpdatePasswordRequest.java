package com.piotrpabich.talktactics.auth.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record UpdatePasswordRequest(
        @NotNull
        UUID userUuid,
        @Size(min = 8, max = 100)
        String oldPassword,
        @Size(min = 8, max = 100)
        String newPassword,
        @Size(min = 8, max = 100)
        String repeatNewPassword
) {
}
