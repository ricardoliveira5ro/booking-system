package com.booking.system.appointment.validation;

import com.booking.system.appointment.dto.AppointmentRequestDTO;
import com.booking.system.appointment.service.ServiceService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServicesValidator implements ConstraintValidator<ValidServices, AppointmentRequestDTO> {

    @Autowired
    private ServiceService serviceService;

    @Override
    public boolean isValid(AppointmentRequestDTO appointmentRequest, ConstraintValidatorContext context) {
        if (appointmentRequest.getServices().isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Services list must not be empty")
                    .addPropertyNode("services")
                    .addConstraintViolation();

            return false;
        }

        return serviceService.getServicesByCode(appointmentRequest.getServices()).size() != appointmentRequest.getServices().size();
    }
}
