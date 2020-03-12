package com.bbd.RPG.models;

import java.util.Random;

public class Player extends GameCharacter{

    int maxHitPoints = 150;
    public Position position;
    public int health;
    public String name;
    public int level;
    public GameMap map;

    public Player(String name, Position position, GameMap map){
        this.map = map;
        this.name = name;
        this.level = 1;
        this.position = position;
        this.hitPoints = maxHitPoints;
        this.experiencePoints = 0;
    }

    public Player(Position position, int hitPoints, int experiencePoints){
//        this.name = name;
//        this.level = level;
        this.position = position;
        this.hitPoints = hitPoints;
        this.experiencePoints = experiencePoints;
    }

    public void gainExperience(int xp) {
        this.experiencePoints += xp;
    }

    public void gainHitPoints(int hp) {

        if ((this.hitPoints + hp) > maxHitPoints)
            this.hitPoints = maxHitPoints; // set health/hitpoints to the max
        else
            this.hitPoints += hp;

        return;

    }

    public void playerDies() {
        return;
    }

    @Override
    void giveDamage(GameCharacter victim) {
        // Generate random integers in range 0 to 50 (maxAttackPoints + 1)
        int attackPoints = new Random().nextInt(this.maxAttackPoints + 1);

        victim.takeDamage(attackPoints, this);
        return;
    }

    @Override
    void takeDamage(int damage, GameCharacter attacker) {
        this.hitPoints -= damage;

        if (this.hitPoints <= 0)
            playerDies();
    }
}
