package com.booking.system.appointment.dto;

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

    private LocalDateTime appointmentDay;
    private List<ServiceDTO> services;
    // Todo: DetailsDTO

    public int getDuration() {
        return services.stream().mapToInt(ServiceDTO::getSlotTime).sum();
    }
}
