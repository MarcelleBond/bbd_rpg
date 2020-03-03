package com.bbd.RPG.models;

public class Player {
    public String name;
    public int level;
    public Position position;

    public Player(String name, int level, Position position){
        this.name = name;
        this.level = level;
        this.position = position;
    }
}
