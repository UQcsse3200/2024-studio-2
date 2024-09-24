package com.csse3200.game.components.tasks;

import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.events.listeners.EventListener1;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class ShootTaskTest {
  @BeforeEach
  void beforeEach() {
    // Mock rendering, physics, game time
    RenderService renderService = new RenderService();
    renderService.setDebug(mock(DebugRenderer.class));
    ServiceLocator.registerRenderService(renderService);
    GameTime gameTime = mock(GameTime.class);
    when(gameTime.getDeltaTime()).thenReturn(20f / 1000);  // Mock delta time
    when(gameTime.getTime()).thenReturn(0L);  // Mock initial game time
    ServiceLocator.registerTimeSource(gameTime);
    ServiceLocator.registerPhysicsService(new PhysicsService());
  }

  /**
   * Tests that the shoot task fires a projectile when in range and the wait time has passed.
   */
  @Test
  void shouldShootWhenInRangeAndWaitTimeElapsed() {
    Entity target = new Entity();
    target.setPosition(1f, 1f);

    ShootTask shootTask = new ShootTask(1f, target, 5f);  // Wait time of 1 second, range of 5 units
    Entity entity = makePhysicsEntity();
    AITaskComponent ai = new AITaskComponent().addTask(shootTask);
    entity.addComponent(ai);
    entity.create();
    entity.setPosition(0f, 0f);

    // Set up a real EventHandler and add a listener to verify "FireBanana" is triggered
    EventHandler eventHandler = entity.getEvents();
    EventListener1<Entity> fireBananaListener = mock(EventListener1.class);
    eventHandler.addListener("FireBanana", fireBananaListener);

    // Fast forward time to trigger shooting
    when(ServiceLocator.getTimeSource().getTime()).thenReturn(1000L);  // 1 second later
    shootTask.start();  // Start the shoot task
    shootTask.update(); // Trigger the task update

    // Verify that "FireBanana" event was triggered
    verify(fireBananaListener, times(1)).handle(entity);
  }

  /**
   * Tests that the shoot task only fires when within a certain distance.
   */
  @Test
  void shouldShootOnlyWhenInDistance() {
    Entity target = new Entity();
    target.setPosition(0f, 6f);

    ShootTask shootTask = new ShootTask(1f, target, 5f);  // Wait time of 1 second, range of 5 units
    Entity entity = makePhysicsEntity();
    AITaskComponent ai = new AITaskComponent().addTask(shootTask);
    entity.addComponent(ai);
    entity.create();
    entity.setPosition(0f, 0f);

    // Set up a real EventHandler and add a listener to verify "FireBanana" is triggered
    EventHandler eventHandler = entity.getEvents();
    EventListener1<Entity> fireBananaListener = mock(EventListener1.class);
    eventHandler.addListener("FireBanana", fireBananaListener);

    // Not currently active, target is too far, should have negative priority
    assertTrue(shootTask.getPriority() < 0);

    // When in view distance, should give higher priority
    target.setPosition(0f, 4f);
    assertEquals(5, shootTask.getPriority());

    // Simulate shooting
    when(ServiceLocator.getTimeSource().getTime()).thenReturn(1000L);  // 1 second later
    shootTask.start();
    shootTask.update();

    // Verify "FireBanana" event triggered when in range and wait time passed
    verify(fireBananaListener, times(1)).handle(entity);

    // When active, should not shoot outside range
    target.setPosition(0f, 12f);
    assertTrue(shootTask.getPriority() < 0);
  }

  private Entity makePhysicsEntity() {
    return new Entity()
            .addComponent(new PhysicsComponent())
            .addComponent(new PhysicsMovementComponent());
  }
}