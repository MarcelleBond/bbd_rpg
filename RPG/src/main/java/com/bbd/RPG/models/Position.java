package com.bbd.RPG.models;

public class Position {
    public int x;
    public int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return String.format("{x: %d, y: %d}", x , y);
    }
}
