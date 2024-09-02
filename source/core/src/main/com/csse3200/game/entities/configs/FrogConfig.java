package com.csse3200.game.entities.configs;

/**
 * Defines the properties stored in ghost king config files to be loaded by the NPC Factory.
 */
public class FrogConfig extends BaseEntityConfig{
    protected FrogConfig() {
        this.spritePath = "images/chicken.atlas";
        this.animationSpeed = 0.1f;
        this.health = 5;
        this.baseAttack = 50;
        this.baseDefense = 5;
        this.speed = 0.5f;
        this.experience = 5;
    }
}
