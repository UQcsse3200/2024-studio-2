package com.csse3200.game.physics;

import com.csse3200.game.services.ServiceLocator;

/**
 * Provides a global access point to the physics engine. This is necessary for physics-based
 * entities to add or remove themselves from the world, as well as update their position each frame.
 */
public class LightingService {
  private final LightingEngine engine;

  public LightingService(LightingEngine engine) {
    this.engine = engine;
  }

  public LightingEngine getLighting() {
    return engine;
  }
}
