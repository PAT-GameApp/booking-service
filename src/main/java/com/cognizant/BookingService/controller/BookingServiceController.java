package com.cognizant.BookingService.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.BookingService.service.BookingService;

import com.cognizant.BookingService.entity.Booking;
import com.cognizant.BookingService.exception.ResourceNotFoundException;
import com.cognizant.BookingService.dto.BookingCreateRequestDTO;
import com.cognizant.BookingService.util.PagingUtil;

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
    public ResponseEntity<List<Booking>> getAllBookings(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String direction) {
        List<String> allowedSort = java.util.Arrays.asList("bookingId", "userId", "gameId", "bookingStartTime", "bookingEndTime", "locationId", "allotmentId");
        Pageable pageable = PagingUtil.buildPageable(page, size, sort, direction, allowedSort);
        if (pageable != null) {
            Page<Booking> p = bookingService.getAllBookings(pageable);
            HttpHeaders headers = PagingUtil.buildHeaders(p);
            return ResponseEntity.ok().headers(headers).body(p.getContent());
        }
        Sort sortSpec = PagingUtil.buildSort(sort, direction, allowedSort);
        List<Booking> list = bookingService.getAllBookings(sortSpec);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public Booking getBookingById(@PathVariable Long id) {
        Booking booking = bookingService.getBookingById(id);
        if (booking == null) {
            throw new ResourceNotFoundException("Booking not found with id " + id);
        }
        return booking;

    }

    @GetMapping("/user/{userId}")
    public List<Booking> getBookingsByUserId(@PathVariable Long userId) {
        return bookingService.getBookingsByUserId(userId);
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
