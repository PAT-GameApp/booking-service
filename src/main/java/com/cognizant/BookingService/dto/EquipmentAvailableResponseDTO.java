package com.cognizant.BookingService.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EquipmentAvailableResponseDTO {
    private int availableQuantity;
}
