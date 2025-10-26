package com.booking.system.appointment.controller;

import com.booking.system.appointment.service.GoogleCalendarService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class GoogleCalendarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GoogleCalendarService googleCalendarService;

    @Test
    void shouldReturnConnectionStatus() throws Exception {
        when(googleCalendarService.isConnected()).thenReturn(true);

        mockMvc.perform(get("/api/calendar/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.connected").value(true));
    }

    @Test
    void shouldConnectToGoogleCalendar() throws Exception {
        String expectedUrl = "https://accounts.google.com/o/oauth2/auth";
        when(googleCalendarService.getAuthorizationUrl()).thenReturn(expectedUrl);

        mockMvc.perform(get("/api/calendar/connect"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(expectedUrl));
    }

    @Test
    void shouldHandleOAuthCallback() throws Exception {
        String authorizationCode = "aaaa-bbbb-cccc";

        mockMvc.perform(get("/oauth2/callback/google?code=" + authorizationCode))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully authorized"));

        verify(googleCalendarService).handleOAuthCallback(authorizationCode);
    }

    @Test
    void shouldDisconnectGoogleCalendar() throws Exception {
        mockMvc.perform(post("/api/calendar/disconnect"))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully disconnected"));

        verify(googleCalendarService).disconnect();
    }
}