package com.bbd.RPG.models;

import java.util.Random;

public abstract class EnemyFactory {
    public static Enemy newEnemy(Position position)
    {
        int type = new Random().nextInt(4); // 0 - 3
        return new Enemy(position, type);
    }
}
