package com.piotrpabich.talktactics.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@ResponseStatus(BAD_REQUEST)
public class BadRequestException extends RuntimeException {
    public BadRequestException(final String msg) {
        super(msg);
    }
}
