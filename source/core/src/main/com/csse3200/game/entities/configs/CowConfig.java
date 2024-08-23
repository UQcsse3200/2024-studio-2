package com.csse3200.game.entities.configs;

import java.util.Map;

/**
 * Defines the Cow's statistics stored in cow config files to be loaded by the NPC factory.
 */
public class CowConfig extends BaseEntityConfig {
    public final String favouriteColour = "Hay";
    public final String soundPath = "sounds/mooing-cow.mp3";

    public CowConfig() {
        this.hints = Map.of(
                0, new String[]{"Welcome to Animal Kingdom!", "I am Charlie the Cow."},
                1, new String[]{"This is cow specific hint 2.", "We hope you're having fun"}
        );;
        this.hintLevel = 0;
        this.currentHint = 0;
        this.health = 30;
        this.baseAttack = 0;
        this.animalName =  "Cow";
    }
}
