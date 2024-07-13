package com.piotrpabich.talktactics.service.course;

import com.piotrpabich.talktactics.common.PageResult;
import com.piotrpabich.talktactics.dto.course.*;
import com.piotrpabich.talktactics.entity.Course;
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
