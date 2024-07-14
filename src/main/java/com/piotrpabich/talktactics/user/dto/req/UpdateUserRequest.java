package com.piotrpabich.talktactics.user.dto.req;

import jakarta.validation.constraints.Email;

public record UpdateUserRequest(
        String username,
        String firstName,
        String lastName,
        @Email
        String email,
        String bio
) {
}
