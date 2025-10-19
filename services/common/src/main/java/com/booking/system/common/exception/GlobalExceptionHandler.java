package com.booking.system.common.exception;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @Value("${spring.profiles.active}")
    private String activeProfile;

    protected String details = null;
    protected List<String> stackTrace = null;

    @ExceptionHandler(AppointmentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAppointmentNotFoundException(AppointmentNotFoundException ex) {
        setDetailsAndStackTrace(ex);

        return new ResponseEntity<>(new ErrorResponse("APPOINTMENT_NOT_FOUND", "Appointment does not exist or already cancelled", details, stackTrace, LocalDateTime.now()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AlreadyBookingException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyBookingException(AlreadyBookingException ex) {
        setDetailsAndStackTrace(ex);

        return new ResponseEntity<>(new ErrorResponse("APPOINTMENT_ALREADY_BOOKED", "Appointment creation failed", details, stackTrace, LocalDateTime.now()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        String code = ex.getBindingResult().getFieldErrors()
                            .stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                            .toList().getFirst();

        setDetailsAndStackTrace(ex);

        return new ResponseEntity<>(new ErrorResponse(code, "Validation failed", details, stackTrace, LocalDateTime.now()),
                                            HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        setDetailsAndStackTrace(ex);

        return new ResponseEntity<>(new ErrorResponse("GENERAL", "An unexpected error occurred", details, stackTrace, LocalDateTime.now()),
                                            HttpStatus.BAD_REQUEST);
    }

    private void setDetailsAndStackTrace(Exception ex) {
        if ("dev".equalsIgnoreCase(activeProfile)) {
            details = ex.getMessage();
            stackTrace = Arrays.stream(ex.getStackTrace())
                    .map(StackTraceElement::toString)
                    .toList();
        }
    }
}
