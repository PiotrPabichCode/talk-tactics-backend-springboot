package com.example.talktactics.controllers;

import com.example.talktactics.exceptions.AnswerNotFoundException;
import com.example.talktactics.exceptions.TaskNotFoundException;
import com.example.talktactics.models.Answer;
import com.example.talktactics.models.Task;
import com.example.talktactics.repositories.AnswerRepository;
import com.example.talktactics.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AnswerController {
    @Autowired
    private AnswerRepository answerRepository;

    @PostMapping("/answer")
    Answer newAnswer(@RequestBody Answer newAnswer) {
        return answerRepository.save(newAnswer);
    }

    @GetMapping("/answers")
    List<Answer> getAllAnswers() {
        return answerRepository.findAll();
    }

    @GetMapping("/answer/{id}")
    Answer getAnswerById(@PathVariable Long id) {
        return answerRepository.findById(id).orElseThrow(() -> new AnswerNotFoundException(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/answer/{id}")
    Answer updateAnswer(@RequestBody Answer newAnswer, @PathVariable Long id) {
        return answerRepository.findById(id)
                .map(answer -> {
                    answer.setContent(newAnswer.getContent());
                    answer.setFinishTime(newAnswer.getFinishTime());
                    answer.setTask(newAnswer.getTask());
                    answer.setUser(newAnswer.getUser());
                    return answerRepository.save(answer);
                }).orElseThrow(() -> new AnswerNotFoundException(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/answer/{id}")
    String deleteAnswer(@PathVariable Long id) {
        if(!answerRepository.existsById(id)) {
            throw new AnswerNotFoundException(id);
        }
        answerRepository.deleteById(id);
        return "Answer with id " + id + " deleted.";
    }

    @GetMapping("/answers/username/{name}")
    public List<Answer> getAnswersByUserName(@PathVariable String name) {
        return answerRepository.findByUserNameContaining(name);
    }
}
