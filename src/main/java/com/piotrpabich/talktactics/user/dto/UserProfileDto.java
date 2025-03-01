package com.piotrpabich.talktactics.user.dto;

import com.piotrpabich.talktactics.user_course.dto.UserCourseDto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record UserProfileDto(
        @NotNull
        UUID uuid,
        @NotNull
        String firstName,
        @NotNull
        String lastName,
        @Min(0)
        Integer totalPoints,
        @NotNull
        String bio,
        @NotNull
        List<UserCourseDto> courses
){
    public static UserProfileDto of(
        final UserProfilePreviewDto userProfile,
        final List<UserCourseDto> userCourses
    ) {
        return new UserProfileDto(
            userProfile.uuid(),
            userProfile.firstName(),
            userProfile.lastName(),
            userProfile.totalPoints(),
            userProfile.bio(),
            userCourses
        );
    }
}
