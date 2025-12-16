package com.cognizant.BookingService;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cognizant.BookingService.dto.BookingCreateRequestDTO;
import com.cognizant.BookingService.entity.Booking;
import com.cognizant.BookingService.entity.UserServiceEntity;
import com.cognizant.BookingService.exception.InvalidDurationException;
import com.cognizant.BookingService.exception.InvalidPlayersException;
import com.cognizant.BookingService.feign.InventoryServiceFeign;
import com.cognizant.BookingService.feign.UserServiceFeignClient;
import com.cognizant.BookingService.repository.BookingServiceRepository;
import com.cognizant.BookingService.service.BookingService;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    private BookingServiceRepository bookingRepository;

    @Mock
    private UserServiceFeignClient userServiceFeignClient;

    @Mock
    private InventoryServiceFeign inventoryServiceFeign;

    @InjectMocks
    private BookingService bookingService;

    @Test
    public void testCreateBooking_DurationMoreThanOneHour_ThrowsException() {
        BookingCreateRequestDTO request = new BookingCreateRequestDTO();
        request.setPlayerIds(Collections.singletonList(1L));
        request.setBookingStartTime(LocalDateTime.now());
        request.setBookingEndTime(LocalDateTime.now().plusMinutes(61));

        when(userServiceFeignClient.getUserById(1L)).thenReturn(new UserServiceEntity());

        assertThrows(InvalidDurationException.class, () -> bookingService.createBooking(request));
    }

    @Test
    public void testCreateBooking_PlayerAlreadyBookedToday_ThrowsException() {
        BookingCreateRequestDTO request = new BookingCreateRequestDTO();
        request.setPlayerIds(Collections.singletonList(1L));
        request.setBookingStartTime(LocalDateTime.now());
        request.setBookingEndTime(LocalDateTime.now().plusMinutes(60));

        when(userServiceFeignClient.getUserById(1L)).thenReturn(new UserServiceEntity());
        when(bookingRepository.existsAnyBookingTodayByPlayers(any(), any(), any())).thenReturn(true);

        assertThrows(InvalidPlayersException.class, () -> bookingService.createBooking(request));
    }

    @Test
    public void testAllotBooking_Success() {
        Long bookingId = 1L;
        Booking booking = new Booking();
        booking.setBookingId(bookingId);
        booking.setEquipmentId(101L);
        booking.setUserId(202L);

        com.cognizant.BookingService.dto.AllotmentDTO allotmentResponse = com.cognizant.BookingService.dto.AllotmentDTO
                .builder()
                .allotmentId(303L)
                .build();

        when(bookingRepository.findById(bookingId)).thenReturn(java.util.Optional.of(booking));
        when(inventoryServiceFeign.createAllotment(any())).thenReturn(allotmentResponse);
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Booking result = bookingService.allotBooking(bookingId);

        org.junit.jupiter.api.Assertions.assertNotNull(result);
        org.junit.jupiter.api.Assertions.assertEquals(303L, result.getAllotmentId());
    }
}
