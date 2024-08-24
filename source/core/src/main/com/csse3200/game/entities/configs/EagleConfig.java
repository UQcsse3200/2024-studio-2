package com.csse3200.game.entities.configs;

import java.util.Map;

/**
 * Defines the Eagle's statistics stored in eagle config files to be loaded by the NPC factory.
 */
public class EagleConfig extends BaseEntityConfig {
    protected String favouriteFood = "Fish";
    protected String soundPath = "sounds/eagle-scream.mp3";

    protected EagleConfig() {
        this.baseHint = new String[]{"Welcome to Animal Kingdom!", "I am Ethan the Eagle."};
        this.health = 25;
        this.baseAttack = 0;
        this.animalName = "Eagle";
    }
}