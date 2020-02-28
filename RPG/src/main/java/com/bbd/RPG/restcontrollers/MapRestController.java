package com.bbd.RPG.restcontrollers;

import com.bbd.RPG.services.MapService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/map")
public class MapRestController {

    private MapService mapService;

    public MapRestController(MapService mapService) {
        this.mapService = mapService;
    }

    @GetMapping("")
    public Character[][] getMap() {
        return mapService.generateMaze(40, 20);
    }
    @GetMapping("/rooms")
    public Character[][] getMapWithRooms() {
        return mapService.generateDivisionMaze(80, 40);
    }
}
