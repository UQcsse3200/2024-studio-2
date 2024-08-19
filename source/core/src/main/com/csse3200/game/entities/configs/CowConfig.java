package com.csse3200.game.entities.configs;

/**
 * Defines the Cow's statistics stored in cow config files to be loaded by the NPC factory.
 */
public class CowConfig extends BaseEntityConfig {
    public String favouriteColour = "Hay";
    public String soundPath = "sounds/mooing-cow.mp3";
    public String animalName = "Cow";
    public int health = 30;
    public int baseAttack = 0;
}
