package com.piotrpabich.talktactics.exception;

import org.springframework.http.HttpStatus;

public abstract class UnauthorizedException extends CustomHttpException {

    public UnauthorizedException(final String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
