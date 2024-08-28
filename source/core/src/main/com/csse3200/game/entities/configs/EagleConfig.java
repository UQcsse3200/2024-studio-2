package com.csse3200.game.entities.configs;

/**
 * Defines the Eagle's statistics stored in eagle config files to be loaded by the NPC factory.
 */
public class EagleConfig extends BaseEntityConfig {
    protected EagleConfig() {
        this.baseHint = new String[]{"Welcome to Animal Kingdom!", "I am Ethan the Eagle."};
        this.spritePath = "images/eagle.atlas";
        this.animationSpeed = 0.1f;
        this.soundPath = new String[] {"sounds/eagle-scream.mp3"};
        this.health = 25;
        this.baseAttack = 0;
        this.animalName = "Eagle";
    }
}