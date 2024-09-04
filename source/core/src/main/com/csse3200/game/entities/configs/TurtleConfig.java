package com.csse3200.game.entities.configs;

public class TurtleConfig extends BaseFriendlyEntityConfig {
    protected TurtleConfig() {
        this.baseHint = new String[]{"Welcome to Animal Kingdom!", "I am Tilly the Turtle."};
        this.spritePath = "images/turtle.atlas";
        this.animationSpeed = 0.5f;
        this.soundPath = new String[] {"sounds/turtle-hiss.mp3"};
        this.health = 20;
        this.baseAttack = 0;
        this.animalName = "Turtle";
    }
}
