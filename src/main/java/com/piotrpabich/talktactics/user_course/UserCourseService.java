package com.piotrpabich.talktactics.user_course;

import com.piotrpabich.talktactics.user_course.dto.UserCourseDto;
import com.piotrpabich.talktactics.user_course.dto.UserCourseQueryCriteria;
import com.piotrpabich.talktactics.course.entity.Course;
import com.piotrpabich.talktactics.user.entity.User;
import com.piotrpabich.talktactics.user_course.dto.UserCourseRequest;
import com.piotrpabich.talktactics.user_course.entity.UserCourse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserCourseService {
    Page<UserCourseDto> queryAll(UserCourseQueryCriteria criteria, Pageable pageable, User requester);
    UserCourse getUserCourseByUuid(UUID userCourseUuid, User requester);
    void assignUserCourse(User user, Course course);
    void deleteUserCourse(UserCourseRequest request);
}
