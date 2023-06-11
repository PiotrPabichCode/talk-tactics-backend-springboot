package com.example.talktactics.services;

import com.example.talktactics.DTOs.UserCourseRequest;
import com.example.talktactics.exceptions.CourseNotFoundException;
import com.example.talktactics.exceptions.UserCourseExistsException;
import com.example.talktactics.exceptions.UserCourseNotFoundException;
import com.example.talktactics.exceptions.UserNotFoundException;
import com.example.talktactics.models.*;
import com.example.talktactics.repositories.CourseRepository;
import com.example.talktactics.repositories.UserCourseItemRepository;
import com.example.talktactics.repositories.UserCourseRepository;
import com.example.talktactics.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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
    public List<UserCourse> getAllUserCoursesByLogin(String login) {
        User user = userRepository.findByLogin(login).orElseThrow(() -> new UserNotFoundException("User %s not found".formatted(login)));
        return user.getUserCourses();
    }

    public UserCourse getByCourseNameAndLogin(String name, String login) {
        return userCourseRepository.findByCourseNameAndUserLogin(name, login);
    }

    public UserCourse getById(Long id) {
        return userCourseRepository.findById(id).orElseThrow(() -> new UserCourseNotFoundException(id));
    }

    public void addUserCourse(UserCourseRequest userCourseRequest, User user) {
        if (userCourseRepository.existsByCourseNameAndUserLogin(userCourseRequest.getCourseName(), userCourseRequest.getLogin())) {
            throw new UserCourseExistsException("UserCourse already exists");
        }
        // find course
        Course course = courseRepository.findByName(userCourseRequest.getCourseName());

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
