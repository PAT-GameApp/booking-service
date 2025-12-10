package com.cognizant.BookingService.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
public class UserServiceEntity {
    private Long userId;
    private String userName;
    private String email;
    private String phoneNumber;
    private String role;
    private String department;
    private String officeLocation;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
