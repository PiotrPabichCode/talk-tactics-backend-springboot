package com.piotrpabich.talktactics.user.dto;

public record UpdateUserRequest(
        String username,
        String firstName,
        String lastName,
        String email,
        String bio
) {
}
