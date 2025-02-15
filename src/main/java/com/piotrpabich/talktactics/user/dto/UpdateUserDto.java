package com.piotrpabich.talktactics.user.dto;

public record UpdateUserDto(
        String username,
        String firstName,
        String lastName,
        String email,
        String bio
) {
}
