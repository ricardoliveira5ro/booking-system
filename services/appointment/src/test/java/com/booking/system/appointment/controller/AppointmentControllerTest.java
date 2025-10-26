package com.booking.system.appointment.controller;

import com.booking.system.appointment.dto.*;
import com.booking.system.appointment.service.AppointmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AppointmentService appointmentService;

    @Test
    void shouldReturnTimeSlotsForDay() throws Exception {
        TimeSlotsRequestDTO request = new TimeSlotsRequestDTO(LocalDate.now().plusDays(1), List.of("HC", "HT"));

        when(appointmentService.getAvailableTimeSlots(any(TimeSlotsRequestDTO.class))).thenReturn(List.of(LocalTime.of(9, 30), LocalTime.of(10, 0),
                                                                                                    LocalTime.of(10, 30), LocalTime.of(11, 0)));

        String requestBody = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/appointment/time-slots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("09:30"))
                .andExpect(jsonPath("$[1]").value("10:00"))
                .andExpect(jsonPath("$[2]").value("10:30"))
                .andExpect(jsonPath("$[3]").value("11:00"));
    }

    @Test
    void shouldReturnAppointmentById() throws Exception {
        DetailsDTO details = new DetailsDTO("Tester", "tester@example.com", 123456789, "test message");

        ServiceDTO service1 = new ServiceDTO("HC", "Hair Cut", BigDecimal.valueOf(12), 40);
        ServiceDTO service2 = new ServiceDTO("HT", "Hair Treatment", BigDecimal.valueOf(8), 20);
        List<ServiceDTO> services = List.of(service1, service2);

        AppointmentDTO dto = new AppointmentDTO(LocalDateTime.now().plusDays(1).withNano(0), services, details);

        String appointmentId = "1234";

        when(appointmentService.getAppointment(appointmentId)).thenReturn(dto);

        mockMvc.perform(get("/api/appointment/" + appointmentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.appointmentDate").value(dto.getAppointmentDate().toString()))
                .andExpect(jsonPath("$.services[0].code").value("HC"))
                .andExpect(jsonPath("$.services[0].name").value("Hair Cut"))
                .andExpect(jsonPath("$.services[1].code").value("HT"))
                .andExpect(jsonPath("$.services[1].name").value("Hair Treatment"))
                .andExpect(jsonPath("$.details.name").value("Tester"))
                .andExpect(jsonPath("$.details.email").value("tester@example.com"))
                .andExpect(jsonPath("$.details.phoneNumber").value(123456789))
                .andExpect(jsonPath("$.details.message").value("test message"));
    }

    @Test
    void shouldReturnAppointmentCreated() throws Exception {
        DetailsDTO details = new DetailsDTO("Tester", "tester@example.com", 123456789, "test message");

        ServiceDTO service1 = new ServiceDTO("HC", "Hair Cut", BigDecimal.valueOf(12), 40);
        ServiceDTO service2 = new ServiceDTO("HT", "Hair Treatment", BigDecimal.valueOf(8), 20);
        List<ServiceDTO> services = List.of(service1, service2);

        AppointmentDTO dto = new AppointmentDTO(LocalDateTime.now().plusDays(1).withNano(0), services, details);

        AppointmentRequestDTO request = new AppointmentRequestDTO(LocalDate.now().plusDays(1), LocalTime.of(9, 30), List.of("HC", "HT"), details);

        when(appointmentService.createAppointment(any(AppointmentRequestDTO.class))).thenReturn(dto);

        String requestBody = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/appointment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.appointmentDate").value(dto.getAppointmentDate().toString()))
                .andExpect(jsonPath("$.services[0].code").value("HC"))
                .andExpect(jsonPath("$.services[0].name").value("Hair Cut"))
                .andExpect(jsonPath("$.services[1].code").value("HT"))
                .andExpect(jsonPath("$.services[1].name").value("Hair Treatment"))
                .andExpect(jsonPath("$.details.name").value("Tester"))
                .andExpect(jsonPath("$.details.email").value("tester@example.com"))
                .andExpect(jsonPath("$.details.phoneNumber").value(123456789))
                .andExpect(jsonPath("$.details.message").value("test message"));
    }

    @Test
    void shouldCancelAppointment() throws Exception {
        String appointmentId = "1234";
        String cancelKey = "1111222233334444";

        mockMvc.perform(delete("/api/appointment/" + appointmentId + "?cancelKey=" + cancelKey))
                .andExpect(status().isOk())
                .andExpect(content().string("Appointment cancelled"));

        verify(appointmentService).cancelAppointment(appointmentId, cancelKey);
    }
}