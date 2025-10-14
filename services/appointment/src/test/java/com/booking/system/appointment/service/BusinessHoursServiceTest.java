package com.booking.system.appointment.service;

import com.booking.system.appointment.dto.BusinessHoursDTO;
import com.booking.system.appointment.repository.BusinessHoursExceptionsRepository;
import com.booking.system.appointment.repository.BusinessHoursRepository;
import com.booking.system.database.entity.BusinessHoursEntity;
import com.booking.system.database.entity.BusinessHoursExceptionsEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BusinessHoursServiceTest {

    @Mock
    private BusinessHoursRepository businessHoursRepository;

    @Mock
    private BusinessHoursExceptionsRepository businessHoursExceptionsRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private BusinessHoursService service;

    private final BusinessHoursEntity businessHoursEntity = new BusinessHoursEntity();
    private final BusinessHoursExceptionsEntity businessHoursExceptionsEntity = new BusinessHoursExceptionsEntity();

    @BeforeEach
    void setUp() {
        businessHoursEntity.setWeekday(1);
        businessHoursEntity.setStartTime(LocalTime.of(9, 30));
        businessHoursEntity.setEndTime(LocalTime.of(18, 30));

        businessHoursExceptionsEntity.setExceptionDay(LocalDate.now());
        businessHoursExceptionsEntity.setStartTime(LocalTime.of(10, 0));
        businessHoursExceptionsEntity.setEndTime(LocalTime.of(16, 0));
        businessHoursExceptionsEntity.setClosed(false);
    }

    @Test
    void shouldReturnBusinessHoursByDay() {
        LocalDate today = LocalDate.now();
        BusinessHoursDTO businessHoursDTO = new BusinessHoursDTO(LocalTime.of(9, 30), LocalTime.of(18, 30), false);

        when(businessHoursRepository.findById(today.getDayOfWeek().getValue())).thenReturn(Optional.of(businessHoursEntity));
        when(modelMapper.map(businessHoursEntity, BusinessHoursDTO.class)).thenReturn(businessHoursDTO);

        BusinessHoursDTO result = service.getBusinessHoursByDay(today);

        assertEquals(LocalTime.of(9, 30), result.getStartTime());
        assertEquals(LocalTime.of(18, 30), result.getEndTime());
        assertFalse(businessHoursDTO.isClosed());

        verify(businessHoursExceptionsRepository, times(1)).findById(today);
        verify(businessHoursRepository, times(1)).findById(today.getDayOfWeek().getValue());
        verify(modelMapper, times(1)).map(businessHoursEntity, BusinessHoursDTO.class);
    }

    @Test
    void shouldReturnBusinessHoursByDay_whenException() {
        LocalDate today = LocalDate.now();
        BusinessHoursDTO businessHoursDTO = new BusinessHoursDTO(LocalTime.of(10, 0), LocalTime.of(16, 0), false);

        when(businessHoursExceptionsRepository.findById(today)).thenReturn(Optional.of(businessHoursExceptionsEntity));
        when(modelMapper.map(businessHoursExceptionsEntity, BusinessHoursDTO.class)).thenReturn(businessHoursDTO);

        BusinessHoursDTO result = service.getBusinessHoursByDay(today);

        assertEquals(LocalTime.of(10, 0), result.getStartTime());
        assertEquals(LocalTime.of(16, 0), result.getEndTime());
        assertFalse(businessHoursDTO.isClosed());

        verify(businessHoursExceptionsRepository, times(1)).findById(today);
        verify(modelMapper, times(1)).map(businessHoursExceptionsEntity, BusinessHoursDTO.class);
    }
}