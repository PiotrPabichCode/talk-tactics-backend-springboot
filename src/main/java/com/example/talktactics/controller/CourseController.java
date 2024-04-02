package com.example.talktactics.controller;

import com.example.talktactics.dto.course.CoursePreviewProjection;
import com.example.talktactics.entity.*;
import com.example.talktactics.exception.CourseRuntimeException;
import com.example.talktactics.service.course.CourseServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/courses")
@CrossOrigin(origins = {"http://localhost:3000", "https://talk-tactics-frontend.vercel.app/"}, allowCredentials = "true")
@Tag(name = "Courses", description = "Courses management APIs")
public class CourseController {
    private final CourseServiceImpl courseServiceImpl;

    @PostMapping("/create")
    public ResponseEntity<Course> create(@RequestBody Course course) {
        try {
            return ResponseEntity.ok(courseServiceImpl.create(course));
        } catch(CourseRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Course> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(courseServiceImpl.getById(id));
        } catch(CourseRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
    @GetMapping("/all/preview")
    public ResponseEntity<List<CoursePreviewProjection>> getPreviewList() {
        try {
            return ResponseEntity.ok(courseServiceImpl.getPreviewList());
        } catch(CourseRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
    @GetMapping("/level/{level}")
    public ResponseEntity<List<Course>> getCoursesByLevel(@PathVariable String level) {
        try {
            return ResponseEntity.ok(courseServiceImpl.filterByLevel(level));
        } catch (CourseRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
    @PutMapping("/id/{id}")
    public ResponseEntity<Course> update(@PathVariable Long id, @RequestBody Course newCourse) {
        try {
            return ResponseEntity.ok(courseServiceImpl.update(id, newCourse));
        } catch(CourseRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/id/{id}")
    public void delete(@PathVariable Long id) {
        try {
            courseServiceImpl.delete(id);
        } catch(CourseRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
