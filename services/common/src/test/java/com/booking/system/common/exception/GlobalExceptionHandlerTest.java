package com.booking.system.common.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        setActiveProfile(handler, "prod");
    }

    @Test
    void handleAlreadyBookingException() {
        AlreadyBookingException ex = new AlreadyBookingException("Already booked");

        ResponseEntity<ErrorResponse> response = handler.handleAlreadyBookingException(ex);

        assertNotNull(response.getBody());
        assertEquals(400, response.getStatusCode().value());
        assertEquals("APPOINTMENT_ALREADY_BOOKED", response.getBody().code());
        assertEquals("Appointment creation failed", response.getBody().message());
    }

    @Test
    void handleGeneralException() {
        Exception ex = new Exception("Something went wrong");

        ResponseEntity<ErrorResponse> response = handler.handleGeneralException(ex);

        assertNotNull(response.getBody());
        assertEquals(400, response.getStatusCode().value());
        assertEquals("GENERAL", response.getBody().code());
        assertEquals("An unexpected error occurred", response.getBody().message());
    }

    @Test
    void handleValidationException_returnsBadRequest_withFirstFieldError() {
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError error = new FieldError("objectName", "field", "FIELD_INVALID");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(error));

        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<ErrorResponse> response = handler.handleValidationException(ex);

        assertNotNull(response.getBody());
        assertEquals(400, response.getStatusCode().value());
        assertEquals("FIELD_INVALID", response.getBody().code());
        assertEquals("Validation failed", response.getBody().message());
    }

    @Test
    void setDetailsAndStackTrace_populatesDetailsAndStackTrace_inDev() {
        setActiveProfile(handler, "dev");

        Exception ex = new Exception("Test exception");
        handler.handleGeneralException(ex);

        assertEquals("Test exception", handler.details);
        assertFalse(handler.stackTrace.isEmpty());
        assertTrue(handler.stackTrace.getFirst().contains("GlobalExceptionHandlerTest"));
    }

    private void setActiveProfile(GlobalExceptionHandler handler, String profile) {
        try {
            Field field = GlobalExceptionHandler.class.getDeclaredField("activeProfile");
            field.setAccessible(true);
            field.set(handler, profile);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}