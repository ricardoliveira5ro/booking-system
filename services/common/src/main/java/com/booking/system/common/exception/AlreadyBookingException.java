package com.booking.system.common.exception;

public class AlreadyBookingException extends RuntimeException {
    public AlreadyBookingException(String message) {
        super(message);
    }
}
