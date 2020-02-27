package com.bbd.RPG.controllers;

import com.bbd.RPG.models.stompmessages.Status;
import com.bbd.RPG.models.stompmessages.StatusMessage;
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
    public Status status(StatusMessage statusMessage) throws InterruptedException {
        return new Status(String.format("Name: %s, Status: %s, Level: %s", statusMessage.name, statusMessage.status, statusMessage.level));
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
