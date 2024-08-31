package com.csse3200.game.components.tasks;

import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.listeners.EventListener0;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.utils.math.Vector2Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class SpecialWanderTaskTest {
  @Mock
  GameTime gameTime;

  @BeforeEach
  void beforeEach() {
    ServiceLocator.registerTimeSource(gameTime);
  }

  /**
   * Tests that the Special wander task triggers events.
   */
  @Test
  void shouldTriggerEvent() {
    SpecialWanderTask wanderTask = new SpecialWanderTask(Vector2Utils.ONE, 1f);

    AITaskComponent aiTaskComponent = new AITaskComponent().addTask(wanderTask);
    Entity entity = new Entity().addComponent(aiTaskComponent).addComponent(new PhysicsMovementComponent());
    entity.create();

    // Register callbacks
    // Right
    EventListener0 callback = mock(EventListener0.class);
    entity.getEvents().addListener("wanderRight", callback);

    entity.getEvents().trigger("wanderRight");

    verify(callback).handle();

    //left
    callback = mock(EventListener0.class);

    entity.getEvents().addListener("wanderLeft", callback);

    entity.getEvents().trigger("wanderLeft");

    verify(callback).handle();

    // Register callbacks
    // Up
    callback = mock(EventListener0.class);
    entity.getEvents().addListener("wanderUp", callback);

    entity.getEvents().trigger("wanderUp");

    verify(callback).handle();

    // Register callbacks
    // Left Up
    callback = mock(EventListener0.class);
    entity.getEvents().addListener("wanderLeftUp", callback);

    entity.getEvents().trigger("wanderLeftUp");

    verify(callback).handle();

    // Register callbacks
    // URightUp
    callback = mock(EventListener0.class);
    entity.getEvents().addListener("wanderRightUp", callback);

    entity.getEvents().trigger("wanderRightUp");

    verify(callback).handle();

    // Register callbacks
    // Right Down
    callback = mock(EventListener0.class);
    entity.getEvents().addListener("wanderRightDown", callback);

    entity.getEvents().trigger("wanderRightDown");

    verify(callback).handle();

    // Register callbacks
    // Up
    callback = mock(EventListener0.class);
    entity.getEvents().addListener("wanderLeftDown", callback);

    entity.getEvents().trigger("wanderLeftDown");

    verify(callback).handle();
  }


}