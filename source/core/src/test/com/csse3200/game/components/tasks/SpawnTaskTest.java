package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.Task.Status;
import com.csse3200.game.ai.tasks.TaskRunner;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class SpawnTaskTest {
  @BeforeEach
  void beforeEach() {
    ServiceLocator.registerPhysicsService(new PhysicsService());
    GameTime gameTime = mock(GameTime.class);
    when(gameTime.getTime()).thenReturn(0L);
    ServiceLocator.registerTimeSource(gameTime);
  }
  @Test
  void shouldTriggerSpawnEvent() {
    Vector2 target = new Vector2(10f, 10f);
    TaskRunner taskRunner = mock(TaskRunner.class);
    Entity entity = mock(Entity.class);
    EventHandler events = mock(EventHandler.class);

    when(taskRunner.getEntity()).thenReturn(entity);
    when(entity.getEvents()).thenReturn(events);

    SpawnTask spawn = new SpawnTask(target, 1f);
    spawn.setOwner((TaskRunner) taskRunner);
    spawn.start();
    verify(entity.getEvents()).trigger("spawnChicken");
  }
  @Test
  void shouldBeActiveAfterStart(){
    Vector2 target = new Vector2(0f,0f);
    TaskRunner taskRunner = mock(TaskRunner.class);
    Entity entity = mock(Entity.class);
    EventHandler events = mock(EventHandler.class);

    when(taskRunner.getEntity()).thenReturn(entity);
    when(entity.getEvents()).thenReturn(events);

    SpawnTask spawn = new SpawnTask(target,1f);
    spawn.setOwner((TaskRunner) taskRunner);
    spawn.start();
    assertEquals(Status.ACTIVE, spawn.getStatus());
  }

  @Test
  void shouldBeInactiveBeforeAndAfterStart(){
    Vector2 target = new Vector2(0f,0f);
    SpawnTask spawn = new SpawnTask(target,1f);
    assertEquals(Status.INACTIVE, spawn.getStatus());
    spawn.stop();
    assertEquals(Status.INACTIVE, spawn.getStatus());
  }
}