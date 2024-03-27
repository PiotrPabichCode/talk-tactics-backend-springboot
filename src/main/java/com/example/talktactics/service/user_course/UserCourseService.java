package com.example.talktactics.service.user_course;

import com.example.talktactics.dto.user_course.UserCourseDeleteDto;
import com.example.talktactics.dto.user_course.UserCourseGetDto;
import com.example.talktactics.dto.user_course.UserCoursePreviewDto;
import com.example.talktactics.dto.user_course.UserCourseRequestDto;
import com.example.talktactics.exception.CourseNotFoundException;
import com.example.talktactics.exception.UserCourseExistsException;
import com.example.talktactics.exception.UserCourseNotFoundException;
import com.example.talktactics.exception.UserNotFoundException;
import com.example.talktactics.entity.*;
import com.example.talktactics.repository.CourseRepository;
import com.example.talktactics.repository.UserCourseItemRepository;
import com.example.talktactics.repository.UserCourseRepository;
import com.example.talktactics.repository.UserRepository;
import com.example.talktactics.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UserCourseService {

    private final UserCourseItemRepository userCourseItemRepository;
    private final UserCourseRepository userCourseRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final UserService userService;

    public List<UserCourse> getAllUserCourses() {
        return userCourseRepository.findAll(Sort.by("id"));
    }

    public List<UserCourse> getAllUserCoursesByUserId(Long id) {
        return userService.getUserById(id).getUserCourses();
    }
    public List<UserCourse> getAllUserCoursesByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User %s not found".formatted(username)));
        return user.getUserCourses();
    }

    public List<UserCoursePreviewDto> getUserCoursesPreviewListByUserId(Long id) {
        return userService.getUserById(id).getUserCourses().stream().map(UserCourse::toUserCoursePreviewDto).toList();
    }

    public UserCourse getByCourseNameAndUsername(String name, String username) {
        return userCourseRepository.findByCourseTitleAndUserUsername(name, username);
    }

    public UserCourse getById(Long id) {
        return userCourseRepository.findById(id).orElseThrow(() -> new UserCourseNotFoundException(id));
    }

    public void addUserCourse(UserCourseRequestDto userCourseRequestDto) {
        User user = userService.getUserById(userCourseRequestDto.getUserId());

        if (userCourseRepository.existsByCourseIdAndUserId(userCourseRequestDto.getCourseId(), userCourseRequestDto.getUserId())) {
            throw new UserCourseExistsException("UserCourse already exists");
        }
        // find course
        Course course = courseRepository.findById(userCourseRequestDto.getCourseId()).orElseThrow(() -> new CourseNotFoundException(userCourseRequestDto.getCourseId()));

        UserCourse userCourse = UserCourse.builder().completed(false)
                .progress(0.0).user(user).course(course).build();
        List<UserCourseItem> userCourseItems = new ArrayList<>();
        for(CourseItem courseItem: course.getCourseItems()) {
            userCourseItems.add(UserCourseItem.builder().courseItem(courseItem).isLearned(false).userCourse(userCourse).build());
        }

        userCourseRepository.save(userCourse);
        userCourseItemRepository.saveAll(userCourseItems);
    }

    public void deleteUserCourse(UserCourseDeleteDto userCourseDeleteDto) {
        UserCourse userCourse = userCourseRepository.findByCourseIdAndUserId(userCourseDeleteDto.getCourseId(), userCourseDeleteDto.getUserId());
        userCourseRepository.delete(userCourse);
    }

    public UserCourse getByUserIdAndCourseId(UserCourseGetDto userCourseGetDto) {
        return userCourseRepository.findByCourseIdAndUserId(userCourseGetDto.getCourseId(), userCourseGetDto.getUserId());
    }
}
