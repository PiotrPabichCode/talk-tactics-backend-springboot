package com.piotrpabich.talktactics.course.dto;

import com.piotrpabich.talktactics.user_course.dto.UserCourseDto;
import com.piotrpabich.talktactics.course.entity.Course;
import com.piotrpabich.talktactics.course.entity.CourseLevel;

public record CourseDto(
        Long id,
        String title,
        String description,
        CourseLevel level,
        Integer quantity,
        Integer points
) {
    public static CourseDto from(Course course) {
        return new CourseDto(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                course.getLevel(),
                course.getQuantity(),
                course.getPoints()
        );
    }

    public static UserCourseDto from(CourseDto courseDto) {
        return new UserCourseDto(
                null,
                0.0,
                false,
                0,
                courseDto
        );
    }
}