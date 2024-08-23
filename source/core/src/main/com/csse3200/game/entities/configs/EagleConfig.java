package com.csse3200.game.entities.configs;

import java.util.Map;

/**
 * Defines the Eagle's statistics stored in eagle config files to be loaded by the NPC factory.
 */
public class EagleConfig extends BaseEntityConfig {
    public int health = 25;
    public int baseAttack = 0;
    
    protected String favouriteFood = "Fish";
    protected String animalName = "Eagle";
    protected String soundPath = "sounds/eagle-scream.mp3";

    protected EagleConfig() {
        this.hints = Map.of(
                0, new String[]{"Welcome to Animal Kingdom!", "I am Ethan the Eagle."},
                1, new String[]{"This is eagle specific hint 2.", "We hope you're having fun"}
        );
        this.hintLevel = 0;
        this.currentHint = 0;
    }
}