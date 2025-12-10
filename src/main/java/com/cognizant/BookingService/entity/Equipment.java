package com.cognizant.BookingService.entity;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "allotments") // Prevent circular reference
public class Equipment {

    private Long equipmentId;

    private String equipmentName;

    private int equipmentQuantity;

    private Long gameId; // Reference to external Game Catalog Service

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    private List<Allotment> allotments;

}
