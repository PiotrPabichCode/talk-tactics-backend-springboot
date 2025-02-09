package com.piotrpabich.talktactics.exception.handler;

import com.piotrpabich.talktactics.exception.EntityExistsException;
import com.piotrpabich.talktactics.exception.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiError> handleException(final Exception e){
        log.error(e.getMessage(), e);
        return buildResponseEntity(ApiError.error(e.getMessage()));
    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<ApiError> handleEntityNotFoundException(final EntityNotFoundException e) {
        log.error(e.getMessage(), e);
        return buildResponseEntity(ApiError.error(NOT_FOUND.value(),e.getMessage()));
    }

    @ExceptionHandler(value = EntityExistsException.class)
    public ResponseEntity<ApiError> entityExistException(final EntityExistsException e) {
        log.error(e.getMessage(), e);
        return buildResponseEntity(ApiError.error(e.getMessage()));
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e){
        log.error(e.getMessage(), e);
        ObjectError objectError = e.getBindingResult().getAllErrors().getFirst();
        String message = objectError.getDefaultMessage();
        if (objectError instanceof FieldError) {
            message = ((FieldError) objectError).getField() + ": " + message;
        }
        return buildResponseEntity(ApiError.error(message));
    }

    private ResponseEntity<ApiError> buildResponseEntity(final ApiError apiError) {
        return new ResponseEntity<>(apiError, HttpStatus.valueOf(apiError.getStatus()));
    }
}
