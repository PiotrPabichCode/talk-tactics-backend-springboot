package com.piotrpabich.talktactics.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RefreshTokenRequest(
        String username,
        @JsonProperty("refresh_token")
        String refreshToken) {
}
