package com.booking.system.appointment.config;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.GeneralSecurityException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

class GoogleCalendarConfigTest {

    @Test
    void shouldThrowIllegalStateException_whenHttpTransportFails() throws Exception {
        try (var mocked = mockStatic(GoogleNetHttpTransport.class)) {
            mocked.when(GoogleNetHttpTransport::newTrustedTransport).thenThrow(new GeneralSecurityException("security fail"));

            GoogleCalendarConfig config = new GoogleCalendarConfig();

            IllegalStateException ex = assertThrows(IllegalStateException.class, () -> ReflectionTestUtils.invokeMethod(config, "httpTransport"));

            assertEquals("Failed to create HTTP transport", ex.getMessage());
            assertNotNull(ex.getCause());
            assertEquals("security fail", ex.getCause().getMessage());
        }
    }
}