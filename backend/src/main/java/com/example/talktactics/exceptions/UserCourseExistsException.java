package com.example.talktactics.exceptions;

public class UserCourseExistsException extends RuntimeException{
    public UserCourseExistsException(String message) {
        super(message);
    }
}

