package com.piotrpabich.talktactics.user_course;

import com.piotrpabich.talktactics.common.PageResult;
import com.piotrpabich.talktactics.user_course.dto.UserCourseDto;
import com.piotrpabich.talktactics.user_course.dto.UserCourseQueryCriteria;
import com.piotrpabich.talktactics.user_course.dto.req.UserCourseDeleteRequest;
import com.piotrpabich.talktactics.user_course.dto.req.UserCourseAddRequest;
import com.piotrpabich.talktactics.user_course.entity.UserCourse;
import com.piotrpabich.talktactics.auth.AuthenticationService;
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
@Tag(name = "UserCourseController", description = "User courses management APIs")
public class UserCourseController {

    private final UserCourseFacade userCourseFacade;
    private final AuthenticationService authenticationService;

    @GetMapping("/all")
    public ResponseEntity<PageResult<UserCourseDto>> queryUserCourses(
            final UserCourseQueryCriteria criteria,
            final Pageable pageable,
            final HttpServletRequest request
    ) {
        final var requester = authenticationService.getUserFromRequest(request);
        return ResponseEntity.ok(userCourseFacade.queryAll(criteria, pageable, requester));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<UserCourse> getById(
            @PathVariable final Long id,
            final HttpServletRequest request
    ) {
        final var requester = authenticationService.getUserFromRequest(request);
        return ResponseEntity.ok(userCourseFacade.getById(id, requester));
    }

    @PutMapping
    public ResponseEntity<Void> addCourseToUser(
            @RequestBody @Valid final UserCourseAddRequest addRequest,
            final HttpServletRequest request
    ) {
        final var requester = authenticationService.getUserFromRequest(request);
        userCourseFacade.addUserCourse(addRequest, requester);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUserCourse(
            @RequestBody @Valid final UserCourseDeleteRequest deleteRequest,
            final HttpServletRequest request
    ) {
        final var requester = authenticationService.getUserFromRequest(request);
        userCourseFacade.deleteUserCourse(deleteRequest, requester);
        return ResponseEntity.ok().build();
    }
}