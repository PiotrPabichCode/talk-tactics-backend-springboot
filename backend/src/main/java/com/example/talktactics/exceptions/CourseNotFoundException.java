package com.example.talktactics.exceptions;

public class CourseNotFoundException extends RuntimeException{
    public CourseNotFoundException(Long id) {
        super("Couldn't find course with id " + id);
    }
}
