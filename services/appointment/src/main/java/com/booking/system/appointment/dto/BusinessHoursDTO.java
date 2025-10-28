package com.booking.system.appointment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BusinessHoursDTO {

    private LocalTime startTime;
    private LocalTime endTime;
    private boolean isClosed = false;

    @Override
    public String toString() {
        return "BusinessHoursDTO{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                ", isClosed=" + isClosed +
                '}';
    }
}
