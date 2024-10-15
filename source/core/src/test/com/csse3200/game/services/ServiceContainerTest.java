package com.csse3200.game.services;

import com.csse3200.game.entities.EntityService;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.RenderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

@ExtendWith(GameExtension.class)
class ServiceContainerTest {
  @Test
  void shouldStoreServices() {
    EntityService entityService = new EntityService();
    RenderService renderService = new RenderService();
    PhysicsService physicsService = mock(PhysicsService.class);
    GameTime gameTime = new GameTime();

    ServiceLocator.registerEntityService(entityService);
    ServiceLocator.registerRenderService(renderService);
    ServiceLocator.registerPhysicsService(physicsService);
    ServiceLocator.registerTimeSource(gameTime);

    ServiceContainer container = new ServiceContainer();

    ServiceLocator.clear();

    assertEquals(entityService, container.getEntityService());
    assertEquals(renderService, container.getRenderService());
    assertEquals(physicsService, container.getPhysicsService());
    assertEquals(gameTime, container.getTimeSource());
  }
}