package com.piotrpabich.talktactics.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserProfilePreviewDto(
        @JsonProperty("id")
        long id,
        @JsonProperty("first_name")
        String firstName,
        @JsonProperty("last_name")
        String lastName,
        @JsonProperty("total_points")
        int totalPoints,
        String bio
) {
}
