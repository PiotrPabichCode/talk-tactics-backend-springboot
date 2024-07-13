package com.piotrpabich.talktactics.controller;

import com.piotrpabich.talktactics.common.PageResult;
import com.piotrpabich.talktactics.dto.course.*;
import com.piotrpabich.talktactics.entity.*;
import com.piotrpabich.talktactics.service.course.CourseService;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @GetMapping("/all")
    public ResponseEntity<PageResult<CourseDto>> queryCourses(CourseQueryCriteria criteria, Pageable pageable) {
        return ResponseEntity.ok(courseService.queryAll(criteria, pageable));
    }

    @GetMapping("/navbar")
    public ResponseEntity<List<CourseNavbarDto>> getNavbarList() {
        return ResponseEntity.ok(courseService.getNavbarList());
    }

    @PostMapping
    public ResponseEntity<Object> createCourse(@Validated @RequestBody Course course) {
        courseService.create(course);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @PutMapping
    public ResponseEntity<Object> updateCourse(@RequestBody Course resources) {
        courseService.update(resources);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteCourses(@RequestBody Set<Long> ids) {
        courseService.delete(ids);
        return ResponseEntity.ok().build();
    }
}
