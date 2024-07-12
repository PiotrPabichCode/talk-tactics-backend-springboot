package com.example.talktactics.dto.course;

import com.example.talktactics.dto.user_course.UserCourseDto;
import com.example.talktactics.entity.Course;
import com.example.talktactics.entity.CourseLevel;

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