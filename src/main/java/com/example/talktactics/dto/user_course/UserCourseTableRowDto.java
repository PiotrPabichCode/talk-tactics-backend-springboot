package com.example.talktactics.dto.user_course;

import com.example.talktactics.dto.course.CourseDto;
import com.example.talktactics.entity.UserCourse;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserCourseTableRowDto implements Serializable {
    private Long id;
    private Double progress;
    private Boolean completed;
    private Integer points;
    private CourseDto course;

    public static UserCourseTableRowDto from(UserCourseDto userCourseDto) {
        UserCourseTableRowDto userCourseTableRowDto = new UserCourseTableRowDto();
        userCourseTableRowDto.setId(userCourseDto.getId());
        userCourseTableRowDto.setProgress(userCourseDto.getProgress());
        userCourseTableRowDto.setCompleted(userCourseDto.getCompleted());
        userCourseTableRowDto.setPoints(userCourseDto.getPoints());
        userCourseTableRowDto.setCourse(userCourseDto.getCourse());
        return userCourseTableRowDto;
    }

    public static UserCourseTableRowDto from(CourseDto courseDto) {
        UserCourseTableRowDto userCourseTableRowDto = new UserCourseTableRowDto();
        userCourseTableRowDto.setCourse(courseDto);
        return userCourseTableRowDto;
    }

    public static UserCourseTableRowDto from(UserCourse userCourse) {
        UserCourseTableRowDto userCourseTableRowDto = new UserCourseTableRowDto();
        userCourseTableRowDto.setId(userCourse.getId());
        userCourseTableRowDto.setProgress(userCourse.getProgress());
        userCourseTableRowDto.setCompleted(userCourse.isCompleted());
        userCourseTableRowDto.setPoints(userCourse.getPoints());
        userCourseTableRowDto.setCourse(CourseDto.from(userCourse.getCourse()));
        return userCourseTableRowDto;
    }
}
