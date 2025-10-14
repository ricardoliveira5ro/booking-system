package com.booking.system.appointment.service;

import com.booking.system.appointment.dto.*;
import com.booking.system.appointment.repository.AppointmentRepository;
import com.booking.system.common.exception.AlreadyBookingException;
import com.booking.system.database.entity.AppointmentEntity;
import com.booking.system.database.entity.ServiceEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private ServiceService serviceService;

    @Mock
    private BusinessHoursService businessHoursService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AppointmentService appointmentService;

    @Test
    void shouldCreateAppointmentSuccessfully() {
        LocalDate today = LocalDate.now();

        AppointmentRequestDTO dto = new AppointmentRequestDTO();
        dto.setAppointmentDate(today);
        dto.setAppointmentTime(LocalTime.of(10, 0));
        dto.setServices(List.of("HC", "HT"));

        ServiceDTO service1 = new ServiceDTO();
        service1.setCode("HC");
        service1.setName("Hair Cut");
        service1.setPrice(BigDecimal.valueOf(14));
        service1.setSlotTime(40);

        ServiceDTO service2 = new ServiceDTO();
        service2.setCode("HT");
        service2.setName("Hair Treatment");
        service2.setPrice(BigDecimal.valueOf(8));
        service2.setSlotTime(20);

        List<ServiceDTO> serviceDTOs = List.of(service1, service2);

        ServiceEntity serviceEntity1 = new ServiceEntity();
        serviceEntity1.setCode("HC");
        serviceEntity1.setName("Hair Cut");
        serviceEntity1.setPrice(BigDecimal.valueOf(14));
        serviceEntity1.setSlotTime(40);

        ServiceEntity serviceEntity2 = new ServiceEntity();
        serviceEntity2.setCode("HT");
        serviceEntity2.setName("Hair Treatment");
        serviceEntity2.setPrice(BigDecimal.valueOf(8));
        serviceEntity2.setSlotTime(20);

        AppointmentEntity savedEntity = new AppointmentEntity();
        AppointmentDTO expectedDto = new AppointmentDTO();

        when(serviceService.getServicesByCode(List.of("HC", "HT"))).thenReturn(serviceDTOs);
        when(modelMapper.map(service1, ServiceEntity.class)).thenReturn(serviceEntity1);
        when(modelMapper.map(service2, ServiceEntity.class)).thenReturn(serviceEntity2);
        when(appointmentRepository.findByStartAtBetween(any(), any())).thenReturn(List.of());
        when(modelMapper.map(dto, AppointmentEntity.class)).thenReturn(new AppointmentEntity());
        when(appointmentRepository.save(any())).thenReturn(savedEntity);
        when(modelMapper.map(savedEntity, AppointmentDTO.class)).thenReturn(expectedDto);

        AppointmentDTO result = appointmentService.createAppointment(dto);

        assertEquals(expectedDto, result);
        verify(appointmentRepository, times(1)).save(any());
    }

    @Test
    void shouldNotCreateAppointmentSuccessfully_whenOverlap() {
        LocalDate today = LocalDate.now();
        LocalTime start = LocalTime.of(9, 0);

        AppointmentRequestDTO request = new AppointmentRequestDTO();
        request.setAppointmentDate(today);
        request.setAppointmentTime(start);
        request.setServices(List.of("HC"));

        ServiceDTO service1 = new ServiceDTO();
        service1.setCode("HC");
        service1.setName("Hair Cut");
        service1.setPrice(BigDecimal.valueOf(14));
        service1.setSlotTime(40);

        ServiceEntity serviceEntity1 = new ServiceEntity();
        serviceEntity1.setCode("HC");
        serviceEntity1.setName("Hair Cut");
        serviceEntity1.setPrice(BigDecimal.valueOf(14));
        serviceEntity1.setSlotTime(40);

        when(serviceService.getServicesByCode(List.of("HC"))).thenReturn(List.of(service1));
        when(modelMapper.map(service1, ServiceEntity.class)).thenReturn(serviceEntity1);

        AppointmentEntity existing = new AppointmentEntity();
        existing.setStartAt(LocalDateTime.of(today, LocalTime.of(9, 0)));
        existing.setServices(Set.of(serviceEntity1));

        when(appointmentRepository.findByStartAtBetween(any(), any())).thenReturn(List.of(existing));

        AlreadyBookingException ex = assertThrows(AlreadyBookingException.class, () -> appointmentService.createAppointment(request));
        assertTrue(ex.getMessage().contains("Appointment already booked"));

        verify(appointmentRepository, never()).save(any());
    }

    @Test
    void shouldReturnAvailableTimeSlots() {
        LocalDate today = LocalDate.now().plusDays(1);

        TimeSlotsRequestDTO request = new TimeSlotsRequestDTO();
        request.setAppointmentDate(today);
        request.setServices(List.of("HC"));

        ServiceDTO service1 = new ServiceDTO();
        service1.setCode("HC");
        service1.setName("Hair Cut");
        service1.setPrice(BigDecimal.valueOf(14));
        service1.setSlotTime(40);

        when(serviceService.getServicesByCode(any())).thenReturn(List.of(service1));

        BusinessHoursDTO hours = new BusinessHoursDTO();
        hours.setStartTime(LocalTime.of(9, 0));
        hours.setEndTime(LocalTime.of(11, 0));
        hours.setClosed(false);

        when(businessHoursService.getBusinessHoursByDay(today)).thenReturn(hours);
        when(appointmentRepository.findByStartAtBetween(any(), any())).thenReturn(List.of());

        List<LocalTime> result = appointmentService.getAvailableTimeSlots(request);

        assertFalse(result.isEmpty());
        assertTrue(result.contains(LocalTime.of(9, 0)));
    }

    @Test
    void shouldReturnEmptyList_whenClosed() {
        LocalDate today = LocalDate.now();

        TimeSlotsRequestDTO request = new TimeSlotsRequestDTO();
        request.setAppointmentDate(today);
        request.setServices(List.of("HC"));

        BusinessHoursDTO hours = new BusinessHoursDTO();
        hours.setClosed(true);

        when(serviceService.getServicesByCode(any())).thenReturn(List.of());
        when(businessHoursService.getBusinessHoursByDay(today)).thenReturn(hours);

        List<LocalTime> result = appointmentService.getAvailableTimeSlots(request);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmptyList_whenNoAvailableTimeSlots() {
        LocalDate today = LocalDate.now();

        TimeSlotsRequestDTO request = new TimeSlotsRequestDTO();
        request.setAppointmentDate(today);
        request.setServices(List.of("HC"));

        BusinessHoursDTO hours = new BusinessHoursDTO();
        hours.setStartTime(LocalTime.of(9, 0));
        hours.setEndTime(LocalTime.of(11, 0));
        hours.setClosed(false);

        when(serviceService.getServicesByCode(any())).thenReturn(List.of());
        when(businessHoursService.getBusinessHoursByDay(today)).thenReturn(hours);

        List<LocalTime> result = appointmentService.getAvailableTimeSlots(request);

        assertTrue(result.isEmpty());
    }
}