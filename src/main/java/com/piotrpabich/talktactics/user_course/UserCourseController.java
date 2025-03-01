package com.piotrpabich.talktactics.user_course;

import com.piotrpabich.talktactics.user_course.dto.UserCourseDto;
import com.piotrpabich.talktactics.user_course.dto.UserCourseQueryCriteria;
import com.piotrpabich.talktactics.user_course.dto.UserCourseRequest;
import com.piotrpabich.talktactics.user_course.entity.UserCourse;
import com.piotrpabich.talktactics.auth.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.piotrpabich.talktactics.common.AppConst.API_V1;
import static com.piotrpabich.talktactics.common.AppConst.USER_COURSES_PATH;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_V1 + USER_COURSES_PATH)
@Tag(name = "UserCourseController", description = "User courses management APIs")
public class UserCourseController {

    private final UserCourseFacade userCourseFacade;
    private final AuthenticationService authenticationService;

    @GetMapping
    public ResponseEntity<Page<UserCourseDto>> queryUserCourses(
            final UserCourseQueryCriteria criteria,
            final Pageable pageable,
            final HttpServletRequest request
    ) {
        final var requester = authenticationService.getUserFromRequest(request);
        return ResponseEntity.ok(userCourseFacade.queryAll(criteria, pageable, requester));
    }

    @GetMapping("/{userCourseUuid}")
    public ResponseEntity<UserCourse> getUserCourseByUuid(
            @PathVariable final UUID userCourseUuid,
            final HttpServletRequest request
    ) {
        final var requester = authenticationService.getUserFromRequest(request);
        return ResponseEntity.ok(userCourseFacade.getUserCourseByUuid(userCourseUuid, requester));
    }

    @PutMapping
    public ResponseEntity<Void> assignCourseToUser(
            @RequestBody @Valid final UserCourseRequest addRequest,
            final HttpServletRequest request
    ) {
        final var requester = authenticationService.getUserFromRequest(request);
        userCourseFacade.assignUserCourse(addRequest, requester);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUserCourse(
            @RequestBody @Valid final UserCourseRequest deleteRequest,
            final HttpServletRequest request
    ) {
        final var requester = authenticationService.getUserFromRequest(request);
        userCourseFacade.deleteUserCourse(deleteRequest, requester);
        return ResponseEntity.noContent().build();
    }
}