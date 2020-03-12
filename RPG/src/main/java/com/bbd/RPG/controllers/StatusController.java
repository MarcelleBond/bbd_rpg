package com.bbd.RPG.controllers;

import com.bbd.RPG.RpgApplication;
import com.bbd.RPG.models.*;
import com.bbd.RPG.models.stompmessages.InitializeMapIn;
import com.bbd.RPG.models.stompmessages.StatusOut;
import com.bbd.RPG.models.stompmessages.StatusIn;
import com.bbd.RPG.services.MapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.*;

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
