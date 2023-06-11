package com.example.talktactics.exceptions;

public class UserCourseNotFoundException extends RuntimeException{
    public UserCourseNotFoundException() {
        super();
    }
    public UserCourseNotFoundException(Long id) {
        super("Couldn't find user course with id " + id);
    }
}
