package com.booking.system.appointment.validation;

import com.booking.system.appointment.dto.ServiceDTO;
import com.booking.system.appointment.dto.TimeSlotsRequestDTO;
import com.booking.system.appointment.service.ServiceService;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TimeSlotsDTOValidatorTest {

    @Mock
    private ServiceService serviceService;

    @Mock
    private ConstraintValidatorContext context;

    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder builder;

    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext nodeContext;

    @InjectMocks
    private TimeSlotsDTOValidator validator;

    private TimeSlotsRequestDTO dto;

    @BeforeEach
    void setUp() {
        dto = new TimeSlotsRequestDTO();
        dto.setAppointmentDate(LocalDate.now().plusDays(1));
        dto.setServices(List.of("CODE1", "CODE2"));
    }

    @Test
    void shouldReturnFalseWhenDateIsInPast() {
        dto.setAppointmentDate(LocalDate.now().minusDays(1));

        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(builder);
        when(builder.addPropertyNode(anyString())).thenReturn(nodeContext);

        boolean result = validator.isValid(dto, context);

        assertFalse(result);
        verify(context).buildConstraintViolationWithTemplate("INVALID_APPOINTMENT_DATE");
    }

    @Test
    void shouldReturnFalseWhenServicesListIsEmpty() {
        dto.setServices(List.of());

        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(builder);
        when(builder.addPropertyNode(anyString())).thenReturn(nodeContext);

        boolean result = validator.isValid(dto, context);

        assertFalse(result);
        verify(context).buildConstraintViolationWithTemplate("INVALID_SERVICES");
    }

    @Test
    void shouldReturnFalseWhenServiceCodesNotMatch() {
        when(serviceService.getServicesByCode(dto.getServices())).thenReturn(List.of(new ServiceDTO("HC", "Hair Cut", BigDecimal.valueOf(12), 40)));

        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(builder);
        when(builder.addPropertyNode(anyString())).thenReturn(nodeContext);

        boolean result = validator.isValid(dto, context);

        assertFalse(result);
        verify(context).buildConstraintViolationWithTemplate("INVALID_SERVICES");
    }

    @Test
    void shouldReturnTrueWhenDateAndServicesAreValid() {
        when(serviceService.getServicesByCode(dto.getServices())).thenReturn(List.of(new ServiceDTO("HC", "Hair Cut", BigDecimal.valueOf(12), 40),
                                                                                    new ServiceDTO("HT", "Hair Treatment", BigDecimal.valueOf(8), 20)));

        boolean result = validator.isValid(dto, context);

        assertTrue(result);
        verify(context, never()).buildConstraintViolationWithTemplate(anyString());
    }
}