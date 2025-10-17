package com.booking.system.appointment.controller;

import com.booking.system.appointment.service.GoogleCalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class GoogleCalendarController {

    @Autowired
    private GoogleCalendarService googleCalendarService;

    @GetMapping("/api/calendar/status")
    public ResponseEntity<Map<String, Object>> getConnectionStatus() {
        Map<String, Object> response = new HashMap<>();
        response.put("connected", googleCalendarService.isConnected());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/calendar/connect")
    public RedirectView connectGoogleCalendar() throws IOException {
        String authUrl = googleCalendarService.getAuthorizationUrl();
        return new RedirectView(authUrl);
    }

    @GetMapping("/oauth2/callback/google")
    public ResponseEntity<String> handleOAuthCallback(@RequestParam("code") String authorizationCode) throws IOException {
        googleCalendarService.handleOAuthCallback(authorizationCode);
        return ResponseEntity.ok("Successfully authorized");
    }

    @PostMapping("/api/calendar/disconnect")
    public ResponseEntity<String> disconnectGoogleCalendar() {
        googleCalendarService.disconnect();
        return ResponseEntity.ok("Successfully disconnected");
    }
}
