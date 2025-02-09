package com.piotrpabich.talktactics.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@Slf4j
@ResponseStatus(FORBIDDEN)
public class ForbiddenException extends RuntimeException {
    public ForbiddenException(final String message) {
        super(message);
        log.warn(message);
    }
}
