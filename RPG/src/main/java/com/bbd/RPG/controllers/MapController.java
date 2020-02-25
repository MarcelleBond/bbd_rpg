package com.bbd.RPG.controllers;

import com.bbd.RPG.services.MapService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RequestMapping("/view/map")
public class MapController {
    private MapService mapService;
    public MapController(MapService mapService){
        this.mapService = mapService;
    }

    @GetMapping("/test")
    public String test(Model model){
        model.addAttribute("map", mapService.generateMaze(40, 15));
        model.addAttribute("message", "Classic");
        return "index";
    }
    @GetMapping("/rooms")
    public String rooms(Model model){
        model.addAttribute("map", mapService.generateDivisionMaze(80, 30));
        model.addAttribute("message", "Rooms");
        return "index";
    }
}
