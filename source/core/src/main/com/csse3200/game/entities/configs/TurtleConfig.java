package com.csse3200.game.entities.configs;

import java.util.Map;

public class TurtleConfig extends BaseEntityConfig {
    private String favouriteFood = "Algae";
    private String soundPath = "sounds/turtle-hiss.mp3";
    private String animalName = "Turtle";
    private int health = 20;
    private int baseAttack = 0;

    protected TurtleConfig() {
        this.hints = Map.of(
                0, new String[]{"Welcome to Animal Kingdom!", "I am Tilly the Turtle."},
                1, new String[]{"This is Turtle specific hint 2.", "We hope you're having fun"}
        );
        this.hintLevel = 0;
        this.currentHint = 0;
    }
}
