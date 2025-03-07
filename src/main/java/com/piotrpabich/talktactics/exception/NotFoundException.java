package com.piotrpabich.talktactics.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends CustomHttpException {

    public NotFoundException(final String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
