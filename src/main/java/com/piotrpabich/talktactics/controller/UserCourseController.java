package com.piotrpabich.talktactics.controller;

import com.piotrpabich.talktactics.common.PageResult;
import com.piotrpabich.talktactics.dto.user_course.UserCourseDto;
import com.piotrpabich.talktactics.dto.user_course.UserCourseQueryCriteria;
import com.piotrpabich.talktactics.dto.user_course.req.UserCourseDeleteReqDto;
import com.piotrpabich.talktactics.dto.user_course.req.UserCourseAddReqDto;
import com.piotrpabich.talktactics.entity.UserCourse;
import com.piotrpabich.talktactics.service.user_course.UserCourseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.piotrpabich.talktactics.common.AppConst.API_V1;
import static com.piotrpabich.talktactics.common.AppConst.USER_COURSES_PATH;

@RestController
@AllArgsConstructor
@RequestMapping(API_V1 + USER_COURSES_PATH)
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