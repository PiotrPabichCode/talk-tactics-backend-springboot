package com.example.talktactics.controllers;

import com.example.talktactics.DTOs.UserCourseRequest;
import com.example.talktactics.exceptions.UserNotFoundException;
import com.example.talktactics.models.User;
import com.example.talktactics.models.UserCourse;
import com.example.talktactics.services.UserCourseService;
import com.example.talktactics.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class UserCourseController {

    private final UserCourseService userCourseService;
    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user-courses")
    public List<UserCourse> getAllUserCourses() {
        return userCourseService.getAllUserCourses();
    }

    @GetMapping("/user-courses/users/{userID}")
    public List<UserCourse> getAllUserCoursesByUserId(@PathVariable Long userID) {
        System.out.println(userID);
        return userCourseService.getAllCoursesByUserId(userID);
    }

    @GetMapping("/user-courses/users/login/{login}")
    public List<UserCourse> getUserCoursesByLogin(@PathVariable String login) {
        return userCourseService.getAllUserCoursesByLogin(login);
    }

    @GetMapping("/user-courses/user-course-request")
    public UserCourse getUserCourseByCourseNameAndLogin(@RequestBody UserCourseRequest userCourseRequest) {
        return userCourseService.getByCourseNameAndLogin(userCourseRequest.getCourseName(), userCourseRequest.getLogin());
    }

    @GetMapping("/user-courses/{id}")
    public UserCourse getById(@PathVariable Long id) {
        return userCourseService.getById(id);
    }

    @PutMapping("/user-courses")
    public void addCourseToUser(@RequestBody UserCourseRequest userCourseRequest) {
        User user = userService.getUserByLogin(userCourseRequest.getLogin())
                .orElseThrow(() -> new UserNotFoundException("User %s not found".formatted(userCourseRequest.getLogin())));
        userCourseService.addUserCourse(userCourseRequest, user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/user-courses/{id}")
    void deleteUserCourse(@PathVariable Long id) {
        userCourseService.deleteUserCourse(id);
    }
}
