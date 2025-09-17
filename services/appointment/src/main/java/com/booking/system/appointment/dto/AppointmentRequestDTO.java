package com.booking.system.appointment.dto;

import com.booking.system.appointment.validation.ValidAppointmentDTO;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ValidAppointmentDTO
public class AppointmentRequestDTO {

    @NotNull
    private LocalDateTime appointmentDate;

    @NotNull
    private List<String> services;

    // Todo: DetailsDTO
}
