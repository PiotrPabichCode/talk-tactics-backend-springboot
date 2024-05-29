package com.example.talktactics.service.user_course;

import com.example.talktactics.common.OffsetBasedPageRequest;
import com.example.talktactics.common.PageResult;
import com.example.talktactics.dto.course.CourseDto;
import com.example.talktactics.dto.course.CourseQueryCriteria;
import com.example.talktactics.dto.user_course.UserCourseDto;
import com.example.talktactics.dto.user_course.UserCourseQueryCriteria;
import com.example.talktactics.dto.user_course.req.UserCourseDeleteReqDto;
import com.example.talktactics.dto.user_course.req.UserCourseGetReqDto;
import com.example.talktactics.dto.user_course.req.UserCourseAddReqDto;
import com.example.talktactics.dto.user_course.UserCourseDetailsDto;
import com.example.talktactics.exception.CourseRuntimeException;
import com.example.talktactics.exception.UserCourseRuntimeException;
import com.example.talktactics.entity.*;
import com.example.talktactics.repository.UserCourseItemRepository;
import com.example.talktactics.repository.UserCourseRepository;
import com.example.talktactics.service.course.CourseService;
import com.example.talktactics.service.user.UserService;
import com.example.talktactics.util.Constants;
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
@Transactional
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
    public List<UserCourseDto> queryAll(UserCourseQueryCriteria criteria) {
        List<UserCourse> list = userCourseRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
        return userCourseMapper.toDto(list);
    }

    @Override
    public List<UserCourseDto> queryAll(UserCourseQueryCriteria criteria, Sort sort) {
        List<UserCourse> list = userCourseRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), sort);
        return userCourseMapper.toDto(list);
    }

    @Override
    public List<UserCourse> getAllUserCourses() throws UserCourseRuntimeException {
        userService.validateAdmin();
        return userCourseRepository.findAll(Sort.by("id"));
    }
    @Override
    public List<UserCourseDetailsDto> getAllByUserId(long userID) throws UserCourseRuntimeException {
        return userCourseRepository.findAllByUserId(userID).stream().map(UserCourse::toUserCourseDetailsDto).sorted(Comparator.comparing(UserCourseDetailsDto::isCompleted).thenComparing(UserCourseDetailsDto::getProgress).reversed()).toList();
    }
    @Override
    public UserCourse getById(long id) throws UserCourseRuntimeException {
        UserCourse userCourse = userCourseRepository.findById(id).orElseThrow(() -> new UserCourseRuntimeException(Constants.USER_COURSE_NOT_FOUND_EXCEPTION));
        userService.validateCredentials(userCourse.getUser());
        return userCourse;
    }
    @Override
    public void addUserCourse(UserCourseAddReqDto req) throws UserCourseRuntimeException, CourseRuntimeException {
        if (userCourseRepository.existsByCourseIdAndUserId(req.getCourseId(), req.getUserId())) {
            throw new UserCourseRuntimeException(Constants.USER_COURSE_EXISTS_EXCEPTION);
        }

        User user = userService.getUserById(req.getUserId());
        userService.validateCredentials(user);
        // find course
        Course course = courseService.getById(req.getCourseId());

        UserCourse userCourse = UserCourse.builder().user(user).course(course).build();
        List<UserCourseItem> userCourseItems = new ArrayList<>();
        for(CourseItem courseItem: course.getCourseItems()) {
            userCourseItems.add(UserCourseItem.builder().courseItem(courseItem).userCourse(userCourse).build());
        }

        userCourseRepository.save(userCourse);
        userCourseItemRepository.saveAll(userCourseItems);
    }
    @Override
    public void deleteUserCourse(UserCourseDeleteReqDto req) throws UserCourseRuntimeException {
        User user = userService.getUserById(req.getUserId());
        userService.validateCredentials(user);
        UserCourse userCourse = getUserCourse(req.getCourseId(), req.getUserId());
        userCourseRepository.delete(userCourse);
    }

    @Override
    public UserCourse getByUserIdAndCourseId(UserCourseGetReqDto req) throws UserCourseRuntimeException {
        User user = userService.getUserById(req.getUserId());
        userService.validateCredentials(user);
        return getUserCourse(req.getCourseId(), req.getUserId());
    }

    //  PRIVATE

    private UserCourse getUserCourse(Long courseId, Long userId) {
        UserCourse userCourse = userCourseRepository.findByCourseIdAndUserId(courseId, userId);
        if(userCourse == null) {
            throw new UserCourseRuntimeException(Constants.USER_COURSE_NOT_FOUND_EXCEPTION);
        }
        return userCourse;
    }
    private PageResult<UserCourseDto> queryAllWithCourses(UserCourseQueryCriteria criteria, Pageable pageable) {
        criteria.setFetchCourses(false);
        Sort sort = pageable.getSort();
        if (isCourseSorted(sort)) {
            return processCourseSortRequest(criteria, pageable);
        }
        return processSortRequest(criteria, pageable, SortUtil.isSortAscending(sort));
    }

    private PageResult<UserCourseDto> processSortRequest(UserCourseQueryCriteria criteria, Pageable pageable, boolean isAscending) {
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
                .map(userCourse -> userCourse.getCourse().getId())
                .collect(Collectors.toSet());

        int offset = pageable.getPageNumber() * pageable.getPageSize();
        Pageable offsetPageable = new OffsetBasedPageRequest(
                offset,
                pageable.getPageSize(),
                toCoursesSort(pageable.getSort()).and(Sort.by("id").ascending())
        );

        PageResult<CourseDto> courseDtoPageResult = courseService.queryAll(
                CourseQueryCriteria.fromUserCourseQueryCriteria(criteria, excludeCourseIds),
                offsetPageable
        );
        List<UserCourseDto> content = courseDtoPageResult.content().stream()
                .map(UserCourseDto::fromCourseDto)
                .collect(Collectors.toList());
        if(content.size() == pageable.getPageSize()) {
            return content;
        }


        int limit = pageable.getPageSize() - content.size();
        int skip = (int) Math.max(0, offset - courseDtoPageResult.totalElements());

        userCourseList = userCourseList.stream()
                .skip(skip)
                .limit(limit)
                .collect(Collectors.toList());
        content.addAll(userCourseList);

        return content;
    }

    private List<UserCourseDto> getDescendingSortContentWithFetchedCourses(UserCourseQueryCriteria criteria, Pageable pageable) {
        int offset = pageable.getPageNumber() * pageable.getPageSize();

        List<UserCourseDto> userCourseList = queryAll(criteria, pageable.getSort());

        List<UserCourseDto> content = userCourseList.stream()
                .skip(offset)
                .limit(pageable.getPageSize())
                .collect(Collectors.toList());
        if(content.size() == pageable.getPageSize()) {
            return content;
        }

        Set<Long> excludeCourseIds = userCourseList.stream()
                .map(userCourse -> userCourse.getCourse().getId())
                .collect(Collectors.toSet());

        int limit = pageable.getPageSize() - content.size();
        offset = Math.max(0, offset - userCourseList.size() - pageable.getPageSize());
        Pageable offsetPageable = new OffsetBasedPageRequest(
                offset,
                limit,
                toCoursesSort(pageable.getSort()).and(Sort.by("id").ascending())
        );

        PageResult<CourseDto> courseDtoPageResult = courseService.queryAll(
                CourseQueryCriteria.fromUserCourseQueryCriteria(criteria, excludeCourseIds),
                offsetPageable
        );

        List<UserCourseDto> courseToUserCourseDtoList = courseDtoPageResult.content().stream()
                .map(UserCourseDto::fromCourseDto)
                .toList();

        content.addAll(courseToUserCourseDtoList);
        return content;
    }

    private PageResult<UserCourseDto> processCourseSortRequest(UserCourseQueryCriteria criteria, Pageable pageable) {
        PageResult<CourseDto> courseDtoPageResult = courseService.queryAll(
                CourseQueryCriteria.fromUserCourseQueryCriteria(criteria, null),
                getAdditionalCoursesPageRequest(pageable)
        );

        Set<Long> courseIds = courseDtoPageResult.content().stream()
                .map(CourseDto::getId)
                .collect(Collectors.toSet());
        criteria.setCourseIds(courseIds);

        List<UserCourseDto> userCourseDtoList = queryAll(criteria);
        Map<Long, UserCourseDto> userCourseDtoMap = userCourseDtoList.stream()
                .collect(Collectors.toMap(
                        userCourse -> userCourse.getCourse().getId(),
                        userCourse -> userCourse
                ));

        List<UserCourseDto> content = courseDtoPageResult.content().stream()
                .map(courseDto -> userCourseDtoMap.getOrDefault(courseDto.getId(), UserCourseDto.fromCourseDto(courseDto)))
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
