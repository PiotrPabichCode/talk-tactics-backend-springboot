package com.example.talktactics.controllers;


import com.example.talktactics.models.Answer;
import com.example.talktactics.models.Course;
import com.example.talktactics.models.Task;
import com.example.talktactics.models.User;
import com.example.talktactics.repositories.AnswerRepository;
import com.example.talktactics.repositories.CourseRepository;
import com.example.talktactics.repositories.TaskRepository;
import com.example.talktactics.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@CrossOrigin("http://localhost:3000")
public class GetController {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final TaskRepository taskRepository;
    @Autowired
    private final CourseRepository courseRepository;
    @Autowired
    private final AnswerRepository answerRepository;

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/tasks")
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }
    @GetMapping("/courses")
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }
    @GetMapping("/answers")
    public List<Answer> getAllAnswers() {
        return answerRepository.findAll();
    }
}
