package com.example.talktactics.controllers;

import com.example.talktactics.exceptions.CourseNotFoundException;
import com.example.talktactics.models.Answer;
import com.example.talktactics.models.Course;
import com.example.talktactics.models.Task;
import com.example.talktactics.repositories.AnswerRepository;
import com.example.talktactics.repositories.CourseRepository;
import com.example.talktactics.repositories.TaskRepository;
import com.example.talktactics.services.CourseService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class CourseController {
    private final CourseService courseService;

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
}
