package com.example.talktactics.service.user_course;

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
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
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

    public List<UserCourse> getAllUserCourses() {
        return userCourseRepository.findAll(Sort.by("id"));
    }

    public List<UserCourse> getAllCoursesByUserId(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return user.getUserCourses();
    }
    public List<UserCourse> getAllUserCoursesByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User %s not found".formatted(username)));
        return user.getUserCourses();
    }

    public UserCourse getByCourseNameAndUsername(String name, String username) {
        return userCourseRepository.findByCourseTitleAndUserUsername(name, username);
    }

    public UserCourse getById(Long id) {
        return userCourseRepository.findById(id).orElseThrow(() -> new UserCourseNotFoundException(id));
    }

    public void addUserCourse(UserCourseRequestDto userCourseRequestDto, User user) {
        if (userCourseRepository.existsByCourseTitleAndUserUsername(userCourseRequestDto.getCourseName(), userCourseRequestDto.getUsername())) {
            throw new UserCourseExistsException("UserCourse already exists");
        }
        // find course
        Course course = courseRepository.findByTitle(userCourseRequestDto.getCourseName());

        UserCourse userCourse = UserCourse.builder().completed(false)
                .progress(0.0).user(user).course(course).build();
        List<UserCourseItem> userCourseItems = new ArrayList<>();
        for(CourseItem courseItem: course.getCourseItems()) {
            userCourseItems.add(UserCourseItem.builder().courseItem(courseItem).isLearned(false).userCourse(userCourse).build());
        }

        userCourseRepository.save(userCourse);
        userCourseItemRepository.saveAll(userCourseItems);
    }

    public void deleteUserCourse(Long id) {
        if (!userCourseRepository.existsById(id)) {
            throw new CourseNotFoundException(id);
        }
        userCourseRepository.deleteById(id);
    }
}
