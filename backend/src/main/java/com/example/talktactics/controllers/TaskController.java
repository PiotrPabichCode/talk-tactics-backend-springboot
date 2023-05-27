package com.example.talktactics.controllers;

import com.example.talktactics.exceptions.TaskNotFoundException;
import com.example.talktactics.models.Answer;
import com.example.talktactics.models.Task;
import com.example.talktactics.repositories.AnswerRepository;
import com.example.talktactics.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class TaskController {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private AnswerRepository answerRepository;

    @PostMapping("/task")
    Task newTask(@RequestBody Task newTask) {
        return taskRepository.save(newTask);
    }

    @GetMapping("/tasks")
    List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @GetMapping("/task/{id}")
    Task getTaskById(@PathVariable Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/task/{id}")
    Task updateTask(@RequestBody Task newTask, @PathVariable Long id) {
        System.out.println(newTask);
        return taskRepository.findById(id)
                .map(task -> {
                    task.setCourse(newTask.getCourse());
                    task.setDescription(newTask.getDescription());
                    task.setName(newTask.getName());
                    task.setWord(newTask.getWord());
                    task.setPartOfSpeech(newTask.getPartOfSpeech());
                    return taskRepository.save(task);
                }).orElseThrow(() -> new TaskNotFoundException(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/task/{id}")
    String deleteTask(@PathVariable Long id) {
        if(!taskRepository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }
        Optional<Task> task = taskRepository.findById(id);
        List<Answer> answers = answerRepository.findByTaskIn(task.stream().toList());
        answerRepository.deleteAll(answers);
        taskRepository.deleteById(id);
        return "Task with id " + id + " deleted.";
    }

    @GetMapping("/tasks/course/name/{courseName}")
    public List<Task> getTasksByCourseName(@PathVariable String courseName) {
        return taskRepository.findByCourseNameContaining(courseName);
    }
}
