package com.cognizant.BookingService.kafka.producer;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.cognizant.BookingService.entity.Booking;
import com.cognizant.BookingService.kafka.event.BookingEvent;
import com.cognizant.BookingService.kafka.event.BookingEventType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingEventProducer {

    private final KafkaTemplate<String, BookingEvent> kafkaTemplate;

    @Value("${kafka.topic.booking-events:booking-events}")
    private String bookingEventsTopic;

    public void sendBookingCreatedEvent(Booking booking) {
        sendBookingEvent(booking, BookingEventType.CREATED);
    }

    public void sendBookingUpdatedEvent(Booking booking) {
        sendBookingEvent(booking, BookingEventType.UPDATED);
    }

    public void sendBookingDeletedEvent(Long bookingId) {
        BookingEvent event = BookingEvent.builder()
                .eventType(BookingEventType.DELETED)
                .bookingId(bookingId)
                .eventTimestamp(LocalDateTime.now())
                .build();
        
        sendEvent(String.valueOf(bookingId), event);
    }

    public void sendBookingAllottedEvent(Booking booking) {
        sendBookingEvent(booking, BookingEventType.ALLOTTED);
    }

    private void sendBookingEvent(Booking booking, String eventType) {
        BookingEvent event = BookingEvent.builder()
                .eventType(eventType)
                .bookingId(booking.getBookingId())
                .userId(booking.getUserId())
                .gameId(booking.getGameId())
                .playerIds(booking.getPlayerIds())
                .allotmentId(booking.getAllotmentId())
                .equipmentId(booking.getEquipmentId())
                .locationId(booking.getLocationId())
                .bookingStartTime(booking.getBookingStartTime())
                .bookingEndTime(booking.getBookingEndTime())
                .createdAt(booking.getCreatedAt())
                .modifiedAt(booking.getModifiedAt())
                .eventTimestamp(LocalDateTime.now())
                .build();

        sendEvent(String.valueOf(booking.getBookingId()), event);
    }

    private void sendEvent(String key, BookingEvent event) {
        log.info("Sending booking event: {} for bookingId: {}", event.getEventType(), event.getBookingId());
        
        kafkaTemplate.send(bookingEventsTopic, key, event)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("Successfully sent event {} for booking {} to partition {} with offset {}",
                                event.getEventType(),
                                event.getBookingId(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    } else {
                        log.error("Failed to send event {} for booking {}: {}",
                                event.getEventType(),
                                event.getBookingId(),
                                ex.getMessage());
                    }
                });
    }
}
