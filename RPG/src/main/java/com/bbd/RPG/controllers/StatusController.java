package com.bbd.RPG.controllers;

import com.bbd.RPG.models.stompmessages.StatusOut;
import com.bbd.RPG.models.stompmessages.StatusIn;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.HashSet;
import java.util.Set;

@Controller
public class StatusController {
    private Set<String> activePlayers = new HashSet<>();

    @MessageMapping("/status")
    @SendTo("/player/status")
    public String status(StatusIn statusIn) throws InterruptedException {
        return String.format("Name: %s, Status: %s, Level: %s", statusIn.name, statusIn.status, statusIn.level);
    }

    @MessageMapping("/join")
    @SendTo("/player/active")
    public Set<String> playerJoined(String player){
        activePlayers.add(player);
        return activePlayers;
    }

    @MessageMapping("/leave")
    @SendTo("/player/active")
    public Set<String> playerLeft(String player){
        activePlayers.remove(player);
        return activePlayers;
    }

}
