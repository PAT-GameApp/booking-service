package com.cognizant.BookingService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cognizant.BookingService.entity.Booking;

public interface BookingServiceRepository extends JpaRepository<Booking, Long> {

}
