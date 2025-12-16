package com.cognizant.BookingService.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.BookingService.service.BookingService;

import com.cognizant.BookingService.entity.Booking;
import com.cognizant.BookingService.exception.ResourceNotFoundException;
import com.cognizant.BookingService.dto.BookingCreateRequestDTO;

@RestController
@RequestMapping("/bookings")
public class BookingServiceController {

    @Autowired
    private BookingService bookingService;

    @PostMapping("/")
    public String createBooking(@RequestBody BookingCreateRequestDTO request) {
        bookingService.createBooking(request);
        return "Booking created";
    }

    @GetMapping("/")
    public List<Booking> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @GetMapping("/{id}")
    public Booking getBookingById(@PathVariable Long id) {
        Booking booking = bookingService.getBookingById(id);
        if (booking == null) {
            throw new ResourceNotFoundException("Booking not found with id " + id);
        }
        return booking;

    }

    @DeleteMapping("/{id}")
    public String deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return "Booking cancelled";
    }

    @PostMapping("/{id}/allot")
    public ResponseEntity<Booking> allotBooking(@PathVariable Long id) {
        Booking booking = bookingService.allotBooking(id);
        return ResponseEntity.ok(booking);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ex.getMessage();
    }
}
