package com.booking.system.appointment.validation;

import com.booking.system.appointment.dto.AppointmentRequestDTO;
import com.booking.system.appointment.dto.ServiceDTO;
import com.booking.system.appointment.service.ServiceService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class AppointmentDTOValidator implements ConstraintValidator<ValidAppointmentDTO, AppointmentRequestDTO> {

    @Autowired
    private ServiceService serviceService;

    // To be replaced by configs
    private static final LocalTime START_WORKING_HOURS = LocalTime.of(9, 0);
    private static final LocalTime END_WORKING_HOURS = LocalTime.of(19, 30);

    @Override
    public boolean isValid(AppointmentRequestDTO appointmentRequestDTO, ConstraintValidatorContext context) {
        boolean isValid = true;

        if (appointmentRequestDTO.getAppointmentDate().isBefore(LocalDateTime.now()) ||
            appointmentRequestDTO.getAppointmentDate().toLocalTime().isBefore(START_WORKING_HOURS) ||
            appointmentRequestDTO.getAppointmentDate().toLocalTime().isAfter(END_WORKING_HOURS) ||
            isAppointmentDateEndsAfterHours(appointmentRequestDTO)
        ) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Invalid appointment date")
                    .addPropertyNode("appointmentDate")
                    .addConstraintViolation();

            isValid = false;
        }

        if (appointmentRequestDTO.getServices().isEmpty() ||
            serviceService.getServicesByCode(appointmentRequestDTO.getServices()).size() != appointmentRequestDTO.getServices().size()
        ) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Invalid services")
                    .addPropertyNode("services")
                    .addConstraintViolation();

            isValid = false;
        }

        return isValid;
    }

    private boolean isAppointmentDateEndsAfterHours(AppointmentRequestDTO appointmentRequestDTO) {
        int duration = serviceService.getServicesByCode(appointmentRequestDTO.getServices())
                                        .stream().mapToInt(ServiceDTO::getSlotTime).sum();

        LocalDateTime endTime = appointmentRequestDTO.getAppointmentDate().plusMinutes(duration);

        return endTime.toLocalTime().isAfter(END_WORKING_HOURS);
    }
}
