package com.booking.system.appointment.validation;

import com.booking.system.appointment.dto.AppointmentRequestDTO;
import com.booking.system.appointment.dto.ServiceDTO;
import com.booking.system.appointment.service.AppointmentService;
import com.booking.system.appointment.service.ServiceService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class AppointmentDTOValidator implements ConstraintValidator<ValidAppointmentDTO, AppointmentRequestDTO> {

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private AppointmentService appointmentService;

    // To be replaced by configs
    private static final LocalTime START_WORKING_HOURS = LocalTime.of(9, 0);
    private static final LocalTime END_WORKING_HOURS = LocalTime.of(19, 30);

    @Override
    public boolean isValid(AppointmentRequestDTO appointmentRequestDTO, ConstraintValidatorContext context) {
        if (appointmentRequestDTO.getAppointmentDate().isBefore(LocalDate.now())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("INVALID_APPOINTMENT_DATE")
                    .addPropertyNode("appointmentDate")
                    .addConstraintViolation();

            return false;
        }

        if ((appointmentRequestDTO.getAppointmentDate().isEqual(LocalDate.now()) && appointmentRequestDTO.getAppointmentTime().isBefore(LocalTime.now().minusMinutes(1))) ||
            appointmentRequestDTO.getAppointmentTime().isBefore(START_WORKING_HOURS) ||
            appointmentRequestDTO.getAppointmentTime().isAfter(END_WORKING_HOURS) ||
            isAppointmentTimeEndsAfterHours(appointmentRequestDTO)
        ) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("INVALID_APPOINTMENT_TIME")
                    .addPropertyNode("appointmentTime")
                    .addConstraintViolation();

            return false;
        }

        if (doesOverlapTimeSlot(appointmentRequestDTO)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("APPOINTMENT_ALREADY_BOOKED")
                    .addPropertyNode("appointmentDate")
                    .addConstraintViolation();

            return false;
        }

        if (appointmentRequestDTO.getServices().isEmpty() ||
            serviceService.getServicesByCode(appointmentRequestDTO.getServices()).size() != appointmentRequestDTO.getServices().size()
        ) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("INVALID_SERVICES")
                    .addPropertyNode("services")
                    .addConstraintViolation();

            return false;
        }

        if (appointmentRequestDTO.getDetails().getName().isBlank() &&
            (appointmentRequestDTO.getDetails().getEmail().isBlank() || appointmentRequestDTO.getDetails().getPhoneNumber() == null)
        ) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("INVALID_DETAILS")
                    .addPropertyNode("details")
                    .addConstraintViolation();

            return false;
        }

        return true;
    }

    private boolean isAppointmentTimeEndsAfterHours(AppointmentRequestDTO appointmentRequestDTO) {
        int duration = serviceService.getServicesByCode(appointmentRequestDTO.getServices())
                .stream().mapToInt(ServiceDTO::getSlotTime).sum();

        LocalTime endTime = appointmentRequestDTO.getAppointmentTime().plusMinutes(duration);

        return endTime.isAfter(END_WORKING_HOURS);
    }

    private boolean doesOverlapTimeSlot(AppointmentRequestDTO appointmentRequestDTO) {
        int duration = serviceService.getServicesByCode(appointmentRequestDTO.getServices())
                .stream().mapToInt(ServiceDTO::getSlotTime).sum();

        LocalDate date = appointmentRequestDTO.getAppointmentDate();
        LocalTime requestedStartTime = appointmentRequestDTO.getAppointmentTime();
        LocalTime requestedEndTime = appointmentRequestDTO.getAppointmentTime().plusMinutes(duration);

        return appointmentService.doesOverlapTimeSlot(requestedStartTime, requestedEndTime, date);
    }
}
