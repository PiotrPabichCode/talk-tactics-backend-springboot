package com.example.talktactics.controller;

import com.example.talktactics.entity.*;
import com.example.talktactics.service.course.CourseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/courses")
@Tag(name = "Courses", description = "Courses management APIs")
public class CourseController {
    private final CourseService courseService;

    @PostMapping()
    Course createCourse(@RequestBody Course course) {
        return courseService.createCourse(course);
    }

    @GetMapping()
    List<Course> getAllCourses() {
        return courseService.getCourses();
    }

    @GetMapping("/{id}")
    Course getCourseById(@PathVariable Long id) {
        return courseService.getCourseById(id);
    }
    @GetMapping("/level/{level}")
    public List<Course> getCoursesByLevel(@PathVariable String level) {
        return courseService.filterCoursesByLevelName(level);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    Course updateCourse(@PathVariable Long id, @RequestBody Course newCourse) {
        return courseService.updateCourse(id, newCourse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    void deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
    }
}
