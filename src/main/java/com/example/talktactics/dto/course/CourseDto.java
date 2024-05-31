package com.example.talktactics.dto.course;

import com.example.talktactics.dto.user_course.UserCourseDto;
import com.example.talktactics.entity.Course;
import com.example.talktactics.entity.CourseLevel;
import lombok.Data;

import java.io.Serializable;

@Data
public class CourseDto implements Serializable {
    private Long id;
    private String title;
    private String description;
    private CourseLevel level;
    private int quantity;
    private int points;

    public static CourseDto from(Course course) {
        CourseDto courseDto = new CourseDto();
        courseDto.setId(course.getId());
        courseDto.setTitle(course.getTitle());
        courseDto.setDescription(course.getDescription());
        courseDto.setLevel(course.getLevel());
        courseDto.setQuantity(course.getQuantity());
        courseDto.setPoints(course.getPoints());
        return courseDto;
    }

    public static UserCourseDto from(CourseDto courseDto) {
        UserCourseDto userCourseDto = new UserCourseDto();
        userCourseDto.setCourse(courseDto);
        return userCourseDto;
    }
}
