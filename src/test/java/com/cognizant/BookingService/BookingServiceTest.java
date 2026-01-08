package com.cognizant.BookingService;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cognizant.BookingService.dto.AllotmentDTO;
import com.cognizant.BookingService.dto.BookingCreateRequestDTO;
import com.cognizant.BookingService.dto.EquipmentAvailableResponseDTO;
import com.cognizant.BookingService.entity.Booking;
import com.cognizant.BookingService.entity.UserServiceEntity;
import com.cognizant.BookingService.exception.InvalidDurationException;
import com.cognizant.BookingService.exception.InvalidPlayersException;
import com.cognizant.BookingService.feign.InventoryServiceFeign;
import com.cognizant.BookingService.feign.UserServiceFeignClient;
import com.cognizant.BookingService.kafka.producer.BookingEventProducer;
import com.cognizant.BookingService.repository.BookingServiceRepository;
import com.cognizant.BookingService.service.BookingService;

import feign.FeignException;
import feign.Request;
import feign.Response;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    private BookingServiceRepository bookingRepository;

    @Mock
    private UserServiceFeignClient userServiceFeignClient;

    @Mock
    private InventoryServiceFeign inventoryServiceFeign;

    @Mock
    private BookingEventProducer bookingEventProducer;

    @InjectMocks
    private BookingService bookingService;

    @Test
    public void testCreateBooking_DurationMoreThanOneHour_ThrowsException() {
        BookingCreateRequestDTO request = baseRequest();
        request.setBookingEndTime(request.getBookingStartTime().plusMinutes(61));

        when(userServiceFeignClient.getUserById(1L)).thenReturn(new UserServiceEntity());

        assertThrows(InvalidDurationException.class, () -> bookingService.createBooking(request));

        verify(bookingRepository, never()).save(any());
        verify(bookingEventProducer, never()).sendBookingCreatedEvent(any());
    }

    @Test
    public void testCreateBooking_EndTimeBeforeStartTime_ThrowsException() {
        BookingCreateRequestDTO request = baseRequest();
        request.setBookingEndTime(request.getBookingStartTime().minusMinutes(1));

        EquipmentAvailableResponseDTO availableResponse = EquipmentAvailableResponseDTO.builder()
                .availableQuantity(1)
                .build();
        when(inventoryServiceFeign.getEquipmentAvailableCount(any())).thenReturn(availableResponse);
        when(userServiceFeignClient.getUserById(1L)).thenReturn(new UserServiceEntity());

        Booking persisted = Booking.builder()
                .bookingId(1L)
                .userId(request.getUserId())
                .gameId(request.getGameId())
                .equipmentId(request.getEquipmentId())
                .playerIds(request.getPlayerIds())
                .locationId(request.getLocationId())
                .bookingStartTime(request.getBookingStartTime())
                .bookingEndTime(request.getBookingEndTime())
                .build();
        when(bookingRepository.save(any(Booking.class))).thenReturn(persisted);

        AllotmentDTO allotmentResponse = AllotmentDTO.builder()
                .allotmentId(123L)
                .build();
        when(inventoryServiceFeign.createAllotment(any(AllotmentDTO.class))).thenReturn(allotmentResponse);

        // Just ensure no NPE occurs in this scenario
        bookingService.createBooking(request);
    }

    @Test
    public void testCreateBooking_PlayerAlreadyBookedToday_ThrowsException() {
        BookingCreateRequestDTO request = baseRequest();

        when(userServiceFeignClient.getUserById(1L)).thenReturn(new UserServiceEntity());
        when(bookingRepository.existsAnyBookingTodayByPlayers(any(), any(), any())).thenReturn(true);

        assertThrows(InvalidPlayersException.class, () -> bookingService.createBooking(request));

        verify(bookingRepository, never()).save(any());
        verify(bookingEventProducer, never()).sendBookingCreatedEvent(any());
    }

    @Test
    public void testCreateBooking_InvalidPlayer_ThrowsException() {
        BookingCreateRequestDTO request = baseRequest();

        Response response = Response.builder()
                .status(404)
                .reason("Not Found")
                .request(Request.create(Request.HttpMethod.GET, "/players/invalid", Collections.emptyMap(), null, StandardCharsets.UTF_8, null))
                .build();

        FeignException feignException = FeignException.errorStatus("getUserById", response);

        when(userServiceFeignClient.getUserById(1L)).thenThrow(feignException);

        assertThrows(InvalidPlayersException.class, () -> bookingService.createBooking(request));

        verify(bookingRepository, never()).save(any());
        verify(bookingEventProducer, never()).sendBookingCreatedEvent(any());
    }

    @Test
    public void testCreateBooking_AutoAllotmentAndKafkaEvent_Success() {
        BookingCreateRequestDTO request = baseRequest();

        when(userServiceFeignClient.getUserById(1L)).thenReturn(new UserServiceEntity());
        when(bookingRepository.existsAnyBookingTodayByPlayers(any(), any(), any())).thenReturn(false);
        when(bookingRepository.slotAvailable(any(), any())).thenReturn(false);

        EquipmentAvailableResponseDTO equipmentAvailable = EquipmentAvailableResponseDTO.builder()
                .availableQuantity(1)
                .build();
        when(inventoryServiceFeign.getEquipmentAvailableCount(100L)).thenReturn(equipmentAvailable);

        Booking persisted = Booking.builder()
                .bookingId(10L)
                .userId(request.getUserId())
                .gameId(request.getGameId())
                .equipmentId(request.getEquipmentId())
                .playerIds(request.getPlayerIds())
                .locationId(request.getLocationId())
                .bookingStartTime(request.getBookingStartTime())
                .bookingEndTime(request.getBookingEndTime())
                .build();

        when(bookingRepository.save(any(Booking.class)))
                .thenReturn(persisted);

        AllotmentDTO allotmentResponse = AllotmentDTO.builder()
                .allotmentId(999L)
                .build();
        when(inventoryServiceFeign.createAllotment(any(AllotmentDTO.class))).thenReturn(allotmentResponse);

        Booking result = bookingService.createBooking(request);

        verify(inventoryServiceFeign).createAllotment(any(AllotmentDTO.class));
        verify(bookingEventProducer).sendBookingCreatedEvent(any(Booking.class));
    }

    @Test
    public void testAllotBooking_Success() {
        Long bookingId = 1L;
        Booking booking = Booking.builder()
                .bookingId(bookingId)
                .equipmentId(101L)
                .userId(202L)
                .build();

        AllotmentDTO allotmentResponse = AllotmentDTO.builder()
                .allotmentId(303L)
                .build();

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(inventoryServiceFeign.createAllotment(any())).thenReturn(allotmentResponse);
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Booking result = bookingService.allotBooking(bookingId);

        org.junit.jupiter.api.Assertions.assertNotNull(result);
        org.junit.jupiter.api.Assertions.assertEquals(303L, result.getAllotmentId());
        verify(bookingEventProducer).sendBookingAllottedEvent(any(Booking.class));
    }

    @Test
    public void testDeleteBooking_PublishesKafkaEvent() {
        Long bookingId = 5L;

        bookingService.deleteBooking(bookingId);

        verify(bookingRepository).deleteById(bookingId);
        verify(bookingEventProducer).sendBookingDeletedEvent(bookingId);
    }

    private BookingCreateRequestDTO baseRequest() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusMinutes(30);

        return BookingCreateRequestDTO.builder()
                .userId(1L)
                .gameId(2L)
                .equipmentId(100L)
                .playerIds(Collections.singletonList(1L))
                .locationId("LOC-1")
                .bookingStartTime(start)
                .bookingEndTime(end)
                .build();
    }
}
