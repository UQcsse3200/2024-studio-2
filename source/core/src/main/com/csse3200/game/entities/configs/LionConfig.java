package com.csse3200.game.entities.configs;

/**
 * Defines the Lion's statistics stored in lion config files to be loaded by the NPC factory.
 */
public class LionConfig extends BaseEntityConfig {
    private String favouriteFood = "Meat";
    private String soundPath = "sounds/tiger-roar.mp3";

    protected LionConfig() {
        this.baseHint = new String[]{"Welcome to Animal Kingdom!", "I am Lenny the Lion."};
        this.health = 40;
        this.baseAttack = 0;
        this.animalName = "Lion";
    }
}
