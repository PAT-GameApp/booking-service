package com.cognizant.BookingService.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cognizant.BookingService.entity.BookingServiceEntity;
import com.cognizant.BookingService.repository.BookingServiceRepository;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingServiceRepository bookingServiceRepository;

    @Override
    public BookingServiceEntity createBooking(BookingServiceEntity booking) {
    	
        return bookingServiceRepository.save(booking);
    }

    @Override
    public List<BookingServiceEntity> getAllBookings() {
        return bookingServiceRepository.findAll();
    }

    @Override
    public BookingServiceEntity getBookingById(Long id) {
        Optional<BookingServiceEntity> booking = bookingServiceRepository.findById(id);
        return booking.orElse(null);
    }

    @Override
    public void deleteBooking(Long id) {
        bookingServiceRepository.deleteById(id);
    }
}
