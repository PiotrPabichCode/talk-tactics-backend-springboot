package com.example.talktactics.services;


import com.example.talktactics.exceptions.TaskNotFoundException;
import com.example.talktactics.models.Answer;
import com.example.talktactics.models.Task;
import com.example.talktactics.repositories.AnswerRepository;
import com.example.talktactics.repositories.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final AnswerRepository answerRepository;

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }
    public List<Task> getTasks() {
        return taskRepository.findAll();
    }
    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
    }
    public List<Task> filterTasksByCourseName(String courseName) {
        return taskRepository.findByCourseName(courseName);
    }
    public Task updateTask(Long id, Task newTask) {
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
    public void deleteTask(Long id) {
        if(!taskRepository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }
        Optional<Task> task = taskRepository.findById(id);
        List<Answer> answers = answerRepository.findByTaskIn(task.stream().toList());
        answerRepository.deleteAll(answers);
        taskRepository.deleteById(id);
    }

}
