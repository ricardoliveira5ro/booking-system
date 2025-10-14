package com.booking.system.appointment.service;

import com.booking.system.appointment.dto.ServiceDTO;
import com.booking.system.appointment.repository.ServiceRepository;
import com.booking.system.database.entity.ServiceEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceServiceTest {

    @Mock
    private ServiceRepository serviceRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ServiceService serviceService;

    private ServiceEntity entity1;
    private ServiceEntity entity2;

    @BeforeEach
    void setUp() {
        entity1 = new ServiceEntity();
        entity1.setCode("HC");
        entity1.setName("Hair Cut");
        entity1.setPrice(BigDecimal.valueOf(12));
        entity1.setSlotTime(40);

        entity2 = new ServiceEntity();
        entity2.setCode("HT");
        entity2.setName("Hair Treatment");
        entity2.setPrice(BigDecimal.valueOf(8));
        entity2.setSlotTime(20);
    }

    @Test
    void shouldReturnAllServices() {
        List<ServiceEntity> entities = List.of(entity1, entity2);

        ServiceDTO entityDto1 = new ServiceDTO("HC", "Hair Cut", BigDecimal.valueOf(12), 40);
        ServiceDTO entityDto2 = new ServiceDTO("HT", "Hair Treatment", BigDecimal.valueOf(8), 20);
        List<ServiceDTO> entitiesDto = List.of(entityDto1, entityDto2);

        when(serviceRepository.findAll()).thenReturn(entities);
        when(modelMapper.map(entities.getFirst(), ServiceDTO.class)).thenReturn(entityDto1);
        when(modelMapper.map(entities.getLast(), ServiceDTO.class)).thenReturn(entityDto2);

        List<ServiceDTO> result = serviceService.getAllServices();

        assertEquals(entitiesDto.size(), result.size());

        List<String> expected = Arrays.asList(
            "ServiceDTO{code='HC', name='Hair Cut', price=12, slotTime=40}",
            "ServiceDTO{code='HT', name='Hair Treatment', price=8, slotTime=20}"
        );

        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), entitiesDto.get(i).toString());
        }

        verify(serviceRepository, times(1)).findAll();
        verify(modelMapper, times(2)).map(any(ServiceEntity.class), eq(ServiceDTO.class));
    }

    @Test
    void shouldReturnAllServicesByCode() {
        List<ServiceEntity> entities = List.of(entity1);
        List<String> codes = List.of("HC");

        ServiceDTO entityDto1 = new ServiceDTO("HC", "Hair Cut", BigDecimal.valueOf(12), 40);
        List<ServiceDTO> entitiesDto = List.of(entityDto1);

        when(serviceRepository.findAllById(codes)).thenReturn(entities);
        when(modelMapper.map(entities.getFirst(), ServiceDTO.class)).thenReturn(entityDto1);

        List<ServiceDTO> result = serviceService.getServicesByCode(codes);

        assertEquals(entitiesDto.size(), result.size());
        assertEquals("ServiceDTO{code='HC', name='Hair Cut', price=12, slotTime=40}", entitiesDto.getFirst().toString());

        verify(serviceRepository, times(1)).findAllById(codes);
        verify(modelMapper, times(1)).map(any(ServiceEntity.class), eq(ServiceDTO.class));
    }
}