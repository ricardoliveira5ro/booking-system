package com.booking.system.common.exception;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(String code, String message, String details, List<String> stackTrace, LocalDateTime timestamp) {}
