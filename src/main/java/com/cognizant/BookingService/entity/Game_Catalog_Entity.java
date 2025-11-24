package com.cognizant.BookingService.entity;

import jakarta.persistence.*;
import lombok.Data;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data

public class Game_Catalog_Entity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_id")
    private Long game_id;

    @Column(name = "game_name", nullable = false)
    private String game_name;

    @Column(name = "game_locations")
    private String game_locations;

    @Column(name = "game_num_players")
    private Integer game_numPlayers;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime created_at;

    @LastModifiedDate
    @Column(name = "modified_at")
    private LocalDateTime modified_at;

   
}