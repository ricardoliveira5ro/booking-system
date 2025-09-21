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
        boolean isValid = true;

        if (appointmentRequestDTO.getAppointmentDate().isBefore(LocalDate.now())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Invalid appointment date")
                    .addPropertyNode("appointmentDate")
                    .addConstraintViolation();

            isValid = false;
        }

        System.out.println();

        if ((appointmentRequestDTO.getAppointmentDate().isEqual(LocalDate.now()) && appointmentRequestDTO.getAppointmentTime().isBefore(LocalTime.now().minusMinutes(1))) ||
            appointmentRequestDTO.getAppointmentTime().isBefore(START_WORKING_HOURS) ||
            appointmentRequestDTO.getAppointmentTime().isAfter(END_WORKING_HOURS) ||
            isAppointmentTimeEndsAfterHours(appointmentRequestDTO)
        ) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Invalid appointment time")
                    .addPropertyNode("appointmentTime")
                    .addConstraintViolation();

            isValid = false;
        }

        if (doesOverlapTimeSlot(appointmentRequestDTO)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("This appointment time is already booked")
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
