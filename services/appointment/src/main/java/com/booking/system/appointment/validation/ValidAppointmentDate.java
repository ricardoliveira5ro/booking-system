package com.booking.system.appointment.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AppointmentDateValidator.class)
public @interface ValidAppointmentDate {
    String message() default "Invalid appointment time";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
