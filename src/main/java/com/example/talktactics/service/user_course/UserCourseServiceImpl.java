package com.example.talktactics.service.user_course;

import com.example.talktactics.common.OffsetBasedPageRequest;
import com.example.talktactics.common.PageResult;
import com.example.talktactics.dto.course.CourseDto;
import com.example.talktactics.dto.course.CourseQueryCriteria;
import com.example.talktactics.dto.user_course.UserCourseDto;
import com.example.talktactics.dto.user_course.UserCourseQueryCriteria;
import com.example.talktactics.dto.user_course.req.UserCourseDeleteReqDto;
import com.example.talktactics.dto.user_course.req.UserCourseAddReqDto;
import com.example.talktactics.exception.EntityExistsException;
import com.example.talktactics.exception.EntityNotFoundException;
import com.example.talktactics.entity.*;
import com.example.talktactics.repository.UserCourseItemRepository;
import com.example.talktactics.repository.UserCourseRepository;
import com.example.talktactics.service.course.CourseService;
import com.example.talktactics.service.user.UserService;
import com.example.talktactics.util.PageUtil;
import com.example.talktactics.util.QueryHelp;
import com.example.talktactics.util.SortUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserCourseServiceImpl implements UserCourseService {

    private final UserCourseItemRepository userCourseItemRepository;
    private final UserCourseRepository userCourseRepository;
    private final UserService userService;
    private final CourseService courseService;
    private final UserCourseMapper userCourseMapper;

    public UserCourseServiceImpl(
            UserCourseItemRepository userCourseItemRepository,
            UserCourseRepository userCourseRepository,
            @Lazy UserService userService,
            CourseService courseService,
            UserCourseMapper userCourseMapper) {
        this.userCourseItemRepository = userCourseItemRepository;
        this.userCourseRepository = userCourseRepository;
        this.userService = userService;
        this.courseService = courseService;
        this.userCourseMapper = userCourseMapper;
    }

//  PUBLIC
    @Override
    public PageResult<UserCourseDto> queryAll(UserCourseQueryCriteria criteria, Pageable pageable) {
        if (Boolean.TRUE.equals(criteria.getFetchCourses())) {
            return queryAllWithCourses(criteria, pageable);
        }

        Page<UserCourse> page = userCourseRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(userCourseMapper::toDto));
    }
    @Override
    public UserCourse getById(long id) {
        UserCourse userCourse = userCourseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(UserCourse.class, "id", String.valueOf(id)));
        userService.validateCredentials(userCourse.getUser());
        return userCourse;
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUserCourse(UserCourseAddReqDto req) {
        if (userCourseRepository.existsByCourseIdAndUserId(req.courseId(), req.userId())) {
            throw new EntityExistsException(UserCourse.class, "(courseId | userId)", String.format("(%d %d)", req.courseId(), req.userId()));
        }

        User user = userService.getUserById(req.userId());
        userService.validateCredentials(user);
        // find course
        Course course = courseService.getById(req.courseId());

        UserCourse userCourse = UserCourse.builder().user(user).course(course).build();
        List<UserCourseItem> userCourseItems = new ArrayList<>();
        for(CourseItem courseItem: course.getCourseItems()) {
            userCourseItems.add(UserCourseItem.builder().courseItem(courseItem).userCourse(userCourse).build());
        }

        userCourseRepository.save(userCourse);
        userCourseItemRepository.saveAll(userCourseItems);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUserCourse(UserCourseDeleteReqDto req) {
        User user = userService.getUserById(req.userId());
        userService.validateCredentials(user);
        UserCourse userCourse = getUserCourse(req.courseId(), req.userId());
        userCourseRepository.delete(userCourse);
    }

    //  PRIVATE

    private List<UserCourseDto> queryAll(UserCourseQueryCriteria criteria) {
        List<UserCourse> list = userCourseRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
        return userCourseMapper.toDto(list);
    }

    private List<UserCourseDto> queryAll(UserCourseQueryCriteria criteria, Sort sort) {
        List<UserCourse> list = userCourseRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), sort);
        return userCourseMapper.toDto(list);
    }

    private UserCourse getUserCourse(Long courseId, Long userId) {
        UserCourse userCourse = userCourseRepository.findByCourseIdAndUserId(courseId, userId);
        if(userCourse == null) {
            throw new EntityNotFoundException(UserCourse.class, "courseId", courseId.toString());
        }
        return userCourse;
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
        long totalElements = courseService.countAll(CourseQueryCriteria.fromUserCourseQueryCriteria(criteria, null));
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
        PageResult<CourseDto> courseDtoPageResult = courseService.queryAll(
                CourseQueryCriteria.fromUserCourseQueryCriteria(criteria, excludeCourseIds),
                pageRequest
        );
        List<UserCourseDto> content = courseDtoPageResult.content().stream()
                .map(UserCourseDto::from)
                .collect(Collectors.toList());
        if(content.size() == pageable.getPageSize()) {
            return content;
        }

        int limit = pageable.getPageSize() - content.size();
        long offset = Math.max(0, (long) pageable.getPageSize() * pageable.getPageNumber() - courseDtoPageResult.totalElements());

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

        PageResult<CourseDto> courseDtoPageResult = courseService.queryAll(
                CourseQueryCriteria.fromUserCourseQueryCriteria(criteria, excludeCourseIds),
                offsetPageable
        );

        List<UserCourseDto> additionalContent = courseDtoPageResult.content().stream()
                .map(UserCourseDto::from)
                .toList();

        content.addAll(additionalContent);
        return content;
    }

    private PageResult<UserCourseDto> processCourseSortWithCourseFetchRequest(UserCourseQueryCriteria criteria, Pageable pageable) {
        PageResult<CourseDto> courseDtoPageResult = courseService.queryAll(
                CourseQueryCriteria.fromUserCourseQueryCriteria(criteria, null),
                getAdditionalCoursesPageRequest(pageable)
        );

        Set<Long> courseIds = courseDtoPageResult.content().stream()
                .map(CourseDto::id)
                .collect(Collectors.toSet());
        criteria.setCourseIds(courseIds);

        List<UserCourseDto> userCourseDtoList = queryAll(criteria);
        Map<Long, UserCourseDto> userCourseDtoMap = userCourseDtoList.stream()
                .collect(Collectors.toMap(
                        userCourse -> userCourse.course().id(),
                        userCourse -> userCourse
                ));

        List<UserCourseDto> content = courseDtoPageResult.content().stream()
                .map(courseDto -> userCourseDtoMap.getOrDefault(courseDto.id(), UserCourseDto.from(courseDto)))
                .toList();

        return new PageResult<>(content, courseDtoPageResult.totalElements(), courseDtoPageResult.totalPages());
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
}
