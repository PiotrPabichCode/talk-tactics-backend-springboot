package com.example.talktactics.service.course;

import com.example.talktactics.dto.course.CoursePreviewProjection;
import com.example.talktactics.entity.Course;

import java.util.List;

public interface CourseService {
    Course create(Course course);
    Course getById(long id);
    List<CoursePreviewProjection> getPreviewList();
    Course update(long id, Course newCourse);
    void delete(long id);
    List<Course> filterByLevel(String level);
}
