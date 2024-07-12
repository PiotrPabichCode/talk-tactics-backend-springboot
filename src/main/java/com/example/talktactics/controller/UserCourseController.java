package com.example.talktactics.controller;

import com.example.talktactics.common.PageResult;
import com.example.talktactics.dto.user_course.UserCourseDto;
import com.example.talktactics.dto.user_course.UserCourseQueryCriteria;
import com.example.talktactics.dto.user_course.req.UserCourseDeleteReqDto;
import com.example.talktactics.dto.user_course.req.UserCourseAddReqDto;
import com.example.talktactics.entity.UserCourse;
import com.example.talktactics.service.user_course.UserCourseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/user-courses")
@CrossOrigin(origins = {"http://localhost:3000", "https://talk-tactics-frontend.vercel.app/"}, allowCredentials = "true")
@Tag(name = "User courses", description = "User courses management APIs")
public class UserCourseController {

    private final UserCourseService userCourseService;

    @GetMapping("/all")
    public ResponseEntity<PageResult<UserCourseDto>> queryUserCourses(UserCourseQueryCriteria criteria, Pageable pageable) {
        return ResponseEntity.ok(userCourseService.queryAll(criteria, pageable));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<UserCourse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userCourseService.getById(id));
    }

    @PutMapping
    public ResponseEntity<Object> addCourseToUser(@RequestBody UserCourseAddReqDto req) {
        userCourseService.addUserCourse(req);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteUserCourse(@RequestBody UserCourseDeleteReqDto req) {
        userCourseService.deleteUserCourse(req);
        return ResponseEntity.ok().build();
    }
}