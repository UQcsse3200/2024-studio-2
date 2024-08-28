package com.csse3200.game.entities.configs;

/**
 * Defines the Lion's statistics stored in lion config files to be loaded by the NPC factory.
 */
public class LionConfig extends BaseEntityConfig {

    protected LionConfig() {
        this.baseHint = new String[]{"Welcome to Animal Kingdom!", "I am Lenny the Lion."};
        this.spritePath = "images/lion.atlas";
        this.animationSpeed = 0.2f;
        this.soundPath = new String[] {"sounds/tiger-roar.mp3"};
        this.health = 40;
        this.baseAttack = 0;
        this.animalName = "Lion";
    }
}
