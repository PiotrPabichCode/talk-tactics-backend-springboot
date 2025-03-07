package com.piotrpabich.talktactics.course;

import com.piotrpabich.talktactics.auth.AuthenticationService;
import com.piotrpabich.talktactics.common.UuidResponse;
import com.piotrpabich.talktactics.course.dto.CourseDto;
import com.piotrpabich.talktactics.course.dto.CourseNavbarDto;
import com.piotrpabich.talktactics.course.dto.CourseQueryCriteria;
import com.piotrpabich.talktactics.course.dto.CourseRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.piotrpabich.talktactics.common.AppConst.API_V1;
import static com.piotrpabich.talktactics.common.AppConst.COURSES_PATH;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_V1 + COURSES_PATH)
@Tag(name = "CourseController")
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
    public ResponseEntity<UuidResponse> createCourse(
            @RequestBody @Valid final CourseRequest courseRequest,
            final HttpServletRequest request
    ) {
        final var requester = authenticationService.getUserFromRequest(request);
        final var response = courseService.create(courseRequest, requester);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{courseUuid}")
    public ResponseEntity<Void> updateCourse(
            @PathVariable final UUID courseUuid,
            @RequestBody final CourseRequest updateRequest,
            final HttpServletRequest request
    ) {
        final var requester = authenticationService.getUserFromRequest(request);
        courseService.update(courseUuid, updateRequest, requester);
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping("/{courseUuid}")
    public ResponseEntity<Void> deleteCourse(
            @PathVariable final UUID courseUuid,
            final HttpServletRequest request
    ) {
        final var requester = authenticationService.getUserFromRequest(request);
        courseService.delete(courseUuid, requester);
        return ResponseEntity.noContent().build();
    }
}
