package com.example.talktactics.controller;

import com.example.talktactics.dto.user_course.req.UserCourseGetReqDto;
import com.example.talktactics.dto.user_course.req.UserCourseDeleteReqDto;
import com.example.talktactics.dto.user_course.UserCoursePreviewDto;
import com.example.talktactics.dto.user_course.req.UserCourseAddReqDto;
import com.example.talktactics.entity.UserCourse;
import com.example.talktactics.exception.UserCourseRuntimeException;
import com.example.talktactics.exception.UserRuntimeException;
import com.example.talktactics.service.user_course.UserCourseService;
import com.example.talktactics.service.user.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/user-courses")
@CrossOrigin(origins = {"http://localhost:3000", "https://talk-tactics-frontend.vercel.app/"}, allowCredentials = "true")
@Tag(name = "User courses", description = "User courses management APIs")
public class UserCourseController {

    private final UserCourseService userCourseService;
    private final UserService userService;

    @GetMapping("/all")
    public ResponseEntity<List<UserCourse>> getAllUserCourses() {
        try {
            return ResponseEntity.ok(userCourseService.getAllUserCourses());
        } catch (UserCourseRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/preview/user-id/{id}")
    public ResponseEntity<List<UserCoursePreviewDto>> getUserCoursesPreviewByUserId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(userCourseService.getUserCoursesPreviewListByUserId(id));
        } catch (UserCourseRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<UserCourse> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(userCourseService.getById(id));
        } catch (UserRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/user-id/{id}")
    public ResponseEntity<List<UserCourse>> getAllByUserId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(userCourseService.getAllByUserId(id));
        } catch (UserRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<UserCourse> getByUserIdAndCourseId(@RequestBody UserCourseGetReqDto req) {
        try {
            return ResponseEntity.ok(userCourseService.getByUserIdAndCourseId(req));
        } catch (UserRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping
    public void addCourseToUser(@RequestBody UserCourseAddReqDto req) {
        try {
            userCourseService.addUserCourse(req);
        } catch (UserRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping
    void deleteUserCourse(@RequestBody UserCourseDeleteReqDto req) {
        try {
            userCourseService.deleteUserCourse(req);
        } catch (UserRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}