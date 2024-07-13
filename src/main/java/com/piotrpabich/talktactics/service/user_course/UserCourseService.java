package com.piotrpabich.talktactics.service.user_course;

import com.piotrpabich.talktactics.common.PageResult;
import com.piotrpabich.talktactics.dto.user_course.UserCourseDto;
import com.piotrpabich.talktactics.dto.user_course.UserCourseQueryCriteria;
import com.piotrpabich.talktactics.dto.user_course.req.UserCourseAddReqDto;
import com.piotrpabich.talktactics.dto.user_course.req.UserCourseDeleteReqDto;
import com.piotrpabich.talktactics.entity.UserCourse;
import org.springframework.data.domain.Pageable;

public interface UserCourseService {
    PageResult<UserCourseDto> queryAll(UserCourseQueryCriteria criteria, Pageable pageable);
    UserCourse getById(long id);
    void addUserCourse(UserCourseAddReqDto req);
    void deleteUserCourse(UserCourseDeleteReqDto req);
}
