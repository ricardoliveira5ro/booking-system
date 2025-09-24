package com.booking.system.appointment.controller;

import com.booking.system.appointment.dto.AppointmentDTO;
import com.booking.system.appointment.dto.AppointmentRequestDTO;
import com.booking.system.appointment.service.AppointmentService;
import com.booking.system.appointment.validation.CheckTimeSlots;
import com.booking.system.appointment.validation.CreateAppointment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/appointment")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping("/time-slots")
    public ResponseEntity<List<String>> getTimeSlots(@Validated(CheckTimeSlots.class) @RequestBody AppointmentRequestDTO appointmentRequest) {
        List<LocalTime> timeSlots = appointmentService.getAvailableTimeSlots(appointmentRequest);

        return ResponseEntity.ok(timeSlots.stream().map(LocalTime::toString).toList());
    }

    @PostMapping
    public ResponseEntity<AppointmentDTO> createAppointment(@Validated(CreateAppointment.class) @RequestBody AppointmentRequestDTO appointmentRequest) {
        AppointmentDTO appointment = appointmentService.createAppointment(appointmentRequest);

        return ResponseEntity.ok(appointment);
    }
}
