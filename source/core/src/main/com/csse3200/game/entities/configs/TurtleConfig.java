package com.csse3200.game.entities.configs;

import java.util.Map;

public class TurtleConfig extends BaseEntityConfig {
    private String favouriteFood = "Algae";
    private String soundPath = "sounds/turtle-hiss.mp3";

    protected TurtleConfig() {
        this.baseHint = new String[]{"Welcome to Animal Kingdom!", "I am Tilly the Turtle."};
        this.health = 20;
        this.baseAttack = 0;
        this.animalName = "Turtle";
    }
}
