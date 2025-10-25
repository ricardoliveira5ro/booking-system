package com.booking.system.appointment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootCheckController {

    @GetMapping
    public ResponseEntity<String> getAllServices() {
        return ResponseEntity.ok("Services are up!");
    }
}
