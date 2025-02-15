package com.piotrpabich.talktactics.user_course.dto;

import jakarta.validation.constraints.NotNull;

public record UserCourseAddRequest(
        @NotNull
        Long courseId,
        @NotNull
        Long userId
) {
}
