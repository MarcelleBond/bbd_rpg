package com.bbd.RPG.models;

import com.bbd.RPG.RpgApplication;

import java.util.Random;

public class Enemy extends GameCharacter{

    int maxHitPoints;
    int dropExperiencePoints;

    public Enemy() {super();}

    public Enemy(Position position, int experiencePoints){
        this.position = position;
        this.experiencePoints = experiencePoints;
        this.maxHitPoints = getMaxHitPointsFromExperiencePoints(this.experiencePoints);


        this.hitPoints = this.maxHitPoints;
        this.dropExperiencePoints = maxHitPoints;
    }

    public Enemy(Position position, int hitPoints, int experiencePoints){
        this.position = position;
        this.experiencePoints = experiencePoints;
        this.hitPoints = hitPoints;


        this.maxHitPoints = getMaxHitPointsFromExperiencePoints(this.experiencePoints);
        this.dropExperiencePoints = maxHitPoints;
    }

    private int getMaxHitPointsFromExperiencePoints(int xp)
    {
        int hp;

        switch (xp) {
            case 0:
                hp = 50;
                break;
            case 1:
                hp = 100;
                break;
            case 2:
                hp = 150;
                break;
            default:
                hp = 50;
                break;
        }

        return hp;
    }

    @Override
    void giveDamage(GameCharacter victim) {

        boolean willMiss = new Random().nextBoolean(); // let's make it that the enemies can sometimes miss

        int attackPoints = (willMiss ? new Random().nextInt(this.maxAttackPoints + 1) : 0);

        victim.takeDamage(attackPoints, this);
        return;
    }

    @Override
    void takeDamage(int damage, GameCharacter attacker) {
        this.hitPoints -= damage;

        if (this.hitPoints <= 0) {
            attacker.experiencePoints += dropExperiencePoints; // drop XP for the player

            // remove from enemy list
            if (RpgApplication.game.enemies.contains(this))
                RpgApplication.game.enemies.remove(this);
        }
    }
}
