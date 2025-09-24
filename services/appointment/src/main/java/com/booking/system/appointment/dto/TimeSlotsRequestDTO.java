package com.booking.system.appointment.dto;

import com.booking.system.appointment.validation.CheckTimeSlots;
import com.booking.system.appointment.validation.ValidAppointmentDTO;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ValidAppointmentDTO(groups = { CheckTimeSlots.class })
public class TimeSlotsRequestDTO {

    @NotNull
    private LocalDate appointmentDate;

    @NotNull
    private LocalTime appointmentTime;

    @NotNull
    private List<String> services;

    private DetailsDTO details;
}
