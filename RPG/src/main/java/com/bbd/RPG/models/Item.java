package com.bbd.RPG.models;

public class Item {

    public Position position;
    public int hitPoints;

    public Item(Position position, int hitPoints){
        this.position = position;
        this.hitPoints = hitPoints;
    }
}
