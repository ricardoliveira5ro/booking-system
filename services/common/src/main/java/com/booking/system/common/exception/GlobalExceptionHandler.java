package com.booking.system.common.exception;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        String details = null;
        List<String> stackTrace = null;

        if ("dev".equalsIgnoreCase(activeProfile)) {
            details = ex.getMessage();
            stackTrace = Arrays.stream(ex.getStackTrace())
                    .map(StackTraceElement::toString)
                    .toList();
        }

        return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "An unexpected error occurred", details, stackTrace, LocalDateTime.now()), HttpStatus.BAD_REQUEST);
    }
}
