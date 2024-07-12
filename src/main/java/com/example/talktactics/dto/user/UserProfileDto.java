package com.example.talktactics.dto.user;

import com.example.talktactics.dto.user_course.UserCourseDto;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record UserProfileDto(
        Long id,
        @JsonProperty("first_name")
        String firstName,
        @JsonProperty("last_name")
        String lastName,
        @JsonProperty("total_points")
        Integer totalPoints,
        String bio,
        List<UserCourseDto> courses
){
    public static UserProfileDto toUserProfileDto(UserProfilePreviewDto userProfile, List<UserCourseDto> userCourses) {
        return new UserProfileDto(
                userProfile.id(),
                userProfile.firstName(),
                userProfile.lastName(),
                userProfile.totalPoints(),
                userProfile.bio(),
                userCourses
        );
    }
}
