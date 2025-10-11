package com.booking.system.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@ContextConfiguration(classes = SecurityConfiguration.class)
class SecurityConfigurationTest {

    @Autowired
    private SecurityFilterChain securityFilterChain;

    @Autowired
    private CorsConfigurationSource corsConfigurationSource;

    @Test
    void filterChain_shouldLoadSuccessfully() {
        assertNotNull(securityFilterChain);
    }

    @Test
    void contextLoads() {
        assertNotNull(securityFilterChain);
        assertNotNull(corsConfigurationSource);
    }

    @Test
    void corsConfigurationSource_shouldUseLocalhost_whenDevProfile() {
        SecurityConfiguration config = new SecurityConfiguration();
        setActiveProfile(config, "dev");

        CorsConfigurationSource source = config.corsConfigurationSource();

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/");
        CorsConfiguration cors = source.getCorsConfiguration(request);

        assertNotNull(cors);
        assertNotNull(cors.getAllowedOrigins());
        assertNotNull(cors.getAllowCredentials());

        assertTrue(cors.getAllowedOrigins().contains("http://localhost:3000"));
        assertTrue(cors.getAllowCredentials());
    }

    @Test
    void corsConfigurationSource_shouldUseProdDomain_whenProdProfile() {
        SecurityConfiguration config = new SecurityConfiguration();
        setActiveProfile(config, "prod");

        CorsConfigurationSource source = config.corsConfigurationSource();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/");
        CorsConfiguration cors = source.getCorsConfiguration(request);

        assertNotNull(cors);
        assertNotNull(cors.getAllowedOrigins());
        assertNotNull(cors.getAllowCredentials());

        assertTrue(cors.getAllowedOrigins().contains("https://booking-system-blond-psi.vercel.app"));
        assertTrue(cors.getAllowCredentials());
    }

    private void setActiveProfile(SecurityConfiguration config, String profile) {
        try {
            Field field = SecurityConfiguration.class.getDeclaredField("activeProfile");
            field.setAccessible(true);
            field.set(config, profile);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}