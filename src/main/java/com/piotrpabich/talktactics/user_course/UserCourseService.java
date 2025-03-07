package com.piotrpabich.talktactics.user_course;

import com.piotrpabich.talktactics.course.entity.Course;
import com.piotrpabich.talktactics.exception.ConflictException;
import com.piotrpabich.talktactics.exception.ForbiddenException;
import com.piotrpabich.talktactics.exception.NotFoundException;
import com.piotrpabich.talktactics.user_course.dto.UserCourseDto;
import com.piotrpabich.talktactics.user_course.dto.UserCourseQueryCriteria;
import com.piotrpabich.talktactics.user_course.dto.UserCourseRequest;
import com.piotrpabich.talktactics.user_course_item.UserCourseItemRepository;
import com.piotrpabich.talktactics.user.entity.User;
import com.piotrpabich.talktactics.user_course.entity.UserCourse;
import com.piotrpabich.talktactics.common.QueryHelp;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.piotrpabich.talktactics.auth.AuthUtil.isUserAdmin;
import static com.piotrpabich.talktactics.auth.AuthUtil.validateIfUserHimselfOrAdmin;

@Service
@Log4j2
@RequiredArgsConstructor
public class UserCourseService {

    private final UserCourseItemRepository userCourseItemRepository;
    private final UserCourseRepository userCourseRepository;
    private final UserCourseMapper userCourseMapper;

    public Page<UserCourseDto> queryAll(
            final UserCourseQueryCriteria criteria,
            final Pageable pageable,
            final User requester
    ) {
        validateQueryAll(criteria.getUserUuids(), requester);
        return userCourseRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable)
                .map(UserCourseDto::of);
    }

    public UserCourse getUserCourseByUuid(final UUID userCourseUuid, final User requester) {
        final var userCourse = userCourseRepository.findByUuid(userCourseUuid)
                .orElseThrow(() -> new NotFoundException(String.format("User course with uuid %s not found", userCourseUuid)));
        validateIfUserHimselfOrAdmin(requester, userCourse.getUser());
        return userCourse;
    }

    public void assignUserCourse(final User user, final Course course) {
        validateIfUserCourseExists(course.getUuid(), user.getUuid());
        final var userCourse = new UserCourse(user, course);
        final var userCourseItems = userCourseMapper.convert(userCourse);

        userCourseRepository.save(userCourse);
        userCourseItemRepository.saveAll(userCourseItems);
    }

    public void deleteUserCourse(final UserCourseRequest request) {
        final var userCourse = userCourseRepository.findByCourseUuidAndUserUuid(request.courseUuid(), request.userUuid())
                .orElseThrow(() -> new NotFoundException(String.format("User course with course: %s and user: %s not found", request.courseUuid(), request.userUuid())));
        userCourseRepository.delete(userCourse);
    }

    private void validateQueryAll(final Set<UUID> userUuids, final User requester) {
        if (!isUserAdmin(requester)) {
            userUuids.stream().findAny().ifPresent(userUuid -> {
                if(!userUuid.equals(requester.getUuid())) {
                    throw new ForbiddenException("You can only query multiple users if you are an admin or the user himself");
                }
            });
        }
    }

    private void validateIfUserCourseExists(final UUID courseUuid, final UUID userUuid) {
        if (userCourseRepository.existsByCourseUuidAndUserUuid(courseUuid, userUuid)) {
            throw new ConflictException(String.format("User course with course: %s and user: %s already exists", courseUuid, userUuid));
        }
    }
}
