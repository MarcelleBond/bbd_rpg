package com.bbd.RPG.restcontrollers;

import com.bbd.RPG.services.MapService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/map")
public class MapController {

    private MapService mapService;

    public MapController(MapService mapService) {
        this.mapService = mapService;
    }

    @GetMapping("")
    public List<List<Character>> getMap() {
        return mapService.generateMaze(10, 10);
    }
}
