package com.csse3200.game.entities.configs;

import java.util.Map;

/**
 * Defines a basic set of properties stored in entities config files to be loaded by Entity Factories.
 */
public class BaseEntityConfig {
    public int health = 1;
    public int baseAttack = 0;
    public String animalName = "";

    public Map<Integer, String[]> hints = null;
    public int hintLevel = 0;
    public int currentHint = 0;

    public String[] getStringHintLevel() {
        return hints.get(hintLevel);
    }

    public void incrementHintLevel() {
        if (hints != null && hintLevel < (hints.size() - 1)) {
            hintLevel = hintLevel + 1;
            restartCurrentHint();
        }
    }

    public int getHintLevel() {
        if (hints != null) {
            return hintLevel;
        }
        return -1;
    }

    public void restartCurrentHint() {
        if (hints != null) {
            this.currentHint = 0;
        }
    }

    public String getCurrentHint() {
        if (hints != null) {
            String[] hint = hints.get(hintLevel);
            return hint[currentHint];
        }
        return "";
    }

    public void incrementCurrentHint() {
        if (hints != null) {
            currentHint = (currentHint + 1) % (hints.get(hintLevel)).length;
        }
    }

    public void decrementCurrentHint() {
        if (hints != null) {
            currentHint = (currentHint - 1) % (hints.get(hintLevel)).length;
        }
    }

    public String getAnimalName() {
        return animalName;
    }
}
