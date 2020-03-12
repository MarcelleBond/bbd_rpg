package com.bbd.RPG.controllers;

import com.bbd.RPG.models.Player;
import com.bbd.RPG.models.Position;
import com.bbd.RPG.models.stompmessages.StatusOut;
import com.bbd.RPG.models.stompmessages.StatusIn;
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
    @MessageMapping("/status")
    @SendTo("/player/status")
    public String status(StatusIn statusIn) throws InterruptedException {
        return String.format("Name: %s, Status: %s, Level: %s", statusIn.name, statusIn.status, statusIn.level);
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
