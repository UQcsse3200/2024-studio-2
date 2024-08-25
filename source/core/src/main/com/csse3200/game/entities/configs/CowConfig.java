package com.csse3200.game.entities.configs;

/**
 * Defines the Cow's statistics stored in cow config files to be loaded by the NPC factory.
 */
public class CowConfig extends BaseEntityConfig {

    public CowConfig() {
        this.baseHint = new String[]{"Welcome to Animal Kingdom!", "I am Charlie the Cow."};
        this.health = 30;
        this.baseAttack = 0;
        this.animalName = "Cow";
        this.soundPath = new String[] {"sounds/mooing-cow.mp3"};
    }
}
