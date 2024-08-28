package com.csse3200.game.components;
import com.csse3200.game.rendering.AnimationRenderComponent;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.Task.Status;
import com.csse3200.game.ai.tasks.TaskRunner;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class ProximityComponentTest {
  @BeforeEach
  void beforeEach() {
    ServiceLocator.registerPhysicsService(new PhysicsService());
    GameTime gameTime = mock(GameTime.class);
    when(gameTime.getTime()).thenReturn(0L);
    ServiceLocator.registerTimeSource(gameTime);
  }

  @Test
  void shouldDisableEntityInitially() {
    Entity target = mock(Entity.class);
    Entity entity = mock(Entity.class);
    ProximityComponent proximityComponent = new ProximityComponent(target, 10f);
    proximityComponent.setEntity(entity);

    assertFalse(entity.getEnabled());
  }

  @Test
  void shouldEnableOnSpawn() {
    Entity entity = spy(new Entity());

    new ProximityComponent(entity, 10f);

    assertTrue(entity.getEnabled());
  }

  @Test
  void shouldUpdateToEnabled() {
    Entity target = mock(Entity.class);
    Entity entity = mock(Entity.class);
    EventHandler events = mock(EventHandler.class);

    when(target.getPosition()).thenReturn(new Vector2(0, 0));
    when(entity.getPosition()).thenReturn(new Vector2(5, 5));
    when(entity.getEvents()).thenReturn(events);

    entity.setEnabled(false); // Entity is initially disabled
    ProximityComponent proximityComponent = new ProximityComponent(target, 10f);
    proximityComponent.setEntity(entity);
    proximityComponent.update();

    assertFalse(entity.getEnabled());
    verify(events).trigger("proximityTriggered"); // Verify the trigger method was called on the events instance
  }

}