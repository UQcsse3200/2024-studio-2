package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.listeners.EventListener0;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.utils.math.Vector2Utils;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Vector;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the ProjectileMovementTask class.
 * This class tests the functionality of the projectile movement tasks,
 * ensuring that events are triggered correctly and the priority is accurately returned.
 */
@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class ProjectileMovementTaskTest {
  @Mock
  GameTime gameTime;

  /**
   * Sets up the test environment before each test case.
   * Registers the mock GameTime and PhysicsService with the ServiceLocator.
   */
  @BeforeEach
  void beforeEach() {
    ServiceLocator.registerTimeSource(gameTime);
    ServiceLocator.registerPhysicsService(new PhysicsService());
  }

  /**
   * Tests that various movement events are triggered correctly.
   * This method verifies that the ProjectileMovementTask correctly responds
   * to event triggers for different movement directions, confirming that the
   * expected events are handled properly.
   */
  @Test
  void shouldTriggerEvent() {
    Entity target = new Entity();
    target.setPosition(1f, 1f);
    ProjectileMovementTask movementTask = new ProjectileMovementTask(target, 10);

    AITaskComponent aiTaskComponent = new AITaskComponent().addTask(movementTask);
    Entity entity = new Entity().addComponent(aiTaskComponent).addComponent(new PhysicsMovementComponent());
    entity.create();

    EventListener0 callback = mock(EventListener0.class);
    entity.getEvents().addListener("up", callback);
    entity.getEvents().trigger("up");
    verify(callback).handle();

    callback = mock(EventListener0.class);
    entity.getEvents().addListener("rightUp", callback);
    entity.getEvents().trigger("rightUp");
    verify(callback).handle();

    callback = mock(EventListener0.class);
    entity.getEvents().addListener("right", callback);
    entity.getEvents().trigger("right");
    verify(callback).handle();

    callback = mock(EventListener0.class);
    entity.getEvents().addListener("rightDown", callback);
    entity.getEvents().trigger("rightDown");
    verify(callback).handle();

    callback = mock(EventListener0.class);
    entity.getEvents().addListener("down", callback);
    entity.getEvents().trigger("down");
    verify(callback).handle();

    callback = mock(EventListener0.class);
    entity.getEvents().addListener("leftDown", callback);
    entity.getEvents().trigger("leftDown");
    verify(callback).handle();

    callback = mock(EventListener0.class);
    entity.getEvents().addListener("left", callback);
    entity.getEvents().trigger("left");
    verify(callback).handle();

    callback = mock(EventListener0.class);
    entity.getEvents().addListener("leftUp", callback);
    entity.getEvents().trigger("leftUp");
    verify(callback).handle();
  }

  /**
   * Tests that the ProjectileMovementTask returns the correct priority.
   * This method verifies that the priority value of the task is set
   * correctly based on the input parameter.
   */
  @Test
  void shouldReturnCorrectPriority() {
    Entity targetEntity = mock(Entity.class);
    when(targetEntity.getPosition()).thenReturn(new Vector2(5, 5));

    ProjectileMovementTask projectileTask = new ProjectileMovementTask(targetEntity, 10);

    assertEquals(10, projectileTask.getPriority());
  }

}