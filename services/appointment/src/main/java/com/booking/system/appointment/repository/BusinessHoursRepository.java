package com.booking.system.appointment.repository;

import com.booking.system.database.entity.BusinessHoursEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessHoursRepository extends JpaRepository<BusinessHoursEntity, Integer> {

}
