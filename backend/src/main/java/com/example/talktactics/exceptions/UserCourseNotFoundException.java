package com.example.talktactics.exceptions;

public class UserCourseNotFoundException extends RuntimeException{
    public UserCourseNotFoundException(Long id) {
        super("Couldn't find user course with id " + id);
    }
}
