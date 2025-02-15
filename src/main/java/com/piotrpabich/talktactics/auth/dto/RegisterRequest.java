package com.piotrpabich.talktactics.auth.dto;

public record RegisterRequest(
        String username,
        String password,
        String repeatPassword,
        String firstName,
        String lastName,
        String email
) {
}
