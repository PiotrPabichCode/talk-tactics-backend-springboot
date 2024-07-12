package com.example.talktactics.exception;

import org.springframework.util.StringUtils;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(Class clazz, String field, String val) {
        super(EntityNotFoundException.buildMessage(clazz.getSimpleName(), field, val));
    }

    private static String buildMessage(String entity, String field, String val) {
        return StringUtils.capitalize(entity)
                + " with " + field + " "+ val + " does not exist";
    }
}
