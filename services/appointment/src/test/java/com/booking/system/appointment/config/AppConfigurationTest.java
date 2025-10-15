package com.booking.system.appointment.config;

import com.booking.system.appointment.dto.AppointmentDTO;
import com.booking.system.appointment.dto.AppointmentRequestDTO;
import com.booking.system.appointment.dto.DetailsDTO;
import com.booking.system.appointment.dto.ServiceDTO;
import com.booking.system.database.entity.AppointmentEntity;
import com.booking.system.database.entity.ServiceEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AppConfigurationTest {

    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        AppConfiguration config = new AppConfiguration();
        modelMapper = config.modelMapper();
    }

    @Test
    void shouldMapAppointmentRequestDTOToAppointmentEntity() {
        DetailsDTO details = new DetailsDTO("Tester", "tester@example.com", 123456789, "Test message");
        AppointmentRequestDTO requestDTO = new AppointmentRequestDTO(LocalDate.of(2025, 10, 20), LocalTime.of(14, 0), List.of("HC"), details);

        AppointmentEntity entity = modelMapper.map(requestDTO, AppointmentEntity.class);

        assertEquals(LocalDateTime.of(2025, 10, 20, 14, 0), entity.getStartAt());
        assertEquals("Tester", entity.getClientName());
        assertEquals("tester@example.com", entity.getClientEmail());
        assertEquals(123456789, entity.getPhoneNumber());
        assertEquals("Test message", entity.getMessage());
    }

    @Test
    void shouldMapAppointmentEntityToAppointmentDTO() {
        ServiceEntity service = new ServiceEntity();
        service.setCode("HC");
        service.setName("Hair Cut");
        service.setPrice(BigDecimal.valueOf(12));
        service.setSlotTime(40);

        AppointmentEntity entity = new AppointmentEntity();
        entity.setStartAt(LocalDateTime.of(2025, 10, 20, 14, 0));
        entity.setClientName("Tester");
        entity.setClientEmail("tester@example.com");
        entity.setPhoneNumber(987654321);
        entity.setMessage("Test message");
        entity.setServices(Set.of(service));

        AppointmentDTO dto = modelMapper.map(entity, AppointmentDTO.class);

        assertEquals(LocalDateTime.of(2025, 10, 20, 14, 0), dto.getAppointmentDate());
        assertEquals("Tester", dto.getDetails().getName());
        assertEquals("tester@example.com", dto.getDetails().getEmail());
        assertEquals(987654321, dto.getDetails().getPhoneNumber());
        assertEquals("Test message", dto.getDetails().getMessage());

        assertEquals(1, dto.getServices().size());
        ServiceDTO s = dto.getServices().getFirst();
        assertEquals("HC", s.getCode());
        assertEquals("Hair Cut", s.getName());
        assertEquals(BigDecimal.valueOf(12), s.getPrice());
        assertEquals(40, s.getSlotTime());
    }
}