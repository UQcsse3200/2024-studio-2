package com.csse3200.game.entities.configs;

public class TurtleConfig extends BaseEntityConfig {
    public String favouriteFood = "Algae";
    public String soundPath = "sounds/turtle-hiss.mp3";
    public String animalName = "Turtle";
    public int health = 20;
    public int baseAttack = 0;

    public String getAnimalName() {
        return "Turtle";
    }
}
