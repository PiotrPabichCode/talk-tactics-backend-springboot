package com.piotrpabich.talktactics.controller;

import com.piotrpabich.talktactics.common.PageResult;
import com.piotrpabich.talktactics.dto.course.*;
import com.piotrpabich.talktactics.entity.*;
import com.piotrpabich.talktactics.service.auth.AuthenticationService;
import com.piotrpabich.talktactics.service.course.CourseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

import static com.piotrpabich.talktactics.common.AppConst.API_V1;
import static com.piotrpabich.talktactics.common.AppConst.COURSES_PATH;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_V1 + COURSES_PATH)
@Tag(name = "Courses", description = "Courses management APIs")
public class CourseController {
    private final CourseService courseService;
    private final AuthenticationService authenticationService;

    @GetMapping("/all")
    public ResponseEntity<PageResult<CourseDto>> queryCourses(
            CourseQueryCriteria criteria,
            Pageable pageable
    ) {
        return ResponseEntity.ok(courseService.queryAll(criteria, pageable));
    }

    @GetMapping("/navbar")
    public ResponseEntity<List<CourseNavbarDto>> getNavbarList() {
        return ResponseEntity.ok(courseService.getNavbarList());
    }

    @PostMapping
    public ResponseEntity<Object> createCourse(
            @Validated @RequestBody Course course,
            HttpServletRequest request
    ) {
        User requester = authenticationService.getUserFromRequest(request);
        courseService.create(course, requester);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @PutMapping
    public ResponseEntity<Object> updateCourse(
            @RequestBody Course resources,
            HttpServletRequest request
    ) {
        User requester = authenticationService.getUserFromRequest(request);
        courseService.update(resources, requester);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteCourses(
            @RequestBody Set<Long> ids,
            HttpServletRequest request
    ) {
        User requester = authenticationService.getUserFromRequest(request);
        courseService.delete(ids, requester);
        return ResponseEntity.ok().build();
    }
}
