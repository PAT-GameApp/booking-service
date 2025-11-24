package com.cognizant.BookingService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cognizant.BookingService.entity.BookingServiceEntity;

public interface BookingServiceRepository extends JpaRepository<BookingServiceEntity, Long> {

}
