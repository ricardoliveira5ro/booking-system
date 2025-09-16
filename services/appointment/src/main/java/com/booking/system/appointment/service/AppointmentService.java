package com.booking.system.appointment.service;

import com.booking.system.appointment.dto.AppointmentRequestDTO;
import com.booking.system.appointment.repository.AppointmentRepository;
import com.booking.system.database.entity.AppointmentEntity;
import com.booking.system.database.entity.ServiceEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    // To be replaced by configs
    private static final LocalTime START_WORKING_HOURS = LocalTime.of(9, 0);
    private static final LocalTime END_WORKING_HOURS = LocalTime.of(19, 30);

    public List<LocalTime> getAvailableTimeSlots(AppointmentRequestDTO appointmentRequest) {
        LocalDate date = appointmentRequest.getAppointmentDay().toLocalDate();
        List<AppointmentEntity> existingAppointments = appointmentRepository.findByStartAtBetween(date.atStartOfDay(), date.atTime(LocalTime.MAX));

        List<LocalTime> availableTimeSlots = new ArrayList<>();
        for (LocalTime timeSlot : generateSlots()) {
            LocalDateTime startTime = LocalDateTime.of(date, timeSlot);
            LocalDateTime endTime = startTime.plusMinutes(appointmentRequest.getDuration());

            if (endTime.toLocalTime().isAfter(END_WORKING_HOURS))
                break;

            if (!doesOverlapTimeSlot(startTime, endTime, existingAppointments))
                availableTimeSlots.add(timeSlot);
        }

        return availableTimeSlots;
    }

    private List<LocalTime> generateSlots() {
        List<LocalTime> slots = new ArrayList<>();
        LocalTime time = START_WORKING_HOURS;

        while (!time.isAfter(END_WORKING_HOURS.minusMinutes(30))) {
            slots.add(time);
            time = time.plusMinutes(30);
        }

        return slots;
    }

    private boolean doesOverlapTimeSlot(LocalDateTime requestedStartTime, LocalDateTime requestedEndTime, List<AppointmentEntity> existingAppointments) {
        for (AppointmentEntity existingAppointment : existingAppointments) {
            int duration = existingAppointment.getServices().stream().mapToInt(ServiceEntity::getSlotTime).sum();
            LocalDateTime existingAppointmentEndTime = existingAppointment.getStartAt().plusMinutes(duration);

            if (requestedStartTime.isBefore(existingAppointmentEndTime) && requestedEndTime.isAfter(existingAppointment.getStartAt()))
                return true;
        }

        return false;
    }
}
