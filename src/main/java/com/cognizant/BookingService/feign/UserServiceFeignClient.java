package com.cognizant.BookingService.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cognizant.BookingService.entity.UserServiceEntity;

@FeignClient(name = "user-service")
public interface UserServiceFeignClient {
    @GetMapping("/users/{id}")
    UserServiceEntity getUserById(@PathVariable("id") Long id);
}
