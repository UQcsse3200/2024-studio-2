package com.csse3200.game.entities.configs;

public class TurtleConfig extends BaseEntityConfig {
    protected TurtleConfig() {
        this.baseHint = new String[]{"Welcome to Animal Kingdom!", "I am Tilly the Turtle."};
        this.soundPath = new String[] {"sounds/turtle-hiss.mp3"};
        this.health = 20;
        this.baseAttack = 0;
        this.animalName = "Turtle";
    }
}
