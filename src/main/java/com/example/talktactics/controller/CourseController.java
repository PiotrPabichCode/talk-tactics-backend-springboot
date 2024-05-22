package com.example.talktactics.controller;

import com.example.talktactics.common.PageResult;
import com.example.talktactics.dto.course.*;
import com.example.talktactics.entity.*;
import com.example.talktactics.exception.CourseRuntimeException;
import com.example.talktactics.service.course.CourseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/courses")
@CrossOrigin(origins = {"http://localhost:3000", "https://talk-tactics-frontend.vercel.app/"}, allowCredentials = "true")
@Tag(name = "Courses", description = "Courses management APIs")
public class CourseController {
    private final CourseService courseService;

    @GetMapping("/all")
    public ResponseEntity<PageResult<CourseDto>> queryCourses(CourseQueryCriteria criteria, Pageable pageable) {
        try {
            return new ResponseEntity<>(courseService.queryAll(criteria, pageable), HttpStatus.OK);
        } catch(CourseRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/navbar")
    public ResponseEntity<List<CourseNavbarDto>> getNavbarList() {
        try {
            return new ResponseEntity<>(courseService.getNavbarList(), HttpStatus.OK);
        } catch(CourseRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createCourse(@RequestBody Course course) {
        try {
            courseService.create(course);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch(CourseRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Course> getById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(courseService.getById(id), HttpStatus.OK);
        } catch(CourseRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
    @GetMapping("/all/preview")
    public ResponseEntity<List<CoursePreviewProjection>> getPreviewList() {
        try {
            return new ResponseEntity<>(courseService.getPreviewList(), HttpStatus.OK);
        } catch(CourseRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
    @PutMapping("/id/{id}")
    public ResponseEntity<Object> updateCourse(@PathVariable Long id, @RequestBody Course newCourse) {
        try {
            courseService.update(id, newCourse);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch(CourseRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<Object> deleteCourse(@PathVariable Long id) {
        try {
            courseService.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch(CourseRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
