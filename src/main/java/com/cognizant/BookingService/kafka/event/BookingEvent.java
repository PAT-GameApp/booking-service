package com.cognizant.BookingService.kafka.event;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingEvent {
    
    private String eventType; // CREATED, UPDATED, DELETED, ALLOTTED
    private Long bookingId;
    private Long userId;
    private Long gameId;
    private List<Long> playerIds;
    private Long allotmentId;
    private Long equipmentId;
    private String locationId;
    private LocalDateTime bookingStartTime;
    private LocalDateTime bookingEndTime;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime eventTimestamp;
}
