package com.booking.system.appointment.service;

import com.booking.system.appointment.dto.*;
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
    private BusinessHoursService businessHoursService;

    @Autowired
    private ModelMapper modelMapper;

    public AppointmentDTO createAppointment(AppointmentRequestDTO appointmentRequest) {
        List<ServiceEntity> services = serviceService.getServicesByCode(appointmentRequest.getServices())
                .stream().map(s -> this.modelMapper.map(s, ServiceEntity.class))
                .toList();

        AppointmentEntity appointment = modelMapper.map(appointmentRequest, AppointmentEntity.class);
        appointment.setServices(new HashSet<>(services));

        return modelMapper.map(appointmentRepository.save(appointment), AppointmentDTO.class);
    }

    public List<LocalTime> getAvailableTimeSlots(TimeSlotsRequestDTO timeSlotsRequestDTO) {
        LocalDate date = timeSlotsRequestDTO.getAppointmentDate();

        int durationRequested = serviceService.getServicesByCode(timeSlotsRequestDTO.getServices())
                                                .stream().mapToInt(ServiceDTO::getSlotTime).sum();

        BusinessHoursDTO businessHours = businessHoursService.getBusinessHoursByDay(date);
        if (businessHours.isClosed())
            return List.of();

        List<LocalTime> availableTimeSlots = new ArrayList<>();
        for (LocalTime timeSlot : generateSlots(businessHours.getStartTime(), businessHours.getEndTime())) {
            if (date.isEqual(LocalDate.now()) && timeSlot.isBefore(LocalTime.now()))
                continue;

            if (timeSlot.plusMinutes(durationRequested).isAfter(businessHours.getEndTime()))
                break;

            if (!doesOverlapTimeSlot(timeSlot, timeSlot.plusMinutes(durationRequested), date))
                availableTimeSlots.add(timeSlot);
        }

        return availableTimeSlots;
    }

    private List<LocalTime> generateSlots(LocalTime startTime, LocalTime endTime) {
        List<LocalTime> slots = new ArrayList<>();
        LocalTime time = startTime;

        while (!time.isAfter(endTime.minusMinutes(30))) {
            slots.add(time);
            time = time.plusMinutes(30);
        }

        return slots;
    }

    public boolean doesOverlapTimeSlot(LocalTime requestedStartTime, LocalTime requestedEndTime, LocalDate date) {
        List<AppointmentEntity> existingAppointments = appointmentRepository.findByStartAtBetween(date.atStartOfDay(), date.atTime(LocalTime.MAX));

        for (AppointmentEntity existingAppointment : existingAppointments) {
            int duration = existingAppointment.getServices().stream().mapToInt(ServiceEntity::getSlotTime).sum();
            LocalDateTime existingAppointmentEndTime = existingAppointment.getStartAt().plusMinutes(duration);

            if (requestedStartTime.isBefore(existingAppointmentEndTime.toLocalTime()) && requestedEndTime.isAfter(existingAppointment.getStartAt().toLocalTime()))
                return true;
        }

        return false;
    }
}
