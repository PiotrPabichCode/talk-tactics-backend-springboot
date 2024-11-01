package com.piotrpabich.talktactics.user.dto;

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
