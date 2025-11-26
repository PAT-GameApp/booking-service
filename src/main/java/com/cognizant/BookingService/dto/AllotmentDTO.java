package com.cognizant.BookingService.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class AllotmentDTO {

    private Long allotment_id; // Optional for create

    @NotNull(message = "Equipment ID cannot be null")
    private Long equipment_id;

    @Min(value = 1, message = "User ID must be greater than 0")
    private int user_id;

    private LocalDateTime created_at;
    private LocalDateTime modified_at;

    // Getters and Setters
    public Long getAllotment_id() {
        return allotment_id;
    }

    public void setAllotment_id(Long allotment_id) {
        this.allotment_id = allotment_id;
    }

    public Long getEquipment_id() {
        return equipment_id;
    }

    public void setEquipment_id(Long equipment_id) {
        this.equipment_id = equipment_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public LocalDateTime getModified_at() {
        return modified_at;
    }

    public void setModified_at(LocalDateTime modified_at) {
        this.modified_at = modified_at;
    }
}