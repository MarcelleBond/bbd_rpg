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

    public Character[][] getMap() {
        return map;
    }

    public void setMap(Character[][] map) {
        this.map = map;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public void setEnemies(List<Enemy> enemies) {
        this.enemies = enemies;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
