package com.booking.system.appointment.validation;

import com.booking.system.appointment.dto.TimeSlotsRequestDTO;
import com.booking.system.appointment.service.ServiceService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TimeSlotsDTOValidator implements ConstraintValidator<ValidTimeSlotsDTO, TimeSlotsRequestDTO> {

    @Autowired
    private ServiceService serviceService;

    @Override
    public boolean isValid(TimeSlotsRequestDTO timeSlotsRequestDTO, ConstraintValidatorContext context) {
        if (timeSlotsRequestDTO.getAppointmentDate().isBefore(LocalDate.now())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("INVALID_APPOINTMENT_DATE")
                    .addPropertyNode("appointmentDate")
                    .addConstraintViolation();

            return false;
        }

        if (timeSlotsRequestDTO.getServices().isEmpty() ||
            serviceService.getServicesByCode(timeSlotsRequestDTO.getServices()).size() != timeSlotsRequestDTO.getServices().size()
        ) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("INVALID_SERVICES")
                    .addPropertyNode("services")
                    .addConstraintViolation();

            return false;
        }

        return true;
    }
}
