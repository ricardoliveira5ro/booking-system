package com.booking.system.appointment.validation;

import com.booking.system.appointment.dto.AppointmentRequestDTO;
import com.booking.system.appointment.dto.BusinessHoursDTO;
import com.booking.system.appointment.dto.DetailsDTO;
import com.booking.system.appointment.dto.ServiceDTO;
import com.booking.system.appointment.service.AppointmentService;
import com.booking.system.appointment.service.BusinessHoursService;
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
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentDTOValidatorTest {

    @Mock
    private ServiceService serviceService;

    @Mock
    private AppointmentService appointmentService;

    @Mock
    private BusinessHoursService businessHoursService;

    @Mock
    private ConstraintValidatorContext context;

    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder builder;

    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext nodeContext;

    @InjectMocks
    private AppointmentDTOValidator validator;

    private AppointmentRequestDTO dto;
    private BusinessHoursDTO businessHours;

    @BeforeEach
    void setUp() {
        dto = new AppointmentRequestDTO();
        dto.setServices(List.of("HC", "HT"));
        dto.setAppointmentDate(LocalDate.now().plusDays(1));
        dto.setAppointmentTime(LocalTime.of(10, 0));
        dto.setDetails(new DetailsDTO("Test", "tester@example.com", null, null));

        businessHours = new BusinessHoursDTO();
        businessHours.setStartTime(LocalTime.of(9, 0));
        businessHours.setEndTime(LocalTime.of(18, 0));
        businessHours.setClosed(false);
    }

    @Test
    void shouldReturnFalseWhenDateIsInPast() {
        dto.setAppointmentDate(LocalDate.now().minusDays(1));

        when(businessHoursService.getBusinessHoursByDay(any())).thenReturn(businessHours);

        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(builder);
        when(builder.addPropertyNode(anyString())).thenReturn(nodeContext);

        boolean result = validator.isValid(dto, context);

        assertFalse(result);
        verify(context).buildConstraintViolationWithTemplate("INVALID_APPOINTMENT_DATE");
    }

    @Test
    void shouldReturnFalse_whenBusinessIsClosed() {
        businessHours.setClosed(true);
        when(businessHoursService.getBusinessHoursByDay(any())).thenReturn(businessHours);

        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(builder);
        when(builder.addPropertyNode(anyString())).thenReturn(nodeContext);

        boolean result = validator.isValid(dto, context);

        assertFalse(result);
        verify(context).buildConstraintViolationWithTemplate("INVALID_APPOINTMENT_DATE");
    }

    @Test
    void shouldReturnFalse_whenAppointmentTimeEndsAfterWorkingHours() {
        when(businessHoursService.getBusinessHoursByDay(any())).thenReturn(businessHours);
        when(serviceService.getServicesByCode(dto.getServices())).thenReturn(List.of(new ServiceDTO("HC", "Hair Cut", BigDecimal.valueOf(12), 600),
                                                                                new ServiceDTO("HT", "Hair Treatment", BigDecimal.valueOf(8), 600)));

        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(builder);
        when(builder.addPropertyNode(anyString())).thenReturn(nodeContext);

        boolean result = validator.isValid(dto, context);

        assertFalse(result);
        verify(context).buildConstraintViolationWithTemplate("INVALID_APPOINTMENT_TIME");
    }

    @Test
    void shouldReturnFalse_whenAppointmentTimeOutsideOfWorkingHours() {
        when(businessHoursService.getBusinessHoursByDay(any())).thenReturn(businessHours);
        dto.setAppointmentTime(LocalTime.of(7, 0));

        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(builder);
        when(builder.addPropertyNode(anyString())).thenReturn(nodeContext);

        boolean result = validator.isValid(dto, context);

        assertFalse(result);
        verify(context).buildConstraintViolationWithTemplate("INVALID_APPOINTMENT_TIME");
    }

    @Test
    void shouldReturnFalseWhenServiceCodesNotMatch() {
        when(businessHoursService.getBusinessHoursByDay(any())).thenReturn(businessHours);
        when(serviceService.getServicesByCode(dto.getServices())).thenReturn(List.of(new ServiceDTO("HC", "Hair Cut", BigDecimal.valueOf(12), 40)));

        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(builder);
        when(builder.addPropertyNode(anyString())).thenReturn(nodeContext);

        boolean result = validator.isValid(dto, context);

        assertFalse(result);
        verify(context).buildConstraintViolationWithTemplate("INVALID_SERVICES");
    }

    @Test
    void shouldReturnFalse_whenInvalidDetails() {
        when(businessHoursService.getBusinessHoursByDay(any())).thenReturn(businessHours);
        when(serviceService.getServicesByCode(dto.getServices())).thenReturn(List.of(new ServiceDTO("HC", "Hair Cut", BigDecimal.valueOf(12), 40),
                                                                                new ServiceDTO("HT", "Hair Treatment", BigDecimal.valueOf(8), 20)));

        dto.getDetails().setName("");
        dto.getDetails().setEmail("");
        dto.getDetails().setPhoneNumber(null);

        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(builder);
        when(builder.addPropertyNode(anyString())).thenReturn(nodeContext);

        boolean result = validator.isValid(dto, context);

        assertFalse(result);
        verify(context).buildConstraintViolationWithTemplate("INVALID_DETAILS");
    }

    @Test
    void shouldReturnTrue_whenAllDataIsValid() {
        when(businessHoursService.getBusinessHoursByDay(any())).thenReturn(businessHours);
        when(serviceService.getServicesByCode(dto.getServices())).thenReturn(List.of(new ServiceDTO("HC", "Hair Cut", BigDecimal.valueOf(12), 40),
                                                                                    new ServiceDTO("HT", "Hair Treatment", BigDecimal.valueOf(8), 20)));

        boolean result = validator.isValid(dto, context);

        assertTrue(result);
        verify(context, never()).buildConstraintViolationWithTemplate(anyString());
    }
}