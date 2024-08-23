package com.csse3200.game.entities.configs;

import java.util.Map;

/**
 * Defines the Lion's statistics stored in lion config files to be loaded by the NPC factory.
 */
public class LionConfig extends BaseEntityConfig {

    public String favouriteFood = "Meat";
    public String animalName = "Lion";
    public String soundPath = "sounds/tiger-roar.mp3";
    public int health = 40;
    public int baseAttack = 0;

    public static final Map<Integer, String[]> hints = Map.of(
            0, new String[]{"Welcome to Animal Kingdom!", "I am Lenny the Lion."},
            1, new String[]{"This is lion specific hint 2.", "We hope you're having fun"}
    );
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

    public String getAnimalName() {
        return "Lion";
    }
}
