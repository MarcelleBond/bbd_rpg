package com.bbd.RPG.services;

import com.bbd.RPG.models.*;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {

    @Autowired
    MapService mapService;
    public void generateNewMap(Player player, int width, int height){
        Character[][] map = mapService.generateMaze(width, height);
        Position playerPos = mapService.getMazeStartingPosition(map);
        Pair<List<Item>, List<Enemy>> items_enemies = mapService.getItemsAndEnemies(map);
        player.map = new GameMap(map, items_enemies.getValue1(), items_enemies.getValue0());
        player.position = playerPos;
    }
}
