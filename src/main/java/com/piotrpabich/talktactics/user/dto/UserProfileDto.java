package com.piotrpabich.talktactics.user.dto;

import com.piotrpabich.talktactics.user_course.dto.UserCourseDto;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UserProfileDto(
        @NotNull
        Long id,
        @NotNull
        String firstName,
        @NotNull
        String lastName,
        Integer totalPoints,
        String bio,
        List<UserCourseDto> courses
){
    public static UserProfileDto toUserProfileDto(
            final UserProfilePreviewDto userProfile,
            final List<UserCourseDto> userCourses
    ) {
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
