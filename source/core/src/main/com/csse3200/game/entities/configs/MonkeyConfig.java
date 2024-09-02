package com.csse3200.game.entities.configs;

/**
 * Defines the properties stored in ghost king config files to be loaded by the NPC Factory.
 */
public class MonkeyConfig extends BaseEntityConfig{
    protected MonkeyConfig() {
        this.spritePath = "images/monkey.atlas";
        this.animationSpeed = 0.1f;
        this.health = 18;
        this.baseAttack = 13;
        this.baseDefense = 9;
        this.speed = 1.5f;
        this.experience = 9;
    }
}
