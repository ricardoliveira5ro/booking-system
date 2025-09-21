package com.booking.system.appointment.config;

import com.booking.system.appointment.dto.AppointmentDTO;
import com.booking.system.appointment.dto.AppointmentRequestDTO;
import com.booking.system.appointment.dto.DetailsDTO;
import com.booking.system.appointment.dto.ServiceDTO;
import com.booking.system.database.entity.AppointmentEntity;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class AppConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.typeMap(AppointmentRequestDTO.class, AppointmentEntity.class)
                .addMappings(mapper -> {
                    mapper.using(ctx -> {
                        AppointmentRequestDTO src = (AppointmentRequestDTO) ctx.getSource();
                        return LocalDateTime.of(src.getAppointmentDate(), src.getAppointmentTime());
                    }).map(src -> src, AppointmentEntity::setStartAt);
                    mapper.map(src -> src.getDetails().getName(), AppointmentEntity::setClientName);
                    mapper.map(src -> src.getDetails().getEmail(), AppointmentEntity::setClientEmail);
                    mapper.map(src -> src.getDetails().getPhoneNumber(), AppointmentEntity::setPhoneNumber);
                    mapper.map(src -> src.getDetails().getMessage(), AppointmentEntity::setMessage);
                });

        TypeMap<AppointmentEntity, AppointmentDTO> typeMap = modelMapper.createTypeMap(AppointmentEntity.class, AppointmentDTO.class);

        typeMap.addMappings(mapper -> {
            mapper.map(AppointmentEntity::getStartAt, AppointmentDTO::setAppointmentDate);
            mapper.using(ctx -> {
                AppointmentEntity src = (AppointmentEntity) ctx.getSource();
                return new DetailsDTO(src.getClientName(), src.getClientEmail(), src.getPhoneNumber(), src.getMessage());
            }).map(src -> src, AppointmentDTO::setDetails);
            mapper.using(ctx -> {
                AppointmentEntity src = (AppointmentEntity) ctx.getSource();
                return src.getServices().stream()
                        .map(s -> new ServiceDTO(s.getCode(), s.getName(), s.getPrice(), s.getSlotTime()))
                        .toList();
            }).map(src -> src, AppointmentDTO::setServices);
        });

        return modelMapper;
    }
}
