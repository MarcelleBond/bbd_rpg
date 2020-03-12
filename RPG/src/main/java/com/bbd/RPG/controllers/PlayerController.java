package com.bbd.RPG.controllers;

import com.bbd.RPG.models.*;
import com.bbd.RPG.models.stompmessages.InitializeMapIn;
import com.bbd.RPG.models.stompmessages.StatusIn;
import com.bbd.RPG.services.MapService;
import com.bbd.RPG.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.*;

@Controller
public class PlayerController {
    private Map<String, Player> activePlayers = new HashMap<>();

    @Autowired
    MapService mapService;

    @Autowired
    PlayerService playerService;

    @MessageMapping("/initializeMap/{playerName}")
    @SendTo("/map/initialMap/{playerName}")
    public Player initializeMap(@DestinationVariable String playerName, InitializeMapIn size){
        playerService.generateNewMap(activePlayers.get(playerName), size.x, size.y);
        return activePlayers.get(playerName);
    }

    @MessageMapping("/status")
    @SendTo("/player/status")
    public String status(StatusIn statusIn) throws InterruptedException {
        return String.format("Name: %s, Status: %s, Level: %s", statusIn.name, statusIn.status, statusIn.level);
    }

    @MessageMapping("/player/move/{player}")
    @SendTo("/player/active")
    public Map<String, Player> updatePosition(@DestinationVariable String player, Position newPosition){
        activePlayers.get(player).position = newPosition;
        return activePlayers;
    }

    @MessageMapping("/join/{player}")
    @SendTo("/player/active")
    public Map<String, Player> playerJoined(@DestinationVariable String player){
        Player newPlayer = new Player(player, null, null);
        playerService.generateNewMap(newPlayer, 10, 10);
        activePlayers.put(player, newPlayer);
        return activePlayers;
    }

    @MessageMapping("/leave/{player}")
    @SendTo("/player/active")
    public Map<String, Player> playerLeft(@DestinationVariable String player){
        activePlayers.remove(player);
        return activePlayers;
    }

    @MessageMapping("/player/levelup/{player}")
    @SendTo("/player/active")
    public Map<String, Player> levelupPlayer(@DestinationVariable String player){
        activePlayers.get(player).level++;
        return activePlayers;
    }

    @MessageMapping("/player/update/{playerName}")
    @SendTo("/player/active")
    public Map<String, Player> updatePlayer(@DestinationVariable String playerName, Player player){
        activePlayers.put(playerName, player);
        return activePlayers;
    }


}
