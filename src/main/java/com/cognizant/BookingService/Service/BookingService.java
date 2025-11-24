package com.cognizant.BookingService.Service;

import com.cognizant.BookingService.entity.BookingServiceEntity;
import java.util.List;

public interface BookingService {
    BookingServiceEntity createBooking(BookingServiceEntity booking);
    List<BookingServiceEntity> getAllBookings();
    BookingServiceEntity getBookingById(Long id);
    void deleteBooking(Long id);
}
