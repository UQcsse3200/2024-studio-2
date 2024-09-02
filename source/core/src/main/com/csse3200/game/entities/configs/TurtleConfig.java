package com.csse3200.game.entities.configs;

public class TurtleConfig extends BaseEntityConfig {
    protected TurtleConfig() {
        this.baseHint = new String[][]{
                { "Welcome to Animal Kingdom!", "I am Tilly the Turtle.", "/cWhich tip do you wanna hear about?/s01What do potions do???/s02How to beat the final boss/s03Nothing. Bye" },
                { "Potions heals you by (n) HP!", "I hope this helped." },
                { "Final boss?? That Kangaroo??", "idk" },
                { "Good luck!" }
        };
        this.spritePath = "images/turtle.atlas";
        this.animationSpeed = 0.5f;
        this.soundPath = new String[] {"sounds/turtle-hiss.mp3"};
        this.health = 20;
        this.baseAttack = 0;
        this.animalName = "Turtle";
    }
}
