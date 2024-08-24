package com.csse3200.game.entities.configs;

import java.util.Map;

/**
 * Defines the Cow's statistics stored in cow config files to be loaded by the NPC factory.
 */
public class CowConfig extends BaseEntityConfig {
    public final String favouriteColour = "Hay";
    public final String soundPath = "sounds/mooing-cow.mp3";
    public final String animalName = "Cow";
    public final int health = 30;
    public final int baseAttack = 0;
    public static final String[] baseMessage = new String[]{"Welcome to Animal Kingdom!", "I am Charlie the Cow."};
    public static int hintLevel = 0;
    public static int currentHint = 0;

    public String[] getStringHintLevel() {
        return hints.get(hintLevel);
    }

    public int getHintLevel() {
        if (hints != null) {
            return hintLevel;
        }
        return -1;
    }

}
