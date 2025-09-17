package com.booking.system.appointment.service;

import com.booking.system.appointment.dto.AppointmentDTO;
import com.booking.system.appointment.dto.AppointmentRequestDTO;
import com.booking.system.appointment.dto.ServiceDTO;
import com.booking.system.appointment.repository.AppointmentRepository;
import com.booking.system.database.entity.AppointmentEntity;
import com.booking.system.database.entity.ServiceEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private ModelMapper modelMapper;

    // To be replaced by configs
    private static final LocalTime START_WORKING_HOURS = LocalTime.of(9, 0);
    private static final LocalTime END_WORKING_HOURS = LocalTime.of(19, 30);

    public AppointmentDTO createAppointment(AppointmentRequestDTO appointmentRequest) {
        List<ServiceEntity> services = serviceService.getServicesByCode(appointmentRequest.getServices())
                .stream().map(s -> this.modelMapper.map(s, ServiceEntity.class))
                .toList();

        AppointmentEntity appointment = modelMapper.map(appointmentRequest, AppointmentEntity.class);
        appointment.setServices(new HashSet<>(services));

        return modelMapper.map(appointmentRepository.save(appointment), AppointmentDTO.class);
    }

    public List<LocalTime> getAvailableTimeSlots(AppointmentRequestDTO appointmentRequest) {
        LocalDate date = appointmentRequest.getAppointmentDate().toLocalDate();
        List<AppointmentEntity> existingAppointments = appointmentRepository.findByStartAtBetween(date.atStartOfDay(), date.atTime(LocalTime.MAX));

        int durationRequested = serviceService.getServicesByCode(appointmentRequest.getServices())
                                                .stream().mapToInt(ServiceDTO::getSlotTime).sum();

        List<LocalTime> availableTimeSlots = new ArrayList<>();
        for (LocalTime timeSlot : generateSlots()) {
            LocalDateTime startTime = LocalDateTime.of(date, timeSlot);
            LocalDateTime endTime = startTime.plusMinutes(durationRequested);

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
