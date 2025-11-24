package com.cognizant.BookingService.entity;
 
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import com.cognizant.inventory_service.entity.Equipment;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "equipment") // Prevent circular reference
public class Allotment {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int allotment_id;
 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id", nullable = false)
    private Equipment equipment;
 
    @Column(nullable = false)
    private int user_id;
 
    @Column(nullable = false)
    private LocalDateTime created_at = LocalDateTime.now();
 
    @Column(nullable = false)
    private LocalDateTime modified_at = LocalDateTime.now();
 
    @PrePersist
    protected void onCreate() {
        this.created_at = LocalDateTime.now();
        this.modified_at = LocalDateTime.now();
    }
 
    @PreUpdate
    protected void onUpdate() {
        this.modified_at = LocalDateTime.now();
    }
}