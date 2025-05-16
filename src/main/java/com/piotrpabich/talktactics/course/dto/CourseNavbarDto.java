package com.piotrpabich.talktactics.course.dto;

import com.piotrpabich.talktactics.course.entity.Course;
import com.piotrpabich.talktactics.course.entity.CourseLevel;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CourseNavbarDto(
        @NotNull
        UUID uuid,
        @NotBlank
        String title,
        @NotNull
        CourseLevel level,
        @Min(0)
        Integer quantity,
        @NotNull
        UUID wordUuid
) {
    public static CourseNavbarDto of(final Course course, final UUID wordUuid) {
        return new CourseNavbarDto(
                course.getUuid(),
                course.getTitle(),
                course.getLevel(),
                course.getQuantity(),
                wordUuid
        );
    }
}
