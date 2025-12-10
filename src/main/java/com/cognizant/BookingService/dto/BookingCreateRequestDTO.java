package com.cognizant.BookingService.dto;

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
public class BookingCreateRequestDTO {

    private Long userId; // ref to user

    private Long gameId; // ref to game

    private List<Long> playerIds; // list of player ids

    // private Long allotmentId; // ref after allotment

    private Long equipmentId; // ref to eqiuipment

    private String locationId;

    private LocalDateTime bookingStartTime;

    private LocalDateTime bookingEndTime;
}
