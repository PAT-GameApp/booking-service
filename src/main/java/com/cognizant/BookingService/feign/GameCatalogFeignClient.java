package com.cognizant.BookingService.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//import com.cognizant.gamecatalog.entity.Game_Catalog_Entity;

@FeignClient(name = "game-catalog-service", url = "http://localhost:8081")
public interface GameCatalogFeignClient {
    @GetMapping("/games/get_game_by_id/{id}")
    Object getGameById(@PathVariable("id") Long id);
}
