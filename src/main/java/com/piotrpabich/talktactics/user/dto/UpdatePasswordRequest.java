package com.piotrpabich.talktactics.user.dto;

import jakarta.validation.constraints.NotNull;

public record UpdatePasswordRequest(
        @NotNull
        Long id,
        @NotNull
        String oldPassword,
        @NotNull
        String newPassword,
        @NotNull
        String repeatNewPassword
) { }
