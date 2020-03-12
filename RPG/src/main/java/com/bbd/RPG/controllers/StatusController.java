package com.bbd.RPG.controllers;

import com.bbd.RPG.RpgApplication;
import com.bbd.RPG.models.Player;
import com.bbd.RPG.models.Position;
import com.bbd.RPG.models.stompmessages.InitializeMapIn;
import com.bbd.RPG.models.stompmessages.StatusOut;
import com.bbd.RPG.models.stompmessages.StatusIn;
import com.bbd.RPG.services.MapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Controller
public class StatusController {
    private Map<String, Player> activePlayers = new HashMap<>();

    @Autowired
    MapService mapService;

    @MessageMapping("/initializeMap/{playerName}")
    @SendTo("/map/initialMap/{playerName}")
    public Map initializeMap(@DestinationVariable String playerName, InitializeMapIn size){
        Map result = new HashMap();
        Character[][] map = mapService.generateMaze(size.x, size.y);
        Position playerPos = mapService.getMazeStartingPosition(map);

        RpgApplication.game.map = map;
        RpgApplication.game.player = new Player(playerName, playerPos);

        result.put("map", map);
        result.put("player", playerPos);
        return result;
    }

    @MessageMapping("/status")
    @SendTo("/player/status")
    public String status(StatusIn statusIn) throws InterruptedException {
        return String.format("Name: %s, Status: %s, Level: %s", statusIn.name, statusIn.status, statusIn.level);
    }

    @MessageMapping("/player/move/{player}")
    @SendTo("/player/active")
    public Map<String, Player> updatePosition(@DestinationVariable String player, Position newPosition){
        System.out.println(player);
        activePlayers.get(player).position = newPosition;
        return activePlayers;
    }

    @MessageMapping("/join/{player}")
    @SendTo("/player/active")
    public Map<String, Player> playerJoined(@DestinationVariable String player){
        Player newPlayer = new Player(player, new Position(0, 0));
        activePlayers.put(player, newPlayer);
        System.out.println(activePlayers);
        return activePlayers;
    }

    @MessageMapping("/leave/{player}")
    @SendTo("/player/active")
    public Map<String, Player> playerLeft(@DestinationVariable String player){
        activePlayers.remove(player);
        return activePlayers;
    }


}
