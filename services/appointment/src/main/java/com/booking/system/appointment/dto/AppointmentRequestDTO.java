package com.booking.system.appointment.dto;

import com.booking.system.appointment.validation.ValidAppointmentDate;
import com.booking.system.appointment.validation.ValidServices;
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
public class AppointmentRequestDTO {

    @NotNull
    @ValidAppointmentDate
    private LocalDateTime appointmentDate;

    @NotNull
    @ValidServices
    private List<String> services;

    // Todo: DetailsDTO
}
