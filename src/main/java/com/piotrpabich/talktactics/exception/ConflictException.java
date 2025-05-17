package com.piotrpabich.talktactics.exception;

import org.springframework.http.HttpStatus;

public class ConflictException extends CustomHttpException {

    public ConflictException(final String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
