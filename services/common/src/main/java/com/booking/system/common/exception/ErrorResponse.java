package com.booking.system.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class ErrorResponse {

    private final String code;
    private final String message;
    private final String details;
    private final List<String> stackTrace;
    private final LocalDateTime timestamp;
}
