package com.piotrpabich.talktactics.course;

import com.piotrpabich.talktactics.course.dto.CourseDto;
import com.piotrpabich.talktactics.course.dto.CourseNavbarDto;
import com.piotrpabich.talktactics.course.dto.CourseQueryCriteria;
import com.piotrpabich.talktactics.course.entity.Course;
import com.piotrpabich.talktactics.auth.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
@Tag(name = "CourseController", description = "Courses management APIs")
public class CourseController {
    private final CourseService courseService;
    private final AuthenticationService authenticationService;

    @GetMapping
    public ResponseEntity<Page<CourseDto>> queryCourses(
            final CourseQueryCriteria criteria,
            final Pageable pageable
    ) {
        return ResponseEntity.ok(courseService.queryAll(criteria, pageable));
    }

    @GetMapping("/navbar")
    public ResponseEntity<List<CourseNavbarDto>> getNavbarList() {
        return ResponseEntity.ok(courseService.getNavbarList());
    }

    @PostMapping
    public ResponseEntity<Void> createCourse(
            @Validated @RequestBody final Course course,
            final HttpServletRequest request
    ) {
        final var requester = authenticationService.getUserFromRequest(request);
        courseService.create(course, requester);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @PutMapping
    public ResponseEntity<Void> updateCourse(
            @RequestBody final Course resources,
            final HttpServletRequest request
    ) {
        final var requester = authenticationService.getUserFromRequest(request);
        courseService.update(resources, requester);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteCourses(
            @RequestBody final Set<Long> ids,
            final HttpServletRequest request
    ) {
        final var requester = authenticationService.getUserFromRequest(request);
        courseService.delete(ids, requester);
        return ResponseEntity.ok().build();
    }
}
