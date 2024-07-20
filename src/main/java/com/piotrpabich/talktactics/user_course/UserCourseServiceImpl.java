package com.piotrpabich.talktactics.user_course;

import com.piotrpabich.talktactics.common.OffsetBasedPageRequest;
import com.piotrpabich.talktactics.common.PageResult;
import com.piotrpabich.talktactics.course.entity.Course;
import com.piotrpabich.talktactics.course.dto.CourseQueryCriteria;
import com.piotrpabich.talktactics.user_course.dto.UserCourseDto;
import com.piotrpabich.talktactics.user_course.dto.UserCourseQueryCriteria;
import com.piotrpabich.talktactics.user_course.dto.req.UserCourseDeleteReqDto;
import com.piotrpabich.talktactics.exception.EntityExistsException;
import com.piotrpabich.talktactics.exception.EntityNotFoundException;
import com.piotrpabich.talktactics.course.CourseRepository;
import com.piotrpabich.talktactics.user_course_item.entity.UserCourseItem;
import com.piotrpabich.talktactics.user_course_item.UserCourseItemRepository;
import com.piotrpabich.talktactics.user.entity.User;
import com.piotrpabich.talktactics.user_course.entity.UserCourse;
import com.piotrpabich.talktactics.auth.AuthUtil;
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

@Service
@Slf4j
@RequiredArgsConstructor
public class UserCourseServiceImpl implements UserCourseService {

