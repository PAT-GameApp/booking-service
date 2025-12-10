package com.cognizant.BookingService.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    @NotNull
    private Long userId; // ref to user

    @NotNull
    private Long gameId; // ref to game

    // store playerIDs in seperate join table
    @ElementCollection
    @CollectionTable(name = "booking_players", joinColumns = @JoinColumn(name = "booking_id"))
    @Column(name = "player_id")
    private List<Long> playerIds;

    private Long allotmentId; // ref after allotment

    private Long equipmentId; // ref to eqiuipment

    @NotNull
    private String locationId;

    @NotNull
    private LocalDateTime bookingStartTime;

    @NotNull
    private LocalDateTime bookingEndTime;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

}
