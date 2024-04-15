package com.example.talktactics.service.user_course;

import com.example.talktactics.dto.user_course.req.UserCourseDeleteReqDto;
import com.example.talktactics.dto.user_course.req.UserCourseGetReqDto;
import com.example.talktactics.dto.user_course.UserCoursePreviewDto;
import com.example.talktactics.dto.user_course.req.UserCourseAddReqDto;
import com.example.talktactics.dto.user_course.res.UserCourseResponseDto;
import com.example.talktactics.exception.CourseRuntimeException;
import com.example.talktactics.exception.UserCourseRuntimeException;
import com.example.talktactics.entity.*;
import com.example.talktactics.repository.UserCourseItemRepository;
import com.example.talktactics.repository.UserCourseRepository;
import com.example.talktactics.service.course.CourseServiceImpl;
import com.example.talktactics.service.user.UserServiceImpl;
import com.example.talktactics.util.Constants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class UserCourseServiceImpl implements UserCourseService {

    private final UserCourseItemRepository userCourseItemRepository;
    private final UserCourseRepository userCourseRepository;
    private final UserServiceImpl userService;
    private final CourseServiceImpl courseService;

//  PUBLIC
    public List<UserCourse> getAllUserCourses() throws UserCourseRuntimeException {
        userService.validateAdmin();
        return userCourseRepository.findAll(Sort.by("id"));
    }

    public List<UserCourseResponseDto> getAllByUserId(long userID) throws UserCourseRuntimeException {
        userService.getUserById(userID);
        return userCourseRepository.findAllByUserId(userID).stream().map(UserCourse::toUserCourseResponseDto).toList();
    }

    public List<UserCoursePreviewDto> getUserCoursesPreviewListByUserId(long userId) throws UserCourseRuntimeException {
        return null;
    }

    public UserCourse getById(long id) throws UserCourseRuntimeException {
        UserCourse userCourse = userCourseRepository.findById(id).orElseThrow(() -> new UserCourseRuntimeException(Constants.USER_COURSE_NOT_FOUND_EXCEPTION));
        userService.validateCredentials(userCourse.getUser());
        return userCourse;
    }

    public void addUserCourse(UserCourseAddReqDto req) throws UserCourseRuntimeException, CourseRuntimeException {
        if (userCourseRepository.existsByCourseIdAndUserId(req.getCourseId(), req.getUserId())) {
            throw new UserCourseRuntimeException(Constants.USER_COURSE_EXISTS_EXCEPTION);
        }

        User user = userService.getUserById(req.getUserId());
        userService.validateCredentials(user);
        // find course
        Course course = courseService.getById(req.getCourseId());

        UserCourse userCourse = UserCourse.builder().completed(false)
                .progress(0.0).user(user).course(course).build();
        List<UserCourseItem> userCourseItems = new ArrayList<>();
        for(CourseItem courseItem: course.getCourseItems()) {
            userCourseItems.add(UserCourseItem.builder().courseItem(courseItem).isLearned(false).userCourse(userCourse).build());
        }

        userCourseRepository.save(userCourse);
        userCourseItemRepository.saveAll(userCourseItems);
    }
    public void deleteUserCourse(UserCourseDeleteReqDto req) throws UserCourseRuntimeException {
        User user = userService.getUserById(req.getUserId());
        userService.validateCredentials(user);
        UserCourse userCourse = getUserCourse(req.getCourseId(), req.getUserId());
        userCourseRepository.delete(userCourse);
    }

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
