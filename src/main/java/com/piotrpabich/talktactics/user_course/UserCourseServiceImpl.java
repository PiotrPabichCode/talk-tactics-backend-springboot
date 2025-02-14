package com.piotrpabich.talktactics.user_course;

import com.piotrpabich.talktactics.course.entity.Course;
import com.piotrpabich.talktactics.user_course.dto.UserCourseDto;
import com.piotrpabich.talktactics.user_course.dto.UserCourseQueryCriteria;
import com.piotrpabich.talktactics.user_course.dto.UserCourseDeleteRequest;
import com.piotrpabich.talktactics.exception.EntityExistsException;
import com.piotrpabich.talktactics.exception.EntityNotFoundException;
import com.piotrpabich.talktactics.course.CourseRepository;
import com.piotrpabich.talktactics.user_course_item.entity.UserCourseItem;
import com.piotrpabich.talktactics.user_course_item.UserCourseItemRepository;
import com.piotrpabich.talktactics.user.entity.User;
import com.piotrpabich.talktactics.user_course.entity.UserCourse;
import com.piotrpabich.talktactics.common.QueryHelp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.piotrpabich.talktactics.auth.AuthUtil.isUserAdmin;
import static com.piotrpabich.talktactics.auth.AuthUtil.validateIfUserHimselfOrAdmin;
import static java.util.stream.Collectors.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserCourseServiceImpl implements UserCourseService {

    private final UserCourseItemRepository userCourseItemRepository;
    private final UserCourseRepository userCourseRepository;
    private final UserCourseMapper userCourseMapper;

    @Override
    public Page<UserCourseDto> queryAll(
            final UserCourseQueryCriteria criteria,
            final Pageable pageable,
            final User requester
    ) {
        validateQueryAll(criteria.getUserIds(), requester);
        return userCourseRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable)
                .map(userCourseMapper::toDto);
    }
    @Override
    public UserCourse getById(final Long id, final User requester) {
        final var userCourse = userCourseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(UserCourse.class, "id", String.valueOf(id)));
        validateIfUserHimselfOrAdmin(requester, userCourse.getUser());
        return userCourse;
    }
    @Override
    public void addUserCourse(
            final User user,
            final Course course,
            final User requester
    ) {
        validateIfUserHimselfOrAdmin(requester, user);
        validateIfUserCourseExists(course.getId(), user.getId());

        final var userCourse = buildUserCourse(user, course);
        final var userCourseItems = buildUserCourseItems(userCourse);

        userCourseRepository.save(userCourse);
        userCourseItemRepository.saveAll(userCourseItems);
    }
    @Override
    public void deleteUserCourse(final UserCourseDeleteRequest request) {
        final var userCourse = getUserCourse(request.courseId(), request.userId());
        userCourseRepository.delete(userCourse);
    }

    private UserCourse buildUserCourse(final User user, final Course course) {
        return UserCourse.builder()
                .user(user)
                .course(course)
                .build();
    }

    private List<UserCourseItem> buildUserCourseItems(final UserCourse userCourse) {
        return userCourse.getCourse().getCourseItems().stream()
                .map(courseItem -> UserCourseItem.builder()
                        .courseItem(courseItem)
                        .userCourse(userCourse)
                        .build()
                )
                .collect(toList());
    }
    private void validateQueryAll(
            final Set<Long> userIds,
            final User requester
    ) {
        if(!isUserAdmin(requester)) {
            userIds.stream().findAny().ifPresent(userId -> {
                if(!userId.equals(requester.getId())) {
                    throw new IllegalArgumentException("You can only query multiple users if you are an admin or the user himself");
                }
            });
        }
    }

    private UserCourse getUserCourse(final Long courseId, final Long userId) {
        return userCourseRepository.findByCourseIdAndUserId(courseId, userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        UserCourse.class,
                        "(courseId | userId)",
                        String.format("(%d %d)", courseId, userId)
                        ));
    }

    private void validateIfUserCourseExists(
            final Long courseId,
            final Long userId
    ) {
        if(userCourseRepository.existsByCourseIdAndUserId(courseId, userId)) {
            throw new EntityExistsException(
                    UserCourse.class,
                    "(courseId | userId)",
                    String.format("(%d | %d)", courseId, userId)
            );
        }
    }
}
