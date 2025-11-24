package com.cognizant.BookingService.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cognizant.inventory_service.dto.AllotmentDTO;

@FeignClient(name = "inventory-service", url = "http://localhost:8083")
public interface InventoryServiceFeign {
    @GetMapping("/api/allotments/{id}")
    AllotmentDTO getAllotmentById(@PathVariable("id") int id);
}
