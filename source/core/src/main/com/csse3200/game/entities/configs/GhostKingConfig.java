package com.csse3200.game.entities.configs;

/**
 * Defines the properties stored in ghost king config files to be loaded by the NPC Factory.
 */
public class GhostKingConfig extends BaseEntityConfig {
  public int spookyFactor = 0;
  protected GhostKingConfig() {
    this.spritePath = "images/ghostKing.atlas";
    this.animationSpeed = 0.1f;
    this.health = 1;
    this.baseAttack = 0;
  }
}
