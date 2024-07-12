package com.example.talktactics.dto.auth.req;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RefreshTokenRequest(
        String username,
        @JsonProperty("refresh_token")
        String refreshToken) {
}
