package com.booking.system.appointment.controller;

import com.booking.system.appointment.dto.ServiceDTO;
import com.booking.system.appointment.service.ServiceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class ServiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ServiceService serviceService;

    @Test
    void shouldReturnAllServices() throws Exception {
        when(serviceService.getAllServices()).thenReturn(List.of(new ServiceDTO("HC", "Hair Cut", BigDecimal.valueOf(12), 40),
                                                                    new ServiceDTO("HT", "Hair Treatment", BigDecimal.valueOf(8), 20)));

        mockMvc.perform(get("/api/appointment/services"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").value("HC"))
                .andExpect(jsonPath("$[0].name").value("Hair Cut"))
                .andExpect(jsonPath("$[1].code").value("HT"))
                .andExpect(jsonPath("$[1].name").value("Hair Treatment"));
    }
}