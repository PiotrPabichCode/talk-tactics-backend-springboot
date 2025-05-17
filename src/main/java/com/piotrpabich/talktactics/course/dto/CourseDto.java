package com.piotrpabich.talktactics.course.dto;

import com.piotrpabich.talktactics.course.entity.Course;
import com.piotrpabich.talktactics.course.entity.CourseLevel;

import java.util.UUID;

public record CourseDto(
        UUID uuid,
        String title,
        String description,
        CourseLevel level,
        Integer quantity,
        Integer points
) {
    public static CourseDto of(final Course course) {
        return new CourseDto(
                course.getUuid(),
                course.getTitle(),
                course.getDescription(),
                course.getLevel(),
                course.getQuantity(),
                course.getPoints()
        );
    }
}