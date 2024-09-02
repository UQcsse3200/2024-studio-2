package com.csse3200.game.entities.configs;

/**
 * Defines the Cow's statistics stored in cow config files to be loaded by the NPC factory.
 */
public class CowConfig extends BaseEntityConfig {

    public CowConfig() {
        this.baseHint = new String[][]{
                { "Welcome to Animal Kingdom!", "I am Charlie the Cow.", "/cWhich tip do you wanna hear about?/s01What do potions do???/s02How to beat the final boss/s03Nothing. Bye" },
                { "Potions heals you by (n) HP!", "I hope this helped." },
                { "Final boss?? That Kangaroo??", "idk" },
                { "Good luck!" }
        };
        this.spritePath = "images/Cow.atlas";
        this.animationSpeed = 0.2f;
        this.soundPath = new String[] {"sounds/mooing-cow.mp3"};
        this.health = 30;
        this.baseAttack = 0;
        this.animalName = "Cow";
    }
}
