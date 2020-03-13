package com.bbd.RPG.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Position {
    public int x;
    public int y;
    public Position(){super();}
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return String.format("{x: %d, y: %d}", x , y);
    }
}
