package com.bbd.RPG.controllers;

import com.bbd.RPG.RpgApplication;
import com.bbd.RPG.models.*;
import com.bbd.RPG.models.stompmessages.InitializeMapIn;
import com.bbd.RPG.services.MapService;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
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
    @MessageMapping("/initializeMap/{playerName}")
    @SendTo("/map/initialMap/{playerName}")
    public Map initializeMap(InitializeMapIn size){
        Map result = new HashMap();
        Character[][] map = mapService.generateMaze(size.x, size.y);
        Position playerPos = mapService.getMazeStartingPosition(map);

        RpgApplication.game.map = map;
        RpgApplication.game.player = new Player(playerPos);

        int avaliableSpaces = 0;
        for (Character[] characters : map) {
            for (Character chars : characters) {
                if (chars == '0')
                    avaliableSpaces++;
            }
        }

        // find avaliable space and add enemies to the map
        int enemyAmount = (avaliableSpaces > 10) ? new Random().nextInt((int)(Math.ceil(avaliableSpaces / 10) + 1)) : 1; //new Random().nextInt((int)(Math.ceil(map.length / 3) + 1)) ;
        avaliableSpaces -= enemyAmount;
        System.out.println(enemyAmount);
        while (enemyAmount >= 0) {
            int xPosition = new Random().nextInt(map.length) ;
            int yPosition = new Random().nextInt(map.length) ;

            if (map[xPosition][yPosition] == '0')
            {
                Position position = new Position(xPosition, yPosition);
                Enemy enemy = EnemyFactory.newEnemy(position);
                if (enemy != null)
                    RpgApplication.game.enemies.add(enemy);
                enemyAmount--;
            }
        }

        // find avaliable space add items to the map
        int itemAmount = (avaliableSpaces > 10) ? new Random().nextInt((int)(Math.ceil(avaliableSpaces / 10) + 1)) : 1; //new Random().nextInt((int)(Math.ceil(map.length / 3) + 1)) ;
        while (itemAmount >= 0) {
            int xPosition = new Random().nextInt(map.length) ;
            int yPosition = new Random().nextInt(map.length) ;

            if (map[xPosition][yPosition] == '0')
            {
                Position position = new Position(xPosition, yPosition);
                Item item = ItemFactory.newItem(position);
                if (item != null)
                    RpgApplication.game.items.add(item);
                itemAmount--;
            }
        }

        result.put("map", map);
        result.put("player", playerPos);
        return result;
    }

    @GetMapping("/rooms")
    public String rooms(Model model){
        model.addAttribute("map", mapService.generateDivisionMaze(80, 30));
        model.addAttribute("message", "Rooms");
        return "index";
    }
}
