package com.piotrpabich.talktactics.user_course;

import com.piotrpabich.talktactics.common.PageResult;
import com.piotrpabich.talktactics.user_course.dto.UserCourseDto;
import com.piotrpabich.talktactics.user_course.dto.UserCourseQueryCriteria;
import com.piotrpabich.talktactics.user_course.dto.req.UserCourseDeleteRequest;
import com.piotrpabich.talktactics.course.entity.Course;
import com.piotrpabich.talktactics.user.entity.User;
import com.piotrpabich.talktactics.user_course.entity.UserCourse;
import org.springframework.data.domain.Pageable;

public interface UserCourseService {
    PageResult<UserCourseDto> queryAll(UserCourseQueryCriteria criteria, Pageable pageable, User requester);
    UserCourse getById(Long id, User requester);
    void addUserCourse(User user, Course course, User requester);
    void deleteUserCourse(UserCourseDeleteRequest request);
}
