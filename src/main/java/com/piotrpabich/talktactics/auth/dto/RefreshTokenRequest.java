package com.piotrpabich.talktactics.auth.dto;

public record RefreshTokenRequest(
        String username,
        String refreshToken) {
}
