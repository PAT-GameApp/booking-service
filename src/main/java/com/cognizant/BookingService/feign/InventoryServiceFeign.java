package com.cognizant.BookingService.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cognizant.BookingService.dto.AllotmentDTO;

@FeignClient(name = "inventory-service")
public interface InventoryServiceFeign {
    @GetMapping("/allotments/{id}")
    AllotmentDTO getAllotmentById(@PathVariable("id") int id);
}
