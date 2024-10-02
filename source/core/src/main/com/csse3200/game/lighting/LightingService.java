package com.csse3200.game.lighting;

/**
 * Provides a global access point to the lighting engine. This is necessary for lights to be
 * attached to entities.
 * The lighting engine should be updated each frame by adding it to the renderer or calling
 * the render method in the game screen.
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
