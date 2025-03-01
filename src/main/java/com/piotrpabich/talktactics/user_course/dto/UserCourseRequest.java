package com.piotrpabich.talktactics.user_course.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UserCourseRequest(
        @NotNull
        UUID courseUuid,
        @NotNull
        UUID userUuid
) {
}
