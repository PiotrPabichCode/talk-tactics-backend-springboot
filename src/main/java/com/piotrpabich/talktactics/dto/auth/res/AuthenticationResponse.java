package com.piotrpabich.talktactics.dto.auth.res;

import com.piotrpabich.talktactics.entity.Role;
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
