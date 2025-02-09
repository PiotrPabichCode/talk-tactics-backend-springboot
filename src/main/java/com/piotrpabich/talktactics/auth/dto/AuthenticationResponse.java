package com.piotrpabich.talktactics.auth.dto;

import com.piotrpabich.talktactics.user.entity.Role;
import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthenticationResponse(
        Long id,
        String username,
        Role role,
        String token,
        @JsonProperty("refresh_token")
        String refreshToken
) {
}
