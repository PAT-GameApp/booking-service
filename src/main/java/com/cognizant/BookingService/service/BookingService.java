package com.cognizant.BookingService.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cognizant.BookingService.entity.Booking;
import com.cognizant.BookingService.repository.BookingServiceRepository;

@Service
public class BookingService {
    @Autowired
    private BookingServiceRepository bookingServiceRepository;

    public Booking createBooking(Booking booking) {

        return bookingServiceRepository.save(booking);
    }

    public List<Booking> getAllBookings() {
        return bookingServiceRepository.findAll();
    }

    public Booking getBookingById(Long id) {
        Optional<Booking> booking = bookingServiceRepository.findById(id);
        return booking.orElse(null);
    }

    public void deleteBooking(Long id) {
        bookingServiceRepository.deleteById(id);
    }
}
