package com.bbd.RPG.models;

public abstract class GameCharacter {

    public Position position;
    public int hitPoints;
    public int experiencePoints;
    public int maxAttackPoints = 50;
    //    public int level;
    //    public String name;

    abstract void giveDamage(GameCharacter victim);
    abstract void takeDamage(int damage, GameCharacter attacker);

}
