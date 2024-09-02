package com.csse3200.game.entities.configs;

/**
 * Defines the properties stored in ghost king config files to be loaded by the NPC Factory.
 */
public class ChickenConfig extends BaseEntityConfig{
    protected ChickenConfig() {
        this.spritePath = "images/chicken.atlas";
        this.animationSpeed = 0.1f;
        this.health = 10;
        this.baseAttack = 1;
        this.baseDefense = 1;
        this.speed = 2;
        this.experience = 1;
    }
}
