package com.example.talktactics.dto.auth.res;

import com.example.talktactics.entity.Role;
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
