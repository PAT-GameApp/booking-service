package com.cognizant.BookingService.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "equipment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "allotments") // Prevent circular reference
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "equipment_id")
    private Long equipment_id;

    @Column(name = "equipment_name", nullable = false)
    private String equipment_name;

    @Column(name = "equipment_quantity", nullable = false)
    private int equipment_quantity;

    @Column(name = "game_id", nullable = false)
    private Long game_id; // Reference to external Game Catalog Service

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime created_at;

    @Column(name = "modified_at", nullable = false)
    private LocalDateTime modified_at;

    @OneToMany(mappedBy = "equipment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Allotment> allotments;

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