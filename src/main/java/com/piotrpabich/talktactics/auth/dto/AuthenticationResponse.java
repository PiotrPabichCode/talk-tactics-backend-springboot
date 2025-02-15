package com.piotrpabich.talktactics.auth.dto;

import com.piotrpabich.talktactics.user.entity.Role;

public record AuthenticationResponse(
        Long id,
        String username,
        Role role,
        String token,
        String refreshToken
) {
}
