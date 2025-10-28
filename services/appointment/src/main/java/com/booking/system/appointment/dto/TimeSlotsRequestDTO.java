package com.booking.system.appointment.dto;

import com.booking.system.appointment.validation.ValidTimeSlotsDTO;
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
@ValidTimeSlotsDTO
public class TimeSlotsRequestDTO {

    @NotNull
    private LocalDate appointmentDate;

    @NotNull
    private List<String> services;

    @Override
    public String toString() {
        return "TimeSlotsRequestDTO{" +
                "appointmentDate=" + appointmentDate +
                ", services=" + services +
                '}';
    }
}
