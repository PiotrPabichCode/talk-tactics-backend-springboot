package com.example.talktactics.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateUserDto(
        String username,
        @JsonProperty("first_name")
        String firstName,
        @JsonProperty("last_name")
        String lastName,
        String email,
        String bio
) {
}
