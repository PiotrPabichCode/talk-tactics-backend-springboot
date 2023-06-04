package com.example.talktactics.services;

import com.example.talktactics.exceptions.AnswerNotFoundException;
import com.example.talktactics.models.Answer;
import com.example.talktactics.repositories.AnswerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;

    public Answer createAnswer(Answer answer) {
        return answerRepository.save(answer);
    }

    public List<Answer> getAnswers() {
        return answerRepository.findAll();
    }

    public Answer getAnswerById(Long id) {
        return answerRepository.findById(id).orElseThrow(() -> new AnswerNotFoundException(id));
    }
    public List<Answer> filterAnswersByUsername(String username) {
        return answerRepository.findByUsername(username);
    }

    public Answer updateAnswer(Answer newAnswer, Long id) {
        return answerRepository.findById(id)
                .map(answer -> {
                    answer.setContent(newAnswer.getContent());
                    answer.setFinishTime(newAnswer.getFinishTime());
                    answer.setTask(newAnswer.getTask());
                    answer.setUser(newAnswer.getUser());
                    return answerRepository.save(answer);
                }).orElseThrow(() -> new AnswerNotFoundException(id));
    }

    public void deleteAnswerById(Long id) {
        if(!answerRepository.existsById(id)) {
            throw new AnswerNotFoundException(id);
        }
        answerRepository.deleteById(id);
    }
}
