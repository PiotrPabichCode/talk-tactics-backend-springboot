package com.piotrpabich.talktactics.service.user_course;

import com.piotrpabich.talktactics.common.PageResult;
import com.piotrpabich.talktactics.dto.user_course.UserCourseDto;
import com.piotrpabich.talktactics.dto.user_course.UserCourseQueryCriteria;
import com.piotrpabich.talktactics.dto.user_course.req.UserCourseAddReqDto;
import com.piotrpabich.talktactics.dto.user_course.req.UserCourseDeleteReqDto;
import com.piotrpabich.talktactics.entity.*;
import com.piotrpabich.talktactics.service.course.CourseService;
import com.piotrpabich.talktactics.service.user.UserService;
import com.piotrpabich.talktactics.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserCourseFacade {

    private final UserCourseService userCourseService;
    private final UserService userService;
    private final CourseService courseService;

    public PageResult<UserCourseDto> queryAll(
            UserCourseQueryCriteria criteria,
            Pageable pageable,
            User requester
    ) {
        return userCourseService.queryAll(criteria, pageable, requester);
    }

    public UserCourse getById(long id, User requester) {
        return userCourseService.getById(id, requester);
    }

    @Transactional
    public void addUserCourse(UserCourseAddReqDto req, User requester) {
        User user = userService.getUserById(req.userId());
        Course course = courseService.getById(req.courseId());
        userCourseService.addUserCourse(user, course, requester);
    }

    @Transactional
    public void deleteUserCourse(UserCourseDeleteReqDto req, User requester) {
        User user = userService.getUserById(req.userId());
        AuthUtil.validateIfUserHimselfOrAdmin(requester, user);
        userCourseService.deleteUserCourse(req);
    }
}
