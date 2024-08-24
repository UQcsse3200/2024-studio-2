package com.csse3200.game.entities.configs;

import java.util.Map;

/**
 * Defines a basic set of properties stored in entities config files to be loaded by Entity Factories.
 */
public class BaseEntityConfig {
    public int health = 1; // Remain public as per the base game
    public int baseAttack = 0; // Remain public as per the base game
    protected String animalName = "";
    protected String[] baseHint = null;
    protected String[] soundPath = null;

    protected BaseEntityConfig() {}

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
