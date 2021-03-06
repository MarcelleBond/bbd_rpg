package com.bbd.RPG.controllers;

import com.bbd.RPG.RpgApplication;
import com.bbd.RPG.models.*;
import com.bbd.RPG.models.stompmessages.InitializeMapIn;
import com.bbd.RPG.services.MapService;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Controller
@RequestMapping("")
public class MapController {
    private MapService mapService;
    public MapController(MapService mapService){
        this.mapService = mapService;
    }

    @GetMapping("/view/map")
    public String test(){
        return "index";
    }


    @GetMapping("/rooms")
    public String rooms(Model model){
        model.addAttribute("map", mapService.generateDivisionMaze(80, 30));
        model.addAttribute("message", "Rooms");
        return "index";
    }
}
