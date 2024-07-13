package com.piotrpabich.talktactics.service.course;

import com.piotrpabich.talktactics.common.PageResult;
import com.piotrpabich.talktactics.dto.course.*;
import com.piotrpabich.talktactics.entity.Course;
import com.piotrpabich.talktactics.entity.User;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface CourseService {
    PageResult<CourseDto> queryAll(CourseQueryCriteria criteria, Pageable pageable);
    List<CourseNavbarDto> getNavbarList();
    Course getById(long id);
    void create(Course course, User requester);
    void update(Course resources, User requester);
    void delete(Set<Long> ids, User requester);
}
