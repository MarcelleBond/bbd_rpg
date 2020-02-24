package com.bbd.RPG.models;

public class Vector {
    public int x;
    public int y;

    public Vector(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return String.format("{x: %d, y: %d}", x , y);
    }
}
