package com.booking.system.database.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "T_SERVICE")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ServiceEntity {

    @Id
    @EqualsAndHashCode.Include // Lombok uses this to calculate equals and hashcode functions (Objects with same code are equal)
    @Column(name = "CODE")
    private String code;

    @Column(name = "NAME", nullable = false, length = 128)
    private String name;

    @Column(name = "PRICE", nullable = false)
    private BigDecimal price;

    @Column(name = "SLOT_TIME", nullable = false)
    private Integer slotTime;

    @CreationTimestamp
    @Column(name = "CREATED_AT", updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "UPDATED_AT")
    private Instant updatedAt;
}
