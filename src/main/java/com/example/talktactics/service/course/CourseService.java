package com.example.talktactics.service.course;

import com.example.talktactics.common.PageResult;
import com.example.talktactics.dto.course.*;
import com.example.talktactics.entity.Course;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface CourseService {
    PageResult<CourseDto> queryAll(CourseQueryCriteria criteria, Pageable pageable);
    List<CourseNavbarDto> getNavbarList();
    Course getById(long id);
    long countAll(CourseQueryCriteria criteria);
    void create(Course course);
    void update(Course resources);
    void delete(Set<Long> ids);
}
