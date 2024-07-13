package com.piotrpabich.talktactics.controller;

import com.piotrpabich.talktactics.common.PageResult;
import com.piotrpabich.talktactics.dto.user_course.UserCourseDto;
import com.piotrpabich.talktactics.dto.user_course.UserCourseQueryCriteria;
import com.piotrpabich.talktactics.dto.user_course.req.UserCourseDeleteReqDto;
import com.piotrpabich.talktactics.dto.user_course.req.UserCourseAddReqDto;
import com.piotrpabich.talktactics.entity.User;
import com.piotrpabich.talktactics.entity.UserCourse;
import com.piotrpabich.talktactics.service.auth.AuthenticationService;
import com.piotrpabich.talktactics.service.user_course.UserCourseFacade;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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
    private final UserCourseFacade userCourseFacade;
    private final AuthenticationService authenticationService;

    @GetMapping("/all")
    public ResponseEntity<PageResult<UserCourseDto>> queryUserCourses(
            UserCourseQueryCriteria criteria,
            Pageable pageable,
            final HttpServletRequest request
    ) {
        User requester = authenticationService.getUserFromRequest(request);
        return ResponseEntity.ok(userCourseFacade.queryAll(criteria, pageable, requester));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<UserCourse> getById(
            @PathVariable Long id,
            final HttpServletRequest request
    ) {
        User requester = authenticationService.getUserFromRequest(request);
        return ResponseEntity.ok(userCourseFacade.getById(id, requester));
    }

    @PutMapping
    public ResponseEntity<Object> addCourseToUser(
            @RequestBody @Valid UserCourseAddReqDto addRequest,
            final HttpServletRequest request
    ) {
        User requester = authenticationService.getUserFromRequest(request);
        userCourseFacade.addUserCourse(addRequest, requester);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteUserCourse(
            @RequestBody @Valid UserCourseDeleteReqDto deleteRequest,
            final HttpServletRequest request
    ) {
        User requester = authenticationService.getUserFromRequest(request);
        userCourseFacade.deleteUserCourse(deleteRequest, requester);
        return ResponseEntity.ok().build();
    }
}