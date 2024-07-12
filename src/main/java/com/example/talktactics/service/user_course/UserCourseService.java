package com.example.talktactics.service.user_course;

import com.example.talktactics.common.PageResult;
import com.example.talktactics.dto.user_course.UserCourseDto;
import com.example.talktactics.dto.user_course.UserCourseQueryCriteria;
import com.example.talktactics.dto.user_course.req.UserCourseAddReqDto;
import com.example.talktactics.dto.user_course.req.UserCourseDeleteReqDto;
import com.example.talktactics.entity.UserCourse;
import org.springframework.data.domain.Pageable;

public interface UserCourseService {
    PageResult<UserCourseDto> queryAll(UserCourseQueryCriteria criteria, Pageable pageable);
    UserCourse getById(long id);
    void addUserCourse(UserCourseAddReqDto req);
    void deleteUserCourse(UserCourseDeleteReqDto req);
}
