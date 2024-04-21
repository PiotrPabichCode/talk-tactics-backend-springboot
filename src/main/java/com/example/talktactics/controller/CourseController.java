package com.example.talktactics.controller;

import com.example.talktactics.dto.course.CourseFilterDto;
import com.example.talktactics.dto.course.CourseNavbarDto;
import com.example.talktactics.dto.course.CoursePreviewProjection;
import com.example.talktactics.entity.*;
import com.example.talktactics.exception.CourseRuntimeException;
import com.example.talktactics.service.course.CourseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<Page<Course>> getCourseList(@RequestParam(value = "page") int page,
                                                      @RequestParam(value = "size") int size,
                                                      CourseFilterDto filters) {
        try {
            return ResponseEntity.ok(courseService.getCourseList(page, size, filters));
        } catch(CourseRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/navbar")
    public ResponseEntity<List<CourseNavbarDto>> getNavbarList() {
        try {
            return ResponseEntity.ok(courseService.getNavbarList());
        } catch(CourseRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Course> create(@RequestBody Course course) {
        try {
            return ResponseEntity.ok(courseService.create(course));
        } catch(CourseRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Course> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(courseService.getById(id));
        } catch(CourseRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
    @GetMapping("/all/preview")
    public ResponseEntity<List<CoursePreviewProjection>> getPreviewList() {
        try {
            return ResponseEntity.ok(courseService.getPreviewList());
        } catch(CourseRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
    @PutMapping("/id/{id}")
    public ResponseEntity<Course> update(@PathVariable Long id, @RequestBody Course newCourse) {
        try {
            return ResponseEntity.ok(courseService.update(id, newCourse));
        } catch(CourseRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/id/{id}")
    public void delete(@PathVariable Long id) {
        try {
            courseService.delete(id);
        } catch(CourseRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
