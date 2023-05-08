package com.example.talktactics.exceptions;

import java.util.HashMap;
import java.util.Map;

public class ValidationErrorDTO {
    private Map<String, String> fieldErrors;

    public ValidationErrorDTO() {
        this.fieldErrors = new HashMap<>();
    }

    public void addFieldError(String fieldName, String errorMessage) {
        fieldErrors.put(fieldName, errorMessage);
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }
}
