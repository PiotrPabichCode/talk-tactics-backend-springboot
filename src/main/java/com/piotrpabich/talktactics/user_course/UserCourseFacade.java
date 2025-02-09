package com.piotrpabich.talktactics.user_course;

import com.piotrpabich.talktactics.common.PageResult;
import com.piotrpabich.talktactics.user_course.dto.UserCourseDto;
import com.piotrpabich.talktactics.user_course.dto.UserCourseQueryCriteria;
import com.piotrpabich.talktactics.user_course.dto.UserCourseAddRequest;
import com.piotrpabich.talktactics.user_course.dto.UserCourseDeleteRequest;
import com.piotrpabich.talktactics.course.CourseService;
import com.piotrpabich.talktactics.user.entity.User;
import com.piotrpabich.talktactics.user.UserService;
import com.piotrpabich.talktactics.user_course.entity.UserCourse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.piotrpabich.talktactics.auth.AuthUtil.validateIfUserHimselfOrAdmin;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserCourseFacade {

    private final UserCourseService userCourseService;
    private final UserService userService;
    private final CourseService courseService;

    public PageResult<UserCourseDto> queryAll(
            final UserCourseQueryCriteria criteria,
            final Pageable pageable,
            final User requester
    ) {
        return userCourseService.queryAll(criteria, pageable, requester);
    }

    public UserCourse getById(final Long id, final User requester) {
        return userCourseService.getById(id, requester);
    }

    @Transactional
    public void addUserCourse(final UserCourseAddRequest request, final User requester) {
        final var user = userService.getUserById(request.userId());
        final var course = courseService.getById(request.courseId());
        userCourseService.addUserCourse(user, course, requester);
    }

    @Transactional
    public void deleteUserCourse(final UserCourseDeleteRequest request, final User requester) {
        final var user = userService.getUserById(request.userId());
        validateIfUserHimselfOrAdmin(requester, user);
        userCourseService.deleteUserCourse(request);
    }
}
