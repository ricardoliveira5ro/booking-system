package com.booking.system.appointment.validation;

import com.booking.system.appointment.dto.AppointmentRequestDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class AppointmentDateValidator implements ConstraintValidator<ValidAppointmentDate, AppointmentRequestDTO> {

    // To be replaced by configs
    private static final LocalTime START_WORKING_HOURS = LocalTime.of(9, 0);
    private static final LocalTime END_WORKING_HOURS = LocalTime.of(19, 30);

    @Override
    public boolean isValid(AppointmentRequestDTO appointmentRequest, ConstraintValidatorContext context) {
        LocalDateTime appointmentDate = appointmentRequest.getAppointmentDate();

        return !appointmentDate.isBefore(LocalDateTime.now()) &&
                !appointmentDate.toLocalTime().isBefore(START_WORKING_HOURS) &&
                !appointmentDate.toLocalTime().isAfter(END_WORKING_HOURS);
    }
}
