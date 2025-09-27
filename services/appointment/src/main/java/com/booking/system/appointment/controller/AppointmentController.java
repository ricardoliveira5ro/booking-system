package com.booking.system.appointment.controller;

import com.booking.system.appointment.dto.AppointmentDTO;
import com.booking.system.appointment.dto.AppointmentRequestDTO;
import com.booking.system.appointment.dto.TimeSlotsRequestDTO;
import com.booking.system.appointment.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/appointment")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping("/time-slots")
    public ResponseEntity<List<String>> getTimeSlots(@Valid @RequestBody TimeSlotsRequestDTO timeSlotsRequestDTO) {
        List<LocalTime> timeSlots = appointmentService.getAvailableTimeSlots(timeSlotsRequestDTO);

        return ResponseEntity.ok(timeSlots.stream().map(LocalTime::toString).toList());
    }

    @PostMapping
    public ResponseEntity<AppointmentDTO> createAppointment(@Valid @RequestBody AppointmentRequestDTO appointmentRequest) {
        AppointmentDTO appointment = appointmentService.createAppointment(appointmentRequest);

        return ResponseEntity.ok(appointment);
    }
}
