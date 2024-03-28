package com.example.talktactics.controller;

import com.example.talktactics.dto.user_course.UserCourseGetDto;
import com.example.talktactics.dto.user_course.UserCourseDeleteDto;
import com.example.talktactics.dto.user_course.UserCoursePreviewDto;
import com.example.talktactics.dto.user_course.UserCourseRequestDto;
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
@CrossOrigin(origins = {"http://localhost:3000", "https://talk-tactics-frontend.vercel.app/"})
@Tag(name = "User courses", description = "User courses management APIs")
public class UserCourseController {

    private final UserCourseService userCourseService;
    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    public List<UserCourse> getAllUserCourses() {
        return userCourseService.getAllUserCourses();
    }

    @GetMapping("/preview/user-id/{userID}")
    public List<UserCoursePreviewDto> getUserCoursesPreviewByUserId(@PathVariable Long userID) {
        return userCourseService.getUserCoursesPreviewListByUserId(userID);
    }

    @GetMapping("/username/{username}")
    public List<UserCourse> getUserCoursesByUsername(@PathVariable String username) {
        return userCourseService.getAllUserCoursesByUsername(username);
    }

    @PostMapping()
    public UserCourse getByUserIdAndCourseId(@RequestBody UserCourseGetDto userCourseGetDto) {
        return userCourseService.getByUserIdAndCourseId(userCourseGetDto);
    }

    @GetMapping("/{id}")
    public UserCourse getById(@PathVariable Long id) {
        return userCourseService.getById(id);
    }

    @PutMapping()
    public void addCourseToUser(@RequestBody UserCourseRequestDto userCourseRequestDto) {
        userCourseService.addUserCourse(userCourseRequestDto);
    }

    @DeleteMapping()
    void deleteUserCourse(@RequestBody UserCourseDeleteDto userCourseDeleteDto) {
        userCourseService.deleteUserCourse(userCourseDeleteDto);
    }
}
