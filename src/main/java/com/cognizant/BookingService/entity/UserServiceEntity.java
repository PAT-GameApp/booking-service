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

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long user_id;
	private String user_name;
	private String email;
	private String phone_number;
	private String role;
	private String department;
	private String office_location;
	@CreatedDate
	@Column(name = "created_at", updatable = false)
	private LocalDateTime created_at;

	@LastModifiedDate
	@Column(name = "modified_at")
	private LocalDateTime modified_at;

}
