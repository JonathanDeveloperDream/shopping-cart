package com.tecnoin.customer_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

// Marking as exception controller
@ControllerAdvice
public class HandlerExceptionController {

    // Catching the exception
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException exception) {

        // Creating map to return the error
        Map<String, Object> errors = new HashMap<>();

        // Getting the error message
        exception.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put("message", error.getDefaultMessage());
        });

        // Returning the error with status 400 BAD_REQUEST
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException ex) {

        // Creating map to return the error
        Map<String, Object> body = new HashMap<>();

        // Getting the error message
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());
        body.put("status", HttpStatus.BAD_REQUEST.value());

        // Returning the error with status 400 BAD_REQUEST
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneralException(Exception ex) {

        // Creating map to return the error
        Map<String, Object> body = new HashMap<>();

        // Getting the error message
        body.put("timestamp", LocalDateTime.now());
        body.put("message", "Internal server error");
        body.put("error", ex.getMessage());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());

        // Returning the error with status 500 INTERNAL_SERVER_ERROR
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}