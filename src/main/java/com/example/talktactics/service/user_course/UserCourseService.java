package com.example.talktactics.service.user_course;

import com.example.talktactics.common.PageResult;
import com.example.talktactics.dto.user_course.UserCourseDto;
import com.example.talktactics.dto.user_course.UserCourseQueryCriteria;
import com.example.talktactics.dto.user_course.req.UserCourseAddReqDto;
import com.example.talktactics.dto.user_course.req.UserCourseDeleteReqDto;
import com.example.talktactics.dto.user_course.req.UserCourseGetReqDto;
import com.example.talktactics.dto.user_course.UserCourseDetailsDto;
import com.example.talktactics.entity.UserCourse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface UserCourseService {
    List<UserCourse> getAllUserCourses();
    List<UserCourseDetailsDto> getAllByUserId(long userID);
    UserCourse getById(long id);
    void addUserCourse(UserCourseAddReqDto req);
    void deleteUserCourse(UserCourseDeleteReqDto req);
    UserCourse getByUserIdAndCourseId(UserCourseGetReqDto req);

    PageResult<UserCourseDto> queryAll(UserCourseQueryCriteria criteria, Pageable pageable);
    List<UserCourseDto> queryAll(UserCourseQueryCriteria criteria);
    List<UserCourseDto> queryAll(UserCourseQueryCriteria criteria, Sort sort);
}
