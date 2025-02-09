package com.piotrpabich.talktactics.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RegisterRequest(
        String username,
        String password,
        @JsonProperty("repeat_password")
        String repeatPassword,
        @JsonProperty("first_name")
        String firstName,
        @JsonProperty("last_name")
        String lastName,
        String email
) {
}
