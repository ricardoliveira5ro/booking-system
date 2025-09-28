package com.booking.system.appointment.repository;

import com.booking.system.database.entity.BusinessHoursExceptionsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface BusinessHoursExceptionsRepository extends JpaRepository<BusinessHoursExceptionsEntity, LocalDate> {

}
