package com.cognizant.BookingService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AllotmentDTO {

    private Long allotmentId;

    private Long equipmentId;

    private boolean returned;

    private Long bookingId;

    private Long userId;
}
