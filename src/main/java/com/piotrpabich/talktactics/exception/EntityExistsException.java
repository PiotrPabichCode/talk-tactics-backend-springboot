package com.piotrpabich.talktactics.exception;

import static org.springframework.util.StringUtils.capitalize;

public class EntityExistsException extends RuntimeException {

    public EntityExistsException(Class clazz, String field, String val) {
        super(EntityExistsException.buildMessage(clazz.getSimpleName(), field, val));
    }

    private static String buildMessage(String entity, String field, String val) {
        return capitalize(entity) + " with " + field + " " + val + " exists";
    }
}
