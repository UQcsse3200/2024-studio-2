package com.csse3200.game.entities.configs;

import java.util.Map;

/**
 * Defines the Lion's statistics stored in lion config files to be loaded by the NPC factory.
 */
public class LionConfig extends BaseEntityConfig {
    private String favouriteFood = "Meat";
    private String animalName = "Lion";
    private String soundPath = "sounds/tiger-roar.mp3";
    private int health = 40;
    private int baseAttack = 0;

    protected LionConfig() {
        this.hints = Map.of(
                0, new String[]{"Welcome to Animal Kingdom!", "I am Lenny the Lion."},
                1, new String[]{"This is lion specific hint 2.", "We hope you're having fun"}
        );
        this.hintLevel = 0;
        this.currentHint = 0;
    }
}
