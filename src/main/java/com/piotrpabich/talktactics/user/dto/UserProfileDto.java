package com.piotrpabich.talktactics.user.dto;

import com.piotrpabich.talktactics.course.participant.dto.CourseParticipantDto;
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
        List<CourseParticipantDto> courses
){
    public static UserProfileDto of(
        final UserProfilePreviewDto userProfile,
        final List<CourseParticipantDto> courseParticipants
    ) {
        return new UserProfileDto(
            userProfile.uuid(),
            userProfile.firstName(),
            userProfile.lastName(),
            userProfile.totalPoints(),
            userProfile.bio(),
            courseParticipants
        );
    }
}
