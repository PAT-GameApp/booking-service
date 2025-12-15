package com.cognizant.BookingService.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.cognizant.BookingService.dto.AllotmentDTO;
import com.cognizant.BookingService.dto.EquipmentAvailableResponseDTO;

@FeignClient(name = "inventory-service", url = "http://localhost:8083")
public interface InventoryServiceFeign {
    @GetMapping("/allotments/{id}")
    AllotmentDTO getAllotmentById(@PathVariable("id") int id);

    @GetMapping("/equipment/{id}/available")
    EquipmentAvailableResponseDTO getEquipmentAvailableCount(@PathVariable("id") Long id);

    @PostMapping("/allotments/")
    AllotmentDTO createAllotment(@RequestBody AllotmentDTO dto);
}
