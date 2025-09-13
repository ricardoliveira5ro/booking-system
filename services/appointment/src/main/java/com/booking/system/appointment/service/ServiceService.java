package com.booking.system.appointment.service;

import com.booking.system.appointment.dto.ServiceDTO;
import com.booking.system.appointment.repository.ServiceRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<ServiceDTO> getAllServices() {
        return serviceRepository.findAll()
                .stream().map(serviceEntity -> this.modelMapper.map(serviceEntity, ServiceDTO.class))
                .toList();
    }
}
