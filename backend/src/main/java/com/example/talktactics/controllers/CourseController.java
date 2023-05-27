package com.example.talktactics.controllers;

import com.example.talktactics.exceptions.CourseNotFoundException;
import com.example.talktactics.exceptions.TaskNotFoundException;
import com.example.talktactics.models.Answer;
import com.example.talktactics.models.Course;
import com.example.talktactics.models.Task;
import com.example.talktactics.repositories.AnswerRepository;
import com.example.talktactics.repositories.CourseRepository;
import com.example.talktactics.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CourseController {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private AnswerRepository answerRepository;

    @PostMapping("/course")
    Course newCourse(@RequestBody Course newCourse) {
        return courseRepository.save(newCourse);
    }

    @GetMapping("/courses")
    List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @GetMapping("/course/{id}")
    Course getCourseById(@PathVariable Long id) {
        return courseRepository.findById(id).orElseThrow(() -> new CourseNotFoundException(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/course/{id}")
    Course updateCourse(@RequestBody Course newCourse, @PathVariable Long id) {
        System.out.println(newCourse);
        return courseRepository.findById(id)
                .map(course -> {
                    course.setDescription(newCourse.getDescription());
                    course.setLevel(newCourse.getLevel());
                    course.setName(newCourse.getName());
                    return courseRepository.save(course);
                }).orElseThrow(() -> new CourseNotFoundException(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/course/{id}")
    String deleteCourse(@PathVariable Long id) {
        if (!courseRepository.existsById(id)) {
            throw new CourseNotFoundException(id);
        }

        Course course = courseRepository.findById(id).orElseThrow(() -> new CourseNotFoundException(id));

        List<Task> tasks = taskRepository.findByCourse(course);
        List<Answer> answers = answerRepository.findByTaskIn(tasks);
        answerRepository.deleteAll(answers);
        taskRepository.deleteAll(tasks);
        courseRepository.deleteById(id);

        return "Course with id " + id + " and associated tasks deleted.";
    }

    @GetMapping("/courses/level/{levelName}")
    public List<Course> getCoursesByLevelName(@PathVariable String levelName) {
        return courseRepository.findByLevelNameContaining(levelName);
    }
}
