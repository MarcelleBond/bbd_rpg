package com.bbd.RPG.models;

import java.util.Random;

public abstract class ItemFactory {
    public static Item newItem(Position position)
    {
        // For now it is just potions, and the hitpoints is the amount of health you gain
        Item item;
        int type = new Random().nextInt(4); // 0 - 3
        
        switch (type) {
            case 0:
                item = new Item(position, 50);
                break;
            case 1:
                item = new Item(position, 100);
                break;
            case 2:
                item = new Item(position, 150);
                break;
            default:
                item = new Item(position, 50);
                break;
        }
        return item;
    }
}
