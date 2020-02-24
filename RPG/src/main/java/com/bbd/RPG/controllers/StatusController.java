package com.bbd.RPG.controllers;

import com.bbd.RPG.models.stompmessages.Status;
import com.bbd.RPG.models.stompmessages.StatusMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class StatusController {

    @MessageMapping("/status")
    @SendTo("/player/status")
    public Status status(StatusMessage statusMessage) throws InterruptedException {
        return new Status(String.format("%s, %s, %s", statusMessage.name, statusMessage.status, statusMessage.level));
    }
}
