package com.csse3200.game.entities.configs;

/**
 * Defines the properties stored in ghost king config files to be loaded by the NPC Factory.
 */
public class MonkeyConfig extends BaseEntityConfig{
    protected MonkeyConfig() {
        this.spritePath = "images/monkey.atlas";
        this.animationSpeed = 0.1f;
        this.health = 20;
        this.baseAttack = 15;
        this.baseDefense = 10;
        this.speed = 1.5f;
        this.experience = 10;
    }
}
