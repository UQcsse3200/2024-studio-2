package com.csse3200.game.entities.configs;

/**
 * Defines all NPC configs to be loaded by the NPC Factory.
 */
public class NPCConfigs {
  public BaseEntityConfig ghost = new BaseEntityConfig();

  // Create config for Kangaroo boss
  // for now is just a base entity
  public BaseEntityConfig kangarooBoss = new BaseEntityConfig();
  public GhostKingConfig ghostKing = new GhostKingConfig();

}
