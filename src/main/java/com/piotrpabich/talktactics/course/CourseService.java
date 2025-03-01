package com.piotrpabich.talktactics.course;

import com.piotrpabich.talktactics.common.UuidResponse;
import com.piotrpabich.talktactics.course.dto.CourseDto;
import com.piotrpabich.talktactics.course.dto.CourseNavbarDto;
import com.piotrpabich.talktactics.course.dto.CourseQueryCriteria;
import com.piotrpabich.talktactics.course.dto.CourseRequest;
import com.piotrpabich.talktactics.course.entity.Course;
import com.piotrpabich.talktactics.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface CourseService {
    Page<CourseDto> queryAll(CourseQueryCriteria criteria, Pageable pageable);
    List<CourseNavbarDto> getNavbarList();
    Course getCourseByUuid(UUID uuid);
    UuidResponse create(CourseRequest request, User requester);
    void update(UUID courseUuid, CourseRequest request, User requester);
    void delete(UUID courseUuid, User requester);
}
