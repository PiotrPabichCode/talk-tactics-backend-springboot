package com.piotrpabich.talktactics.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends CustomHttpException {

    public BadRequestException(final String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
