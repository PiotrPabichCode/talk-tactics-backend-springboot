package com.piotrpabich.talktactics.user_course.dto;

import com.piotrpabich.talktactics.course.dto.CourseDto;
import com.piotrpabich.talktactics.user_course.entity.UserCourse;

public record UserCourseDto(
        Long id,
        Double progress,
        Boolean completed,
        Integer points,
        CourseDto course
) {
    public static UserCourseDto of(final UserCourse userCourse) {
        return new UserCourseDto(
                userCourse.getId(),
                userCourse.getProgress(),
                userCourse.getCompleted(),
                userCourse.getPoints(),
                CourseDto.of(userCourse.getCourse())
        );
    }
}
