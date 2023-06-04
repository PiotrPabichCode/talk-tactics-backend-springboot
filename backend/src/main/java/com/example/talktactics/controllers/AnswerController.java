package com.example.talktactics.controllers;

import com.example.talktactics.models.Answer;
import com.example.talktactics.services.AnswerService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class AnswerController {
    private final AnswerService answerService;

    @GetMapping("/answers")
    List<Answer> getAllAnswers() {
        return answerService.getAnswers();
    }

    @GetMapping("/answers/{id}")
    Answer getAnswerById(@PathVariable Long id) {
        return answerService.getAnswerById(id);
    }
    @GetMapping("/answers/username/{username}")
    public List<Answer> getAnswersByUserName(@PathVariable String username) {
        return answerService.filterAnswersByUsername(username);
    }

    @PostMapping("/answers")
    Answer newAnswer(@RequestBody Answer answer) {
        return answerService.createAnswer(answer);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/answers/{id}")
    Answer updateAnswer(@RequestBody Answer answer, @PathVariable Long id) {
        return answerService.updateAnswer(answer, id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/answers/{id}")
    void deleteAnswer(@PathVariable Long id) {
        answerService.deleteAnswerById(id);
    }
}
