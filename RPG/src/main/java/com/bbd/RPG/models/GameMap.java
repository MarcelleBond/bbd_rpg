package com.bbd.RPG.models;

import java.util.List;

public class GameMap {
    public Character[][] map;
    public List<Enemy> enemies;
    public List<Item> items;

    public GameMap(Character[][] map, List<Enemy> enemies, List<Item> items) {
        this.map = map;
        this.enemies = enemies;
        this.items = items;
    }
}
