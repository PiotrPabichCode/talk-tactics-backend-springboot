package com.example.talktactics.exception;

public class UserCourseNotFoundException extends RuntimeException{
    public UserCourseNotFoundException(Long id) {
        super("Couldn't find user course with id " + id);
    }
}
