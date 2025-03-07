package com.piotrpabich.talktactics.user_course.dto;

import com.piotrpabich.talktactics.course.dto.CourseDto;
import com.piotrpabich.talktactics.user_course.entity.UserCourse;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UserCourseDto(
        @NotNull
        UUID uuid,
        @Min(0)
        Double progress,
        @NotNull
        Boolean completed,
        @Min(0)
        Integer points,
        @NotNull
        CourseDto course
) {
    public static UserCourseDto of(final UserCourse userCourse) {
        return new UserCourseDto(
                userCourse.getUuid(),
                userCourse.getProgress(),
                userCourse.getCompleted(),
                userCourse.getPoints(),
                CourseDto.of(userCourse.getCourse())
        );
    }
}
