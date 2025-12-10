package com.cognizant.BookingService.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cognizant.BookingService.dto.BookingCreateRequestDTO;
import com.cognizant.BookingService.dto.EquipmentAvailableResponseDTO;
import com.cognizant.BookingService.entity.Booking;
import com.cognizant.BookingService.entity.UserServiceEntity;
import com.cognizant.BookingService.exception.InvalidDurationException;
import com.cognizant.BookingService.exception.InvalidPlayersException;
import com.cognizant.BookingService.feign.GameCatalogFeignClient;
import com.cognizant.BookingService.feign.InventoryServiceFeign;
import com.cognizant.BookingService.feign.UserServiceFeignClient;
import com.cognizant.BookingService.repository.BookingServiceRepository;

import feign.FeignException;

@Service
public class BookingService {
    @Autowired
    private BookingServiceRepository bookingRepository;
    @Autowired
    private UserServiceFeignClient userServiceFeignClient;
    // @Autowired
    // private GameCatalogFeignClient gameCatalogFeignClient;
    @Autowired
    private InventoryServiceFeign inventoryServiceFeign;

    public Booking createBooking(BookingCreateRequestDTO request) {
        // check if all playerIds are valid
        for (Long playerId : request.getPlayerIds()) {
            try {
                UserServiceEntity userById = userServiceFeignClient.getUserById(playerId);
            } catch (FeignException e) {
                throw new InvalidPlayersException("Please Enter Valid Player IDs");
            }
        }
        if (Duration.between(request.getBookingStartTime(), request.getBookingEndTime()).toHours() > 1) {
            throw new InvalidDurationException("Cannot book for more than one hour");
        }
        if (Duration.between(request.getBookingStartTime(), request.getBookingEndTime()).toHours() < 0) {
            throw new InvalidDurationException("End time must be after start time");
        }
        // find if any player already booked today
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.plusDays(1).atStartOfDay();
        if (bookingRepository.existsAnyBookingTodayByPlayers(request.getPlayerIds(), startOfDay, endOfDay)) {
            throw new InvalidPlayersException("One player can only play one game a day");
        }

        // check if slot is empty and equipment available?
        EquipmentAvailableResponseDTO availableResponse = inventoryServiceFeign
                .getEquipmentAvailableCount(request.getEquipmentId());
        int availableCount = availableResponse.getAvailableQuantity();

        // TODO - save booking first and then check if it can be auto allocated

        return null;
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking getBookingById(Long id) {
        Optional<Booking> booking = bookingRepository.findById(id);
        return booking.orElse(null);
    }

    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }
}
