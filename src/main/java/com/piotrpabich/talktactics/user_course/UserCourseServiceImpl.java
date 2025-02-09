package com.piotrpabich.talktactics.user_course;

import com.piotrpabich.talktactics.common.OffsetBasedPageRequest;
import com.piotrpabich.talktactics.common.PageResult;
import com.piotrpabich.talktactics.course.entity.Course;
import com.piotrpabich.talktactics.course.dto.CourseQueryCriteria;
import com.piotrpabich.talktactics.user_course.dto.UserCourseDto;
import com.piotrpabich.talktactics.user_course.dto.UserCourseQueryCriteria;
import com.piotrpabich.talktactics.user_course.dto.req.UserCourseDeleteRequest;
import com.piotrpabich.talktactics.exception.EntityExistsException;
import com.piotrpabich.talktactics.exception.EntityNotFoundException;
import com.piotrpabich.talktactics.course.CourseRepository;
import com.piotrpabich.talktactics.user_course_item.entity.UserCourseItem;
import com.piotrpabich.talktactics.user_course_item.UserCourseItemRepository;
import com.piotrpabich.talktactics.user.entity.User;
import com.piotrpabich.talktactics.user_course.entity.UserCourse;
import com.piotrpabich.talktactics.common.util.PageUtil;
import com.piotrpabich.talktactics.common.QueryHelp;
import com.piotrpabich.talktactics.common.util.SortUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.piotrpabich.talktactics.auth.AuthUtil.isUserAdmin;
import static com.piotrpabich.talktactics.auth.AuthUtil.validateIfUserHimselfOrAdmin;
import static com.piotrpabich.talktactics.common.util.SortUtil.getSortProperty;
import static com.piotrpabich.talktactics.course.dto.CourseQueryCriteria.fromUserCourseQueryCriteria;
import static java.util.stream.Collectors.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserCourseServiceImpl implements UserCourseService {

    private final UserCourseItemRepository userCourseItemRepository;
    private final UserCourseRepository userCourseRepository;
    private final CourseRepository courseRepository;
    private final UserCourseMapper userCourseMapper;

    @Override
    public PageResult<UserCourseDto> queryAll(
            final UserCourseQueryCriteria criteria,
            final Pageable pageable,
            final User requester
    ) {
        validateQueryAll(criteria.getUserIds(), requester);
        if (Boolean.TRUE.equals(criteria.getFetchCourses())) {
            return queryAllWithCourses(criteria, pageable);
        }

        final var page = userCourseRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(userCourseMapper::toDto));
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
    private List<UserCourseDto> queryAll(final UserCourseQueryCriteria criteria) {
        final var items = userCourseRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
        return userCourseMapper.toDto(items);
    }

    private List<UserCourseDto> queryAll(final UserCourseQueryCriteria criteria, final Sort sort) {
        final var items = userCourseRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), sort);
        return userCourseMapper.toDto(items);
    }

    private UserCourse getUserCourse(final Long courseId, final Long userId) {
        return userCourseRepository.findByCourseIdAndUserId(courseId, userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        UserCourse.class,
                        "(courseId | userId)",
                        String.format("(%d %d)", courseId, userId)
                        ));
    }
    private PageResult<UserCourseDto> queryAllWithCourses(
            final UserCourseQueryCriteria criteria,
            final Pageable pageable
    ) {
        criteria.setFetchCourses(false);
        final var sort = pageable.getSort();
        if (isCourseSorted(sort)) {
            return processCourseSortWithCourseFetchRequest(criteria, pageable);
        }
        return processSortWithCourseFetchRequest(criteria, pageable, SortUtil.isSortAscending(sort));
    }

    private PageResult<UserCourseDto> processSortWithCourseFetchRequest(
            final UserCourseQueryCriteria criteria,
            final Pageable pageable,
            final Boolean isAscending
    ) {
        final var totalElements = courseRepository.count((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
        final var totalPages = (int) Math.ceil((double) totalElements / pageable.getPageSize());

        if(isAscending) {
            final var content = getAscendingSortContentWithFetchedCourses(criteria, pageable);
            return new PageResult<>(content, totalElements, totalPages);
        }
        final var content = getDescendingSortContentWithFetchedCourses(criteria, pageable);
        return new PageResult<>(content, totalElements, totalPages);
    }

    private List<UserCourseDto> getAscendingSortContentWithFetchedCourses(
            final UserCourseQueryCriteria criteria,
            final Pageable pageable
    ) {
        final var userCourseList = queryAll(criteria, pageable.getSort());
        final var excludeCourseIds = userCourseList.stream()
                .map(userCourse -> userCourse.course().id())
                .collect(toSet());

        final var pageRequest = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                toCoursesSort(pageable.getSort())
        );
        final var courses = fetchCourses(
                fromUserCourseQueryCriteria(criteria, excludeCourseIds),
                pageRequest
        );
        final var content = courses.content();
        if(content.size() == pageable.getPageSize()) {
            return courses.content();
        }

        int limit = pageable.getPageSize() - content.size();
        long offset = Math.max(0, (long) pageable.getPageSize() * pageable.getPageNumber() - courses.totalElements());

        final var offsetBasedData = userCourseList.stream()
                .skip(offset)
                .limit(limit)
                .toList();
        content.addAll(offsetBasedData);

        return content;
    }

    private List<UserCourseDto> getDescendingSortContentWithFetchedCourses(
            final UserCourseQueryCriteria criteria,
            final Pageable pageable
    ) {
        final var userCourseList = queryAll(criteria, pageable.getSort());

        final var content = userCourseList.stream()
                .skip((long) pageable.getPageNumber() * pageable.getPageSize())
                .limit(pageable.getPageSize())
                .collect(toList());
        if(content.size() == pageable.getPageSize()) {
            return content;
        }

        final var excludeCourseIds = userCourseList.stream()
                .map(userCourse -> userCourse.course().id())
                .collect(toSet());

        final var limit = pageable.getPageSize() - content.size();
        final var offset = Math.max(0, pageable.getPageSize() * pageable.getPageNumber() - userCourseList.size());
        final var offsetPageable = new OffsetBasedPageRequest(
                offset,
                limit,
                toCoursesSort(pageable.getSort()).and(Sort.by("id").ascending())
        );

        final var courses = fetchCourses(
                fromUserCourseQueryCriteria(criteria, excludeCourseIds),
                offsetPageable
        );

        content.addAll(courses.content());
        return content;
    }

    private PageResult<UserCourseDto> fetchCourses(
            final CourseQueryCriteria criteria,
            final Pageable pageable
    ) {
        final var result = courseRepository.findAll((root, criteriaQuery, criteriaBuilder) ->
                        QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        final var content = result.getContent()
                .stream()
                .map(UserCourseDto::from)
                .toList();

        return new PageResult<>(content, result.getTotalElements(), result.getTotalPages());
    }

    private PageResult<UserCourseDto> processCourseSortWithCourseFetchRequest(
            final UserCourseQueryCriteria criteria,
            final Pageable pageable
    ) {
        final var courses = fetchCourses(
                fromUserCourseQueryCriteria(criteria, null),
                getAdditionalCoursesPageRequest(pageable)
        );

        final var courseIds = courses.content().stream()
                .map(UserCourseDto::id)
                .collect(toSet());
        criteria.setCourseIds(courseIds);

        final var userCourses = queryAll(criteria);
        final var userCourseDtoMap = userCourses.stream()
                .collect(toMap(
                        userCourse -> userCourse.course().id(),
                        userCourse -> userCourse
                        ));

        final var content = courses.content().stream()
                .map(courseDto -> userCourseDtoMap.getOrDefault(courseDto.id(), courseDto))
                .toList();

        return new PageResult<>(content, courses.totalElements(), courses.totalPages());
    }
    private PageRequest getAdditionalCoursesPageRequest(final Pageable pageable) {
        return PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                toCoursesSort(pageable.getSort()).and(Sort.by("id").ascending())
        );
    }
    private boolean isCourseSorted(final Sort sort) {
        return getSortProperty(sort).contains("course.");
    }

    private Sort toCoursesSort(final Sort sort) {
        final var orders = sort.stream()
                .map(order -> order.getProperty().contains("course.")
                        ? new Sort.Order(order.getDirection(), order.getProperty().replace("course.", ""))
                        : null
                )
                .filter(Objects::nonNull)
                .collect(toList());
        return Sort.by(orders);
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
