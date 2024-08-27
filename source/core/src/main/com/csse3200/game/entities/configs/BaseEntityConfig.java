package com.csse3200.game.entities.configs;

/**
 * Defines a basic set of properties stored in entities config files to be loaded by Entity Factories.
 */
public class BaseEntityConfig {
    public int health = 1; // Remain public as per the base game
    public int baseAttack = 0; // Remain public as per the base game
    protected String animalName;
    protected String[] baseHint;
    protected String spritePath;
    protected float animationSpeed = 0.1f;
    protected String[] soundPath;
    public int hunger = 100;
    public int strength = 0;
    public int defense = 0;
    public int speed = 1;
    public int experience = 0;

    protected BaseEntityConfig() {}

    public String getSpritePath() {
        return this.spritePath;
    }

    public float getAnimationSpeed() {
        return this.animationSpeed;
    }

    public String[] getSoundPath() {
        return this.soundPath;
    }

    public int getHealth() {
        return this.health;
    }

    public String getAnimalName() {
        return animalName;
    }

    public int getBaseAttack () {
        return this.baseAttack;
    }

    public String[] getBaseHint() {
        return baseHint;
    }

}
