package com.example.talktactics.service.course;

import com.example.talktactics.dto.course.CourseFilterDto;
import com.example.talktactics.dto.course.CourseNavbarDto;
import com.example.talktactics.dto.course.CoursePreviewDto;
import com.example.talktactics.dto.course.CoursePreviewProjection;
import com.example.talktactics.entity.Course;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CourseService {
    Course create(Course course);
    Page<Course> getCourseList(int page, int size, CourseFilterDto filters);
    List<CourseNavbarDto> getNavbarList();
    Course getById(long id);
    List<CoursePreviewProjection> getPreviewList();
    Course update(long id, Course newCourse);
    void delete(long id);
}
