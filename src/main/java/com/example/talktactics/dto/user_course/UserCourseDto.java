package com.example.talktactics.dto.user_course;

import com.example.talktactics.dto.course.CourseDto;
import com.example.talktactics.entity.User;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserCourseDto implements Serializable {
    private Long id;
    private Double progress;
    private Boolean completed;
    private Integer points;
    private User user;
    private CourseDto course;

    public static UserCourseDto fromCourseDto(CourseDto courseDto) {
        UserCourseDto userCourseDto = new UserCourseDto();
        userCourseDto.setCourse(courseDto);
        return userCourseDto;
    }
}
