package com.piotrpabich.talktactics.user_course;

import com.piotrpabich.talktactics.course.entity.Course;
import com.piotrpabich.talktactics.user_course.dto.UserCourseDto;
import com.piotrpabich.talktactics.user_course.dto.UserCourseQueryCriteria;
import com.piotrpabich.talktactics.exception.EntityExistsException;
import com.piotrpabich.talktactics.exception.EntityNotFoundException;
import com.piotrpabich.talktactics.user_course.dto.UserCourseRequest;
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
        validateQueryAll(criteria.getUserUuids(), requester);
        return userCourseRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable)
                .map(UserCourseDto::of);
    }

    @Override
    public UserCourse getUserCourseByUuid(final UUID userCourseUuid, final User requester) {
        final var userCourse = userCourseRepository.findByUuid(userCourseUuid)
                .orElseThrow(() -> new EntityNotFoundException(UserCourse.class, "uuid", String.valueOf(userCourseUuid)));
        validateIfUserHimselfOrAdmin(requester, userCourse.getUser());
        return userCourse;
    }

    @Override
    public void assignUserCourse(final User user, final Course course) {
        validateIfUserCourseExists(course.getUuid(), user.getUuid());
        final var userCourse = userCourseMapper.convert(user, course);
        final var userCourseItems = userCourseMapper.convert(userCourse);

        userCourseRepository.save(userCourse);
        userCourseItemRepository.saveAll(userCourseItems);
    }

    @Override
    public void deleteUserCourse(final UserCourseRequest request) {
        final var userCourse = userCourseRepository.findByCourseUuidAndUserUuid(request.courseUuid(), request.userUuid())
                .orElseThrow(() -> new EntityNotFoundException(
                        UserCourse.class,
                        "(courseUuid | userUuid)",
                        String.format("(%s %s)", request.courseUuid(), request.userUuid())));
        userCourseRepository.delete(userCourse);
    }

    private void validateQueryAll(final Set<UUID> userUuids, final User requester) {
        if(!isUserAdmin(requester)) {
            userUuids.stream().findAny().ifPresent(userUuid -> {
                if(!userUuid.equals(requester.getUuid())) {
                    throw new IllegalArgumentException("You can only query multiple users if you are an admin or the user himself");
                }
            });
        }
    }

    private void validateIfUserCourseExists(final UUID courseUuid, final UUID userUuid) {
        if(userCourseRepository.existsByCourseUuidAndUserUuid(courseUuid, userUuid)) {
            throw new EntityExistsException(
                    UserCourse.class,
                    "(courseUuid | userUuid)",
                    String.format("(%s | %s)", courseUuid, userUuid)
            );
        }
    }
}
