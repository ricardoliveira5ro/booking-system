package com.booking.system.appointment.service;

import com.booking.system.appointment.dto.BusinessHoursDTO;
import com.booking.system.appointment.repository.BusinessHoursExceptionsRepository;
import com.booking.system.appointment.repository.BusinessHoursRepository;
import com.booking.system.database.entity.BusinessHoursEntity;
import com.booking.system.database.entity.BusinessHoursExceptionsEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class BusinessHoursService {

    @Autowired
    private BusinessHoursRepository businessHoursRepository;

    @Autowired
    private BusinessHoursExceptionsRepository businessHoursExceptionsRepository;

    @Autowired
    private ModelMapper modelMapper;

    public BusinessHoursDTO getBusinessHoursByDay(LocalDate day) {
        BusinessHoursExceptionsEntity exception = businessHoursExceptionsRepository.findById(day).orElse(null);

        if (exception != null)
            return modelMapper.map(exception, BusinessHoursDTO.class);

        int weekdayValue = day.getDayOfWeek().getValue();
        BusinessHoursEntity businessHours = businessHoursRepository.findById(weekdayValue).orElse(null);

        return modelMapper.map(businessHours, BusinessHoursDTO.class);
    }
}
