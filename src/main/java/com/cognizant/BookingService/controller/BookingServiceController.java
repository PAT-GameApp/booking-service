package com.cognizant.BookingService.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.BookingService.Service.BookingService;
import com.cognizant.BookingService.entity.BookingServiceEntity;
import com.cognizant.BookingService.exception.ResourceNotFoundException;
import com.cognizant.BookingService.feign.GameCatalogFeignClient;
import com.cognizant.BookingService.feign.InventoryServiceFeign;
import com.cognizant.BookingService.feign.UserServiceFeignClient;
import com.cognizant.BookingService.dto.AllotmentDTO;

@RestController
@RequestMapping("/bookings")
public class BookingServiceController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserServiceFeignClient userServiceFeignClient;
    @Autowired
    private GameCatalogFeignClient gameCatalogFeignClient;
    @Autowired
    private InventoryServiceFeign inventoryServiceFeign;

    @PostMapping("/create_booking")
    public String createBooking(@RequestBody BookingServiceEntity booking) {
        if (booking.getUserId() == null) {
            throw new IllegalArgumentException("userId must not be null");
        }
        if (userServiceFeignClient.getUserById(booking.getUserId()) == null) {
            throw new ResourceNotFoundException("User not found with id " + booking.getUserId());
        }
        if (booking.getGameId() == null) {
            throw new IllegalArgumentException("gameId must not be empty");
        }
        if (gameCatalogFeignClient.getGameById(booking.getGameId()) == null) {
            throw new ResourceNotFoundException("Game not found with id " + booking.getGameId());
        }
        if (booking.getAllotmentId() == null) {
            throw new IllegalArgumentException("allotmentId must not be empty");
        }
        AllotmentDTO allotment = inventoryServiceFeign.getAllotmentById(booking.getAllotmentId().intValue());
        if (allotment == null) {
            throw new ResourceNotFoundException("Allotment not found with id " + booking.getAllotmentId());
        }
        bookingService.createBooking(booking);
        return "Booking created";
    }

    @GetMapping("/get_all_booking")
    public List<BookingServiceEntity> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @GetMapping("/get_booking_by_id/{id}")
    public BookingServiceEntity getBookingById(@PathVariable Long id) {
        BookingServiceEntity booking = bookingService.getBookingById(id);
        if (booking == null) {
            throw new ResourceNotFoundException("Booking not found with id " + id);
        }
        return booking;

    }

    @DeleteMapping("/delete_booking/{id}")
    public String deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return "Booking cancelled";
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