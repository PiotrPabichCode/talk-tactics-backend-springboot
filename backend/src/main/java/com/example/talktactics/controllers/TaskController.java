package com.example.talktactics.controllers;

import com.example.talktactics.exceptions.TaskNotFoundException;
import com.example.talktactics.models.Answer;
import com.example.talktactics.models.Task;
import com.example.talktactics.repositories.AnswerRepository;
import com.example.talktactics.repositories.TaskRepository;
import com.example.talktactics.services.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class TaskController {
    private final TaskService taskService;
    @PostMapping("/tasks")
    Task createTask(@RequestBody Task task) {
        return taskService.createTask(task);
    }

    @GetMapping("/tasks")
    List<Task> getAllTasks() {
        return taskService.getTasks();
    }

    @GetMapping("/tasks/{id}")
    Task getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }
    @GetMapping("/tasks/course/name/{courseName}")
    public List<Task> getTasksByCourseName(@PathVariable String courseName) {
        return taskService.filterTasksByCourseName(courseName);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/tasks/{id}")
    Task updateTask(@PathVariable Long id, @RequestBody Task newTask) {
        return taskService.updateTask(id, newTask);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/tasks/{id}")
    void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }
}
