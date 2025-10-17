package com.booking.system.database.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "T_BARBER")
@Getter
@Setter
public class BarberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "ACCESS_TOKEN", length = 2048)
    private String accessToken;

    @Column(name = "REFRESH_TOKEN", length = 2048)
    private String refreshToken;

    @Column(name = "TOKEN_EXPIRY")
    private LocalDateTime tokenExpiry;

    @Column(name = "IS_CONNECTED")
    private boolean isConnected = false;

    @CreationTimestamp
    @Column(name = "CREATED_AT", updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "UPDATED_AT")
    private Instant updatedAt;
}
