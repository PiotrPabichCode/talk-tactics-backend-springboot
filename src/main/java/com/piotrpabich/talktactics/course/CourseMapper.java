package com.piotrpabich.talktactics.course;

import com.piotrpabich.talktactics.course.dto.CourseRequest;
import com.piotrpabich.talktactics.course.entity.Course;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CourseMapper {

    public Course convert(final CourseRequest request) {
        final var course = new Course();
        course.setTitle(request.title());
        course.setDescription(request.description());
        course.setLevel(request.level());
        return course;
    }

    public Course convert(final Course course, final CourseRequest request) {
        Optional.ofNullable(request.title()).ifPresent(course::setTitle);
        Optional.ofNullable(request.description()).ifPresent(course::setDescription);
        Optional.ofNullable(request.level()).ifPresent(course::setLevel);
        return course;
    }
}
