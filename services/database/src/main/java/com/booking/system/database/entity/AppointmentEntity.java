package com.booking.system.database.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "APPOINTMENT")
@Getter
@Setter
public class AppointmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "CLIENT_EMAIL", nullable = false, length = 128)
    private String clientEmail;

    @Column(name = "START_AT")
    private LocalDateTime startAt;

    @Transient // Hibernate does not map to any database column
    public LocalDateTime getEndAt() {
        int totalMinutes = services.stream()
                .mapToInt(ServiceEntity::getSlotTime)
                .sum();
        return startAt.plusMinutes(totalMinutes);
    }

    @ManyToMany
    @JoinTable(
        name = "APPOINTMENT_SERVICE",
        joinColumns = @JoinColumn(name = "APPOINTMENT_ID"),
        inverseJoinColumns = @JoinColumn(name = "SERVICE_CODE")
    )
    private Set<ServiceEntity> services = new HashSet<>();

    @CreationTimestamp
    @Column(name = "CREATED_AT", updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "UPDATED_AT")
    private Instant updatedAt;
}
