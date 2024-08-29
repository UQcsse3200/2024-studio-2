package com.csse3200.game.entities.configs;

/**
 * Defines the properties stored in ghost king config files to be loaded by the NPC Factory.
 */
public class KangarooBossConfig extends BaseEntityConfig {
    protected KangarooBossConfig() {
        this.spritePath = "images/final_boss_kangaroo.atlas";
        this.animationSpeed = 0.1f;
        this.health = 100;
        this.baseAttack = 100;
    }
}
