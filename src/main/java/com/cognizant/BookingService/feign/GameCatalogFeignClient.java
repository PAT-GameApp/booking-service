package com.cognizant.BookingService.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cognizant.BookingService.entity.Game_Catalog_Entity;

//import com.cognizant.gamecatalog.entity.Game_Catalog_Entity;

@FeignClient(name = "game-catalog")
public interface GameCatalogFeignClient {
    @GetMapping("/games/{id}")
    Object getGameById(@PathVariable("id") Long id);
}