    private final UserCourseItemRepository userCourseItemRepository;
    private final UserCourseRepository userCourseRepository;
    private final CourseRepository courseRepository;
    private final UserCourseMapper userCourseMapper;

//  PUBLIC
    @Override
    public PageResult<UserCourseDto> queryAll(
            UserCourseQueryCriteria criteria,
            Pageable pageable,
            User requester
    ) {
        final var userIds = criteria.getUserIds();
        validateQueryAll(userIds, requester);
        if (Boolean.TRUE.equals(criteria.getFetchCourses())) {
            return queryAllWithCourses(criteria, pageable);
        }

        Page<UserCourse> page = userCourseRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(userCourseMapper::toDto));
    }
    @Override
    public UserCourse getById(long id, User requester) {
        UserCourse userCourse = userCourseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(UserCourse.class, "id", String.valueOf(id)));
        AuthUtil.validateIfUserHimselfOrAdmin(requester, userCourse.getUser());
        return userCourse;
    }
    @Override
    public void addUserCourse(User user, Course course, User requester) {
        AuthUtil.validateIfUserHimselfOrAdmin(requester, user);
        validateIfUserCourseExists(course.getId(), user.getId());

        final var userCourse = buildUserCourse(user, course);
        final var userCourseItems = buildUserCourseItems(userCourse);

        userCourseRepository.save(userCourse);
        userCourseItemRepository.saveAll(userCourseItems);
    }
    @Override
    public void deleteUserCourse(UserCourseDeleteReqDto request) {
        final var userCourse = getUserCourse(
                request.courseId(),
                request.userId()
        );
        userCourseRepository.delete(userCourse);
    }

    //  PRIVATE

    private UserCourse buildUserCourse(User user, Course course) {
        return UserCourse.builder()
                .user(user)
                .course(course)
                .build();
    }

    private List<UserCourseItem> buildUserCourseItems(UserCourse userCourse) {
        return userCourse.getCourse().getCourseItems().stream()
                .map(courseItem -> UserCourseItem.builder()
                        .courseItem(courseItem)
                        .userCourse(userCourse)
                        .build()
                )
                .collect(Collectors.toList());
    }
    private void validateQueryAll(Set<Long> userIds, User requester) {
        if(!AuthUtil.isUserAdmin(requester)) {
            userIds.stream().findAny().ifPresent(userId -> {
                if(!userId.equals(requester.getId())) {
                    throw new IllegalArgumentException("You can only query multiple users if you are an admin or the user himself");
                }
            });
        }
    }
    private List<UserCourseDto> queryAll(UserCourseQueryCriteria criteria) {
        final var items = userCourseRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
        return userCourseMapper.toDto(items);
    }

    private List<UserCourseDto> queryAll(UserCourseQueryCriteria criteria, Sort sort) {
        final var items = userCourseRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), sort);
        return userCourseMapper.toDto(items);
    }

    private UserCourse getUserCourse(Long courseId, Long userId) {
        return userCourseRepository.findByCourseIdAndUserId(courseId, userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        UserCourse.class,
                        "(courseId | userId)",
                        String.format("(%d %d)", courseId, userId)
                        ));
    }
    private PageResult<UserCourseDto> queryAllWithCourses(UserCourseQueryCriteria criteria, Pageable pageable) {
        criteria.setFetchCourses(false);
        Sort sort = pageable.getSort();
        if (isCourseSorted(sort)) {
            return processCourseSortWithCourseFetchRequest(criteria, pageable);
        }
        return processSortWithCourseFetchRequest(criteria, pageable, SortUtil.isSortAscending(sort));
    }

    private PageResult<UserCourseDto> processSortWithCourseFetchRequest(UserCourseQueryCriteria criteria, Pageable pageable, boolean isAscending) {
        long totalElements = courseRepository.count((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
        int totalPages = (int) Math.ceil((double) totalElements / pageable.getPageSize());

        if(isAscending) {
            List<UserCourseDto> content = getAscendingSortContentWithFetchedCourses(criteria, pageable);
            return new PageResult<>(content, totalElements, totalPages);
        }
        List<UserCourseDto> content = getDescendingSortContentWithFetchedCourses(criteria, pageable);
        return new PageResult<>(content, totalElements, totalPages);
    }

    private List<UserCourseDto> getAscendingSortContentWithFetchedCourses(UserCourseQueryCriteria criteria, Pageable pageable) {
        List<UserCourseDto> userCourseList = queryAll(criteria, pageable.getSort());
        Set<Long> excludeCourseIds = userCourseList.stream()
                .map(userCourse -> userCourse.course().id())
                .collect(Collectors.toSet());

        PageRequest pageRequest = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                toCoursesSort(pageable.getSort())
        );
        final var courses = fetchCourses(
                CourseQueryCriteria.fromUserCourseQueryCriteria(criteria, excludeCourseIds),
                pageRequest
        );
        final var content = courses.content();
        if(content.size() == pageable.getPageSize()) {
            return courses.content();
        }

        int limit = pageable.getPageSize() - content.size();
        long offset = Math.max(0, (long) pageable.getPageSize() * pageable.getPageNumber() - courses.totalElements());

        List<UserCourseDto> offsetBasedData = userCourseList.stream()
                .skip(offset)
                .limit(limit)
                .toList();
        content.addAll(offsetBasedData);

        return content;
    }

    private List<UserCourseDto> getDescendingSortContentWithFetchedCourses(UserCourseQueryCriteria criteria, Pageable pageable) {
        List<UserCourseDto> userCourseList = queryAll(criteria, pageable.getSort());

        List<UserCourseDto> content = userCourseList.stream()
                .skip((long) pageable.getPageNumber() * pageable.getPageSize())
                .limit(pageable.getPageSize())
                .collect(Collectors.toList());
        if(content.size() == pageable.getPageSize()) {
            return content;
        }

        Set<Long> excludeCourseIds = userCourseList.stream()
                .map(userCourse -> userCourse.course().id())
                .collect(Collectors.toSet());

        int limit = pageable.getPageSize() - content.size();
        int offset = Math.max(0, pageable.getPageSize() * pageable.getPageNumber() - userCourseList.size());
        Pageable offsetPageable = new OffsetBasedPageRequest(
                offset,
                limit,
                toCoursesSort(pageable.getSort()).and(Sort.by("id").ascending())
        );

        final var courses = fetchCourses(
                CourseQueryCriteria.fromUserCourseQueryCriteria(criteria, excludeCourseIds),
                offsetPageable
        );

        content.addAll(courses.content());
        return content;
    }

    private PageResult<UserCourseDto> fetchCourses(CourseQueryCriteria criteria,
                                             Pageable pageable) {
        var result = courseRepository.findAll((root, criteriaQuery, criteriaBuilder) ->
                        QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        final var content = result.getContent()
                .stream()
                .map(UserCourseDto::from)
                .toList();

        return new PageResult<>(content, result.getTotalElements(), result.getTotalPages());
    }

    private PageResult<UserCourseDto> processCourseSortWithCourseFetchRequest(UserCourseQueryCriteria criteria, Pageable pageable) {
        final var courses = fetchCourses(
                CourseQueryCriteria.fromUserCourseQueryCriteria(criteria, null),
                getAdditionalCoursesPageRequest(pageable)
        );

        Set<Long> courseIds = courses.content().stream()
                .map(UserCourseDto::id)
                .collect(Collectors.toSet());
        criteria.setCourseIds(courseIds);

        final var userCourses = queryAll(criteria);
        Map<Long, UserCourseDto> userCourseDtoMap = userCourses.stream()
                .collect(Collectors.toMap(
                        userCourse -> userCourse.course().id(),
                        userCourse -> userCourse
                ));

        List<UserCourseDto> content = courses.content().stream()
                .map(courseDto -> userCourseDtoMap.getOrDefault(courseDto.id(), courseDto))
                .toList();

        return new PageResult<>(content, courses.totalElements(), courses.totalPages());
    }
    private PageRequest getAdditionalCoursesPageRequest(Pageable pageable) {
        return PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                toCoursesSort(pageable.getSort()).and(Sort.by("id").ascending())
        );
    }
    private boolean isCourseSorted(Sort sort) {
        return SortUtil.getSortProperty(sort).contains("course.");
    }

    private Sort toCoursesSort(Sort sort) {
        List<Sort.Order> orders = sort.stream()
                .map(order -> order.getProperty().contains("course.") ? new Sort.Order(order.getDirection(), order.getProperty().replace("course.", "")) : null)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return Sort.by(orders);
    }

    private void validateIfUserCourseExists(Long courseId, Long userId) {
        if(userCourseRepository.existsByCourseIdAndUserId(courseId, userId)) {
            throw new EntityExistsException(
                    UserCourse.class,
                    "(courseId | userId)",
                    String.format("(%d | %d)", courseId, userId)
            );
        }
    }
}
