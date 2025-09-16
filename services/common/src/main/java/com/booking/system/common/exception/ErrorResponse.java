package com.booking.system.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ErrorResponse {

    private final int code;
    private final String message;
    private final LocalDateTime timestamp;
}
