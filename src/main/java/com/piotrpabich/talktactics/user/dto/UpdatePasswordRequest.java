package com.piotrpabich.talktactics.user.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UpdatePasswordRequest(
        @NotNull
        UUID userUuid,
        @NotNull
        String oldPassword,
        @NotNull
        String newPassword,
        @NotNull
        String repeatNewPassword
) { }
