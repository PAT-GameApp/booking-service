package com.cognizant.BookingService.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cognizant.BookingService.entity.Booking;

import org.springframework.data.repository.query.Param;

public interface BookingServiceRepository extends JpaRepository<Booking, Long> {
        // find if the players exist in any booking made today
        @Query("""
                        SELECT COUNT(b) > 0
                        FROM Booking b JOIN b.playerIds p
                        WHERE p IN :players
                        AND b.bookingStartTime >= :startOfDay
                        AND b.bookingStartTime < :endOfDay
                        """)
        boolean existsAnyBookingTodayByPlayers(
                        @Param("players") List<Long> players,
                        @Param("startOfDay") LocalDateTime startOfDay,
                        @Param("endOfDay") LocalDateTime endOfDay);

        // find if slot is available
        @Query("""
                        SELECT COUNT(b) > 0
                        FROM Booking b
                        WHERE b.bookingStartTime < :endTime
                        AND b.bookingEndTime > :startTime
                        """)
        boolean slotAvailable(@Param("startTime") LocalDateTime startTime,
                        @Param("endTime") LocalDateTime endTime);

}
