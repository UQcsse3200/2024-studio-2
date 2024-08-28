package com.csse3200.game.entities.configs;

import java.util.Map;

public class TurtleConfig extends BaseEntityConfig {
    public String favouriteFood = "Algae";
    public String soundPath = "sounds/turtle-hiss.mp3";
    public String animalName = "Turtle";
    public int health = 20;
    public int baseAttack = 0;

    public static final Map<Integer, String[]> hints = Map.of(
            0, new String[]{"Welcome to Animal Kingdom!", "I am Tilly the Turtle."},
            1, new String[]{"This is Turtle specific hint 2.", "We hope you're having fun"}
    );
    public static int hintLevel = 0;
    public static int currentHint = 0;

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
        return "Turtle";
    }
}
