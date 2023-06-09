package com.example.talktactics.controllers;

import com.example.talktactics.models.*;
import com.example.talktactics.repositories.*;
import com.example.talktactics.services.CourseService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class CourseController {
    private final CourseService courseService;
    private final CourseItemRepository courseItemRepository;
    private final MeaningRepository meaningRepository;

    @PostMapping("/courses")
    Course createCourse(@RequestBody Course course) {
        return courseService.createCourse(course);
    }

    @GetMapping("/courses")
    List<Course> getAllCourses() {
        return courseService.getCourses();
    }

    @GetMapping("/courses/{id}")
    Course getCourseById(@PathVariable Long id) {
        return courseService.getCourseById(id);
    }
    @GetMapping("/courses/level/{levelName}")
    public List<Course> getCoursesByLevelName(@PathVariable String levelName) {
        return courseService.filterCoursesByLevelName(levelName);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/courses/{id}")
    Course updateCourse(@PathVariable Long id, @RequestBody Course newCourse) {
        return courseService.updateCourse(id, newCourse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/courses/{id}")
    void deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
    }

    @GetMapping("/course-items")
    List<CourseItem> getCourseItems() {
        return courseItemRepository.findAll();
    }

    @GetMapping("/course-items/{id}")
    Optional<CourseItem> getCourseItemDetailsById(@PathVariable Long id) {
        return courseItemRepository.findById(id);
    }

    @GetMapping("/meanings")
    List<Meaning> getMeanings() {
        return meaningRepository.findAll();
    }
}
