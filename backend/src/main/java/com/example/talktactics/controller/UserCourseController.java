package com.example.talktactics.controller;

import com.example.talktactics.dto.user_course.UserCourseRequestDto;
import com.example.talktactics.exception.UserNotFoundException;
import com.example.talktactics.entity.User;
import com.example.talktactics.entity.UserCourse;
import com.example.talktactics.service.user_course.UserCourseService;
import com.example.talktactics.service.user.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/user-courses")
@Tag(name = "User courses", description = "User courses management APIs")
public class UserCourseController {

    private final UserCourseService userCourseService;
    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    public List<UserCourse> getAllUserCourses() {
        return userCourseService.getAllUserCourses();
    }

    @GetMapping("/users/{userID}")
    public List<UserCourse> getAllUserCoursesByUserId(@PathVariable Long userID) {
        return userCourseService.getAllCoursesByUserId(userID);
    }

    @GetMapping("/users/login/{login}")
    public List<UserCourse> getUserCoursesByLogin(@PathVariable String login) {
        return userCourseService.getAllUserCoursesByLogin(login);
    }

    @GetMapping("/user-course-request")
    public UserCourse getUserCourseByCourseNameAndLogin(@RequestBody UserCourseRequestDto userCourseRequestDto) {
        return userCourseService.getByCourseNameAndLogin(userCourseRequestDto.getCourseName(), userCourseRequestDto.getLogin());
    }

    @GetMapping("/{id}")
    public UserCourse getById(@PathVariable Long id) {
        return userCourseService.getById(id);
    }

    @PutMapping("")
    public void addCourseToUser(@RequestBody UserCourseRequestDto userCourseRequestDto) {
        User user = userService.getUserByLogin(userCourseRequestDto.getLogin())
                .orElseThrow(() -> new UserNotFoundException("User %s not found".formatted(userCourseRequestDto.getLogin())));
        userCourseService.addUserCourse(userCourseRequestDto, user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    void deleteUserCourse(@PathVariable Long id) {
        userCourseService.deleteUserCourse(id);
    }
}
