package com.example.talktactics.service.user_course;

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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        Page<UserCourse> page = userCourseRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        if(criteria.getFetchCourses() != null && page.getNumberOfElements() < page.getSize()) {
            int remaining = page.getSize() - page.getNumberOfElements();
            Set<Long> courseIds = page.stream().map(UserCourse::getCourse).map(Course::getId).collect(Collectors.toSet());
            PageResult<CourseDto> page1 = courseService.queryAll(CourseQueryCriteria.fromUserCourseQueryCriteria(criteria, courseIds), PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));

            List<UserCourseDto> content = Stream.concat(
                            page.stream().map(userCourseMapper::toDto),
                            page1.content().stream().map(UserCourseDto::fromCourseDto)
                    )
                    .sorted(SortUtil.getComparator(pageable.getSort()))
                    .limit(remaining)
                    .toList();
            long totalElements = page.getTotalElements() + page1.totalElements();
            long totalPages = (totalElements + pageable.getPageSize() - 1) / pageable.getPageSize();

            return new PageResult<>(content, totalElements, totalPages);
        }

        return PageUtil.toPage(page.map(userCourseMapper::toDto));
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
}
