package com.csse3200.game.components;
import com.csse3200.game.events.listeners.EventListener0;
import com.csse3200.game.events.listeners.EventListener1;
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
import org.mockito.ArgumentCaptor;
import java.util.function.Consumer;



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

  @Test
  void shouldStartSpawnAnimationWhenProximityTriggered() {
    Entity entity = mock(Entity.class);
    AnimationRenderComponent animationRenderComponent = mock(AnimationRenderComponent.class);
    EventHandler events = mock(EventHandler.class);

    when(entity.getComponent(AnimationRenderComponent.class)).thenReturn(animationRenderComponent);
    when(entity.getEvents()).thenReturn(events);

    ProximityComponent proximityComponent = new ProximityComponent(entity, 10f);
    proximityComponent.setupSpawnAnimation(entity);

    ArgumentCaptor<EventListener0> proximityListenerCaptor = ArgumentCaptor.forClass(EventListener0.class);
    verify(events).addListener(eq("proximityTriggered"), proximityListenerCaptor.capture());
    proximityListenerCaptor.getValue().handle();

    // Verify that the "spawn" animation starts
    verify(animationRenderComponent).startAnimation("spawn");

    ArgumentCaptor<EventListener1<String>> animationEndCaptor = ArgumentCaptor.forClass(EventListener1.class);
    verify(events).addListener(eq("animationEnd"), animationEndCaptor.capture());
    animationEndCaptor.getValue().handle("spawn");

    // Verify that the "walk" animation starts after the "spawn" animation ends
    verify(animationRenderComponent).startAnimation("walk");
  }
}
