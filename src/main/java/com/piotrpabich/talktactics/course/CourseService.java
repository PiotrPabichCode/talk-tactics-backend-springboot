package com.piotrpabich.talktactics.course;

import com.piotrpabich.talktactics.common.PageResult;
import com.piotrpabich.talktactics.course.dto.CourseDto;
import com.piotrpabich.talktactics.course.dto.CourseNavbarDto;
import com.piotrpabich.talktactics.course.dto.CourseQueryCriteria;
import com.piotrpabich.talktactics.course.entity.Course;
import com.piotrpabich.talktactics.user.entity.User;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface CourseService {
    PageResult<CourseDto> queryAll(CourseQueryCriteria criteria, Pageable pageable);
    List<CourseNavbarDto> getNavbarList();
    Course getById(Long id);
    void create(Course course, User requester);
    void update(Course resources, User requester);
    void delete(Set<Long> ids, User requester);
}
