package com.example.talktactics.service.course;

import com.example.talktactics.dto.course.CoursePreviewProjection;
import com.example.talktactics.entity.Course;

import java.util.List;

public interface CourseService {
    Course create(Course course);
    Course getById(Long id);
    List<CoursePreviewProjection> getPreviewList();
    Course update(Long id, Course newCourse);
    void delete(Long id);
    List<Course> filterByLevel(String level);
}
