package com.booking.system.appointment.service;

import com.booking.system.appointment.config.GoogleCalendarConfig;
import com.booking.system.appointment.dto.AppointmentDTO;
import com.booking.system.appointment.repository.BarberRepository;
import com.booking.system.database.entity.BarberEntity;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;

@Service
public class GoogleCalendarService {

    @Autowired
    private GoogleCalendarConfig config;

    @Autowired
    private NetHttpTransport httpTransport;

    @Autowired
    private JsonFactory jsonFactory;

    @Autowired
    private BarberRepository barberRepository;

    public boolean isConnected() {
        return barberRepository.findAll().getFirst().isConnected();
    }

    public String getAuthorizationUrl() throws IOException {
        GoogleAuthorizationCodeFlow flow = createFlow();
        return flow.newAuthorizationUrl()
                .setRedirectUri(config.getRedirectUri())
                .build();
    }

    public void handleOAuthCallback(String authorizationCode) throws IOException {
        GoogleAuthorizationCodeFlow flow = createFlow();
        TokenResponse tokenResponse = flow.newTokenRequest(authorizationCode)
                .setRedirectUri(config.getRedirectUri())
                .execute();

        BarberEntity credentials = barberRepository.findAll().stream().findFirst().orElse(new BarberEntity());

        credentials.setAccessToken(tokenResponse.getAccessToken());
        credentials.setRefreshToken(tokenResponse.getRefreshToken());
        credentials.setTokenExpiry(LocalDateTime.now().plusSeconds(tokenResponse.getExpiresInSeconds()));
        credentials.setConnected(true);

        barberRepository.save(credentials);
    }

    public String createCalendarEvent(AppointmentDTO appointment, int durationMinutes) throws IOException {
        Calendar calendarService = getCalendarService();

        Event event = new Event()
                .setSummary("Appointment: " + appointment.getDetails().getName())
                .setDescription(buildEventDescription(appointment));

        LocalDateTime startDateTime = appointment.getAppointmentDate();
        EventDateTime start = new EventDateTime().setDateTime(new com.google.api.client.util.DateTime(Date.from(startDateTime.atZone(ZoneId.systemDefault()).toInstant())))
                                                .setTimeZone(ZoneId.systemDefault().getId());
        event.setStart(start);

        LocalDateTime endDateTime = startDateTime.plusMinutes(durationMinutes);
        EventDateTime end = new EventDateTime().setDateTime(new com.google.api.client.util.DateTime(Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant())))
                                                .setTimeZone(ZoneId.systemDefault().getId());
        event.setEnd(end);

        Event createdEvent = calendarService.events()
                .insert("primary", event)
                .execute();

        return createdEvent.getId();
    }

    public void deleteCalendarEvent(String eventId) throws IOException {
        Calendar calendarService = getCalendarService();
        calendarService.events().delete("primary", eventId).execute();
    }

    public void disconnect() {
        BarberEntity barber = barberRepository.findAll().getFirst();

        if (barber != null) {
            barber.setConnected(false);
            barber.setAccessToken(null);
            barber.setRefreshToken(null);
            barber.setTokenExpiry(null);
            barberRepository.save(barber);
        }
    }

    private Calendar getCalendarService() throws IOException {
        BarberEntity barber = barberRepository.findAll().getFirst();

        if (!barber.isConnected())
            throw new IllegalStateException("Google Calendar not configured");

        if (barber.getTokenExpiry().isBefore(LocalDateTime.now()))
            refreshAccessToken(barber);

        Credential credential = new Credential.Builder(BearerToken.authorizationHeaderAccessMethod())
                .setTransport(httpTransport)
                .setJsonFactory(jsonFactory)
                .setTokenServerEncodedUrl("https://oauth2.googleapis.com/token")
                .setClientAuthentication(httpRequest -> {})
                .build()
                .setAccessToken(barber.getAccessToken())
                .setRefreshToken(barber.getRefreshToken());


        return new Calendar.Builder(httpTransport, jsonFactory, credential)
                .setApplicationName(config.getApplicationName())
                .build();
    }

    private void refreshAccessToken(BarberEntity barber) throws IOException {
        GoogleTokenResponse response = new GoogleRefreshTokenRequest(httpTransport, jsonFactory, barber.getRefreshToken(), config.getClientId(), config.getClientSecret())
                                            .execute();

        barber.setAccessToken(response.getAccessToken());
        barber.setTokenExpiry(LocalDateTime.now().plusSeconds(response.getExpiresInSeconds()));
        barberRepository.save(barber);
    }

    private GoogleAuthorizationCodeFlow createFlow() throws IOException {
        return new GoogleAuthorizationCodeFlow.Builder(httpTransport, jsonFactory, config.getClientId(), config.getClientSecret(), Collections.singleton(CalendarScopes.CALENDAR))
                .setAccessType("offline")
                .setApprovalPrompt("force")
                .build();
    }

    private String buildEventDescription(AppointmentDTO appointment) {
        StringBuilder description = new StringBuilder();
        description.append("Appointment Details:\n\n");
        description.append("Client: ").append(appointment.getDetails().getName()).append("\n");
        description.append("Email: ").append(appointment.getDetails().getEmail()).append("\n");

        if (appointment.getDetails().getPhoneNumber() != null)
            description.append("Phone Number: ").append(appointment.getDetails().getPhoneNumber()).append("\n");

        if (appointment.getServices() != null && !appointment.getServices().isEmpty()) {
            description.append("\nServices:\n");
            appointment.getServices().forEach(service -> description.append("  • ").append(service.getName()).append(" -- ").append(service.getSlotTime()).append(" min").append("\n"));
        }

        description.append("\n---\n");
        description.append("by Booking System");

        return description.toString();
    }
}
