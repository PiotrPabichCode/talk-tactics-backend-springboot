package com.piotrpabich.talktactics.user_course;

import com.piotrpabich.talktactics.user_course.dto.UserCourseDto;
import com.piotrpabich.talktactics.user_course.dto.UserCourseQueryCriteria;
import com.piotrpabich.talktactics.user_course.dto.UserCourseRequest;
import com.piotrpabich.talktactics.course.CourseService;
import com.piotrpabich.talktactics.user.entity.User;
import com.piotrpabich.talktactics.user.UserService;
import com.piotrpabich.talktactics.user_course.entity.UserCourse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.piotrpabich.talktactics.auth.AuthUtil.validateIfUserHimselfOrAdmin;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserCourseFacade {

    private final UserCourseService userCourseService;
    private final UserService userService;
    private final CourseService courseService;

    public Page<UserCourseDto> queryAll(
            final UserCourseQueryCriteria criteria,
            final Pageable pageable,
            final User requester
    ) {
        return userCourseService.queryAll(criteria, pageable, requester);
    }

    public UserCourse getUserCourseByUuid(final UUID userCourseUuid, final User requester) {
        return userCourseService.getUserCourseByUuid(userCourseUuid, requester);
    }

    @Transactional
    public void assignUserCourse(final UserCourseRequest request, final User requester) {
        final var user = userService.getUserByUuid(request.userUuid());
        validateIfUserHimselfOrAdmin(requester, user);
        final var course = courseService.getCourseByUuid(request.courseUuid());
        userCourseService.assignUserCourse(user, course);
    }

    public void deleteUserCourse(final UserCourseRequest request, final User requester) {
        final var user = userService.getUserByUuid(request.userUuid());
        validateIfUserHimselfOrAdmin(requester, user);
        userCourseService.deleteUserCourse(request);
    }
}
