package com.piotrpabich.talktactics.user_course.dto;

import com.piotrpabich.talktactics.course.dto.CourseDto;
import com.piotrpabich.talktactics.course.entity.Course;
import com.piotrpabich.talktactics.user_course.entity.UserCourse;

public record UserCourseDto(
        Long id,
        Double progress,
        Boolean completed,
        Integer points,
        CourseDto course
) {
    public static UserCourseDto from(final UserCourse userCourse) {
        return new UserCourseDto(
                userCourse.getId(),
                userCourse.getProgress(),
                userCourse.isCompleted(),
                userCourse.getPoints(),
                CourseDto.from(userCourse.getCourse())
        );
    }

    public static UserCourseDto from(final Course course) {
        return new UserCourseDto(
                null,
                0.0,
                false,
                0,
                CourseDto.from(course)
        );
    }
}
