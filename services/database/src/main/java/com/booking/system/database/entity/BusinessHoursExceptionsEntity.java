package com.booking.system.database.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Table(name = "T_BUSINESS_HOURS_EXCEPTIONS")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class BusinessHoursExceptionsEntity {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "EXCEPTION_DAY")
    private Integer exceptionDay;

    @Column(name = "START_TIME")
    private LocalTime startTime;

    @Column(name = "END_TIME")
    private LocalTime endTime;

    @Column(name = "IS_CLOSED")
    private boolean isClosed;
}
