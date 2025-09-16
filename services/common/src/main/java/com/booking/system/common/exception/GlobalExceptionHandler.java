package com.booking.system.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "An unexpected error occurred", LocalDateTime.now()), HttpStatus.BAD_REQUEST);
    }
}
