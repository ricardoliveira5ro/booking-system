package com.booking.system.common.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AppointmentNotFoundExceptionTest {

    @Test
    void AppointmentNotFoundException_shouldSetMessage() {
        String msg = "Appointment already booked";
        AppointmentNotFoundException ex = new AppointmentNotFoundException(msg);

        assertNotNull(ex);
        assertEquals(msg, ex.getMessage());
        assertInstanceOf(RuntimeException.class, ex);
    }
}