package com.piotrpabich.talktactics.auth.dto;

import com.piotrpabich.talktactics.auth.PasswordMatches;
import jakarta.validation.constraints.Email;

import jakarta.validation.constraints.Size;

@PasswordMatches
public record RegisterRequest(
        @Size(min = 3, max = 20)
        String username,
        @Size(min = 8, max = 100)
        String password,
        @Size(min = 8, max = 100)
        String repeatPassword,
        @Size(min = 3, max = 50)
        String firstName,
        @Size(min = 3, max = 50)
        String lastName,
        @Email
        String email
) {
}

