package com.booking.system.appointment.service;

import com.booking.system.appointment.config.GoogleCalendarConfig;
import com.booking.system.appointment.dto.AppointmentDTO;
import com.booking.system.appointment.dto.DetailsDTO;
import com.booking.system.appointment.dto.ServiceDTO;
import com.booking.system.appointment.repository.BarberRepository;
import com.booking.system.database.entity.BarberEntity;
import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoogleCalendarServiceTest {

    @Mock
    private GoogleCalendarConfig config;

    @Mock
    private BarberRepository barberRepository;

    @Mock
    private NetHttpTransport httpTransport;

    @Mock
    private JsonFactory jsonFactory;

    @InjectMocks
    private GoogleCalendarService service;

    @Test
    void shouldReturnConnectionStatus() {
        BarberEntity barber = new BarberEntity();
        barber.setConnected(true);
        when(barberRepository.findAll()).thenReturn(List.of(barber));

        boolean result = service.isConnected();

        assertTrue(result);
    }

    @Test
    void shouldThrowException_whenBarberNotConnected() {
        BarberEntity barber = new BarberEntity();
        barber.setConnected(false);
        when(barberRepository.findAll()).thenReturn(List.of(barber));

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> ReflectionTestUtils.invokeMethod(service, "getCalendarService"));

        assertEquals("Google Calendar not configured", ex.getMessage());
    }

    @Test
    void shouldReturnAuthorizationUrl() throws IOException {
        GoogleAuthorizationCodeFlow flow = mock(GoogleAuthorizationCodeFlow.class);
        GoogleAuthorizationCodeRequestUrl url = mock(GoogleAuthorizationCodeRequestUrl.class);

        when(flow.newAuthorizationUrl()).thenReturn(url);
        when(url.setRedirectUri(anyString())).thenReturn(url);
        when(url.build()).thenReturn("https://auth.url");
        when(config.getRedirectUri()).thenReturn("http://redirect");

        GoogleCalendarService spyService = Mockito.spy(service);
        doReturn(flow).when(spyService).createFlow();

        String result = spyService.getAuthorizationUrl();

        assertEquals("https://auth.url", result);
    }

    @Test
    void shouldHandleOAuthCallbackAndSaveCredentials() throws IOException {
        GoogleAuthorizationCodeFlow flow = mock(GoogleAuthorizationCodeFlow.class);
        GoogleTokenResponse tokenResponse = mock(GoogleTokenResponse.class);
        GoogleAuthorizationCodeTokenRequest tokenRequest = mock(GoogleAuthorizationCodeTokenRequest.class);

        when(flow.newTokenRequest("code123")).thenReturn(tokenRequest);
        when(tokenRequest.setRedirectUri(anyString())).thenReturn(tokenRequest);
        when(tokenRequest.execute()).thenReturn(tokenResponse);

        when(tokenResponse.getAccessToken()).thenReturn("access");
        when(tokenResponse.getRefreshToken()).thenReturn("refresh");
        when(tokenResponse.getExpiresInSeconds()).thenReturn(3600L);

        BarberEntity existing = new BarberEntity();
        when(barberRepository.findAll()).thenReturn(List.of(existing));
        when(config.getRedirectUri()).thenReturn("http://redirect");

        GoogleCalendarService spyService = Mockito.spy(service);
        doReturn(flow).when(spyService).createFlow();

        spyService.handleOAuthCallback("code123");

        verify(barberRepository).save(argThat(b ->
                b.isConnected() &&
                        "access".equals(b.getAccessToken()) &&
                        "refresh".equals(b.getRefreshToken())
        ));
    }

    @Test
    void shouldDisconnectBarber() {
        BarberEntity barber = new BarberEntity();
        barber.setConnected(true);
        barber.setAccessToken("token");
        when(barberRepository.findAll()).thenReturn(List.of(barber));

        service.disconnect();

        assertFalse(barber.isConnected());
        assertNull(barber.getAccessToken());
        verify(barberRepository).save(barber);
    }

    @Test
    void shouldBuildEventDescription() {
        AppointmentDTO dto = new AppointmentDTO();
        dto.setDetails(new DetailsDTO("Tester", "tester@example.com", 123456789, "test message"));
        dto.setServices(List.of(new ServiceDTO("HC", "Hair Cut", BigDecimal.valueOf(12), 40)));

        String result = ReflectionTestUtils.invokeMethod(service, "buildEventDescription", dto);

        assertNotNull(result);
        assertTrue(result.contains("Client: Tester"));
        assertTrue(result.contains("Services:"));
        assertTrue(result.contains("Cut"));
        assertTrue(result.contains("by Booking System"));
    }

    @Test
    void shouldCreateCalendarEvent() throws IOException {
        Calendar.Events events = mock(Calendar.Events.class);
        Calendar.Events.Insert insert = mock(Calendar.Events.Insert.class);
        Calendar calendar = mock(Calendar.class);

        Event created = new Event().setId("event123");
        when(insert.execute()).thenReturn(created);
        when(events.insert(eq("primary"), any(Event.class))).thenReturn(insert);
        when(calendar.events()).thenReturn(events);

        GoogleCalendarService spyService = Mockito.spy(service);
        doReturn(calendar).when(spyService).getCalendarService();

        AppointmentDTO dto = new AppointmentDTO();
        dto.setDetails(new DetailsDTO("Tester", "tester@example.com", 123456789, "test message"));
        dto.setAppointmentDate(LocalDateTime.now());

        String result = spyService.createCalendarEvent(dto, 30);

        assertEquals("event123", result);
        verify(events).insert(eq("primary"), any(Event.class));
    }

    @Test
    void shouldDeleteCalendarEvent() throws IOException {
        Calendar.Events events = mock(Calendar.Events.class);
        Calendar.Events.Delete delete = mock(Calendar.Events.Delete.class);

        when(events.delete("primary", "event123")).thenReturn(delete);
        doNothing().when(delete).execute();

        Calendar calendar = mock(Calendar.class);
        when(calendar.events()).thenReturn(events);

        GoogleCalendarService spyService = Mockito.spy(service);
        doReturn(calendar).when(spyService).getCalendarService();

        spyService.deleteCalendarEvent("event123");

        verify(events).delete("primary", "event123");
        verify(delete).execute();
    }

    @Test
    void shouldBuildCalendarServiceWhenConnectedAndTokenValid() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        BarberEntity barber = new BarberEntity();
        barber.setConnected(true);
        barber.setAccessToken("token");
        barber.setRefreshToken("refresh");
        barber.setTokenExpiry(LocalDateTime.now().plusHours(1));

        when(barberRepository.findAll()).thenReturn(List.of(barber));
        when(config.getApplicationName()).thenReturn("App");

        Calendar result = service.getClass()
                .getDeclaredMethod("getCalendarService")
                .invoke(service) instanceof Calendar ? (Calendar)
                service.getClass().getDeclaredMethod("getCalendarService")
                        .invoke(service) : null;

        assertNotNull(result);
    }

    @Test
    void shouldRefreshAccessToken_whenTokenExpired() throws IOException {
        BarberEntity barber = new BarberEntity();
        barber.setConnected(true);
        barber.setRefreshToken("refreshToken");

        GoogleTokenResponse tokenResponse = mock(GoogleTokenResponse.class);
        when(tokenResponse.getAccessToken()).thenReturn("newAccess");
        when(tokenResponse.getExpiresInSeconds()).thenReturn(3600L);

        try (MockedConstruction<GoogleRefreshTokenRequest> ignored =
            Mockito.mockConstruction(GoogleRefreshTokenRequest.class, (mock, context) -> when(mock.execute()).thenReturn(tokenResponse))
        ) {
            service.refreshAccessToken(barber);

            assertEquals("newAccess", barber.getAccessToken());
            assertNotNull(barber.getTokenExpiry());
            verify(barberRepository).save(barber);
        }
    }

    @Test
    void shouldCreateFlowSuccessfully() throws IOException {
        when(config.getClientId()).thenReturn("clientId");
        when(config.getClientSecret()).thenReturn("clientSecret");

        GoogleAuthorizationCodeFlow flow = service.createFlow();

        assertNotNull(flow);
    }

    @Test
    void shouldRefreshToken_whenTokenExpired() throws Exception {
        BarberEntity barber = new BarberEntity();
        barber.setConnected(true);
        barber.setAccessToken("old");
        barber.setRefreshToken("refresh");
        barber.setTokenExpiry(LocalDateTime.now().minusMinutes(1));
        when(barberRepository.findAll()).thenReturn(List.of(barber));
        when(config.getApplicationName()).thenReturn("App");

        GoogleCalendarService spyService = Mockito.spy(service);
        doNothing().when(spyService).refreshAccessToken(barber);

        ReflectionTestUtils.invokeMethod(spyService, "getCalendarService");

        verify(spyService).refreshAccessToken(barber);
    }
}