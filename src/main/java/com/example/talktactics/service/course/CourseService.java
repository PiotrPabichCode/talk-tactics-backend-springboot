package com.example.talktactics.service.course;

import com.example.talktactics.common.PageResult;
import com.example.talktactics.dto.course.*;
import com.example.talktactics.entity.Course;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CourseService {
    void create(Course course);
    void update(long id, Course newCourse);
    void delete(long id);
    PageResult<CourseDto> queryAll(CourseQueryCriteria criteria, Pageable pageable);
    long countAll(CourseQueryCriteria criteria);
    List<CourseNavbarDto> getNavbarList();
    Course getById(long id);
    List<CoursePreviewProjection> getPreviewList();
}
