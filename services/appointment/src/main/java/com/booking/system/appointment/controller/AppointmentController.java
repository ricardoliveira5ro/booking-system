package com.booking.system.appointment.controller;

import com.booking.system.appointment.dto.AppointmentDTO;
import com.booking.system.appointment.dto.AppointmentRequestDTO;
import com.booking.system.appointment.dto.TimeSlotsRequestDTO;
import com.booking.system.appointment.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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

    @GetMapping("/{appointmentId}")
    public ResponseEntity<AppointmentDTO> getAppointment(@PathVariable("appointmentId") String appointmentId) {
        AppointmentDTO appointment = appointmentService.getAppointment(appointmentId);

        return ResponseEntity.ok(appointment);
    }

    @PostMapping
    public ResponseEntity<AppointmentDTO> createAppointment(@Valid @RequestBody AppointmentRequestDTO appointmentRequest) throws IOException {
        AppointmentDTO appointment = appointmentService.createAppointment(appointmentRequest);

        return ResponseEntity.ok(appointment);
    }

    @DeleteMapping("/{appointmentId}")
    public ResponseEntity<String> cancelAppointment(@PathVariable("appointmentId") String appointmentId, @RequestParam(name = "cancelKey") String cancelKey) throws IOException {
        appointmentService.cancelAppointment(appointmentId, cancelKey);

        return ResponseEntity.ok("Appointment cancelled");
    }
}
