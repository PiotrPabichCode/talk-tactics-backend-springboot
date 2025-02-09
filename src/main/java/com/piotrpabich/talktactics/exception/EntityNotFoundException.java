package com.piotrpabich.talktactics.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.util.StringUtils.capitalize;

@Slf4j
@ResponseStatus(NOT_FOUND)
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(Class clazz, String field, String val) {
        super(buildMessage(clazz.getSimpleName(), field, val));
    }

    private static String buildMessage(String entity, String field, String val) {
        return capitalize(entity) + " with " + field + " " + val + " does not exist";
    }
}
