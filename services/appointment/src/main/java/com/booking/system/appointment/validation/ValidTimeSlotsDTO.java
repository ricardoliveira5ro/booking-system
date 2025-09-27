package com.booking.system.appointment.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TimeSlotsDTOValidator.class)
public @interface ValidTimeSlotsDTO {
    String message() default "Invalid Time Slots Request";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
