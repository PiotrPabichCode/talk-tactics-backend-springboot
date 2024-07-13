package com.piotrpabich.talktactics.dto.user_course;

import com.piotrpabich.talktactics.dto.course.CourseDto;
import com.piotrpabich.talktactics.entity.UserCourse;

public record UserCourseDto(
        Long id,
        Double progress,
        Boolean completed,
        Integer points,
        CourseDto course
) {
    public static UserCourseDto from(UserCourse userCourse) {
        return new UserCourseDto(
                userCourse.getId(),
                userCourse.getProgress(),
                userCourse.isCompleted(),
                userCourse.getPoints(),
                CourseDto.from(userCourse.getCourse())
        );
    }

    public static UserCourseDto from(CourseDto course) {
        return new UserCourseDto(
                null,
                0.0,
                false,
                0,
                course
        );
    }
}
