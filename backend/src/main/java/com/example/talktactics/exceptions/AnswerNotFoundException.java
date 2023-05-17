package com.example.talktactics.exceptions;

public class AnswerNotFoundException extends RuntimeException{
    public AnswerNotFoundException(Long id) {
        super("Couldn't find answer with id " + id);
    }
}
