package com.example.talktactics.exception;

public class UserCourseExistsException extends RuntimeException{
    public UserCourseExistsException(String message) {
        super(message);
    }
}

