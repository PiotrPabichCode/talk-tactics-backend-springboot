package com.piotrpabich.talktactics.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthenticationRequest(
        @NotBlank
        String username,
        @Size(min = 8, max = 100)
        String password
) {
}
