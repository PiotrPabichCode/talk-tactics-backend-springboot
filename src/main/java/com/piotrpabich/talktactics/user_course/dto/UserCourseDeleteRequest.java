package com.piotrpabich.talktactics.user_course.dto;

import jakarta.validation.constraints.NotNull;

public record UserCourseDeleteRequest(
        @NotNull
        Long courseId,
        @NotNull
        Long userId
) {
}
