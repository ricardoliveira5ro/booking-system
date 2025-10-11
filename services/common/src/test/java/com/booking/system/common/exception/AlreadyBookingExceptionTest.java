package com.booking.system.common.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlreadyBookingExceptionTest {

    @Test
    void AlreadyBookingException_shouldSetMessage() {
        String msg = "Appointment already booked";
        AlreadyBookingException ex = new AlreadyBookingException(msg);

        assertNotNull(ex);
        assertEquals(msg, ex.getMessage());
        assertInstanceOf(RuntimeException.class, ex);
    }
}