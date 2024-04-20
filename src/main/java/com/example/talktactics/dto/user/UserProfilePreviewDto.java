package com.example.talktactics.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfilePreviewDto {
    long id;
    @JsonProperty("full_name")
    String fullName;
    @JsonProperty("total_points")
    int totalPoints;
    String bio;
}
