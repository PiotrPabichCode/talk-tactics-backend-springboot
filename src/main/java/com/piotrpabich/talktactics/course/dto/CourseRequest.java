package com.piotrpabich.talktactics.course.dto;

import com.piotrpabich.talktactics.course.entity.CourseLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CourseRequest(
        @NotBlank
        String title,
        @NotBlank
        String description,
        @NotNull
        CourseLevel level
) {
}
