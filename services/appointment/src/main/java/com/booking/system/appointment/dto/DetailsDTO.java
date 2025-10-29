package com.booking.system.appointment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DetailsDTO {

    private String name;
    private String email;
    private Integer phoneNumber;
    private String message;

    @Override
    public String toString() {
        return "DetailsDTO{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber=" + phoneNumber +
                ", message='" + message + '\'' +
                '}';
    }
}
