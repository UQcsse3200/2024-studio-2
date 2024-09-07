package com.csse3200.game.entities.configs;

import static com.csse3200.game.entities.factories.EnemyFactory.FRIENDLY;

/**
 * Defines the properties stored in ghost king config files to be loaded by the NPC Factory.
 */
public class ChickenConfig extends BaseEntityConfig{
    protected ChickenConfig() {
        if (!FRIENDLY) {
            this.spritePath = "images/enemy-chicken.atlas";
        } else {
            this.spritePath = "images/chicken.atlas";
        }
    this.animationSpeed = 0.1f;
    this.health = 1;
    this.baseAttack = 0;
  }
}
