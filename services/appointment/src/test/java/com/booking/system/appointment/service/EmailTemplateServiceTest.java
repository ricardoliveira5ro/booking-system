package com.booking.system.appointment.service;

import org.junit.jupiter.api.Test;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmailTemplateServiceTest {

    @Test
    void shouldBuildAppointmentEmail() {
        TemplateEngine templateEngine = mock(TemplateEngine.class);
        EmailTemplateService service = new EmailTemplateService(templateEngine);

        Map<String, Object> variables = Map.of(
                "name", "Alice",
                "date", "2025-10-25",
                "time", "10:30",
                "cancelLink", "http://localhost:3000/dummyCancelLink"
        );

        service.buildAppointmentEmail(variables);

        verify(templateEngine).process(eq("appointment-confirmation.html"), any(Context.class));
    }
}