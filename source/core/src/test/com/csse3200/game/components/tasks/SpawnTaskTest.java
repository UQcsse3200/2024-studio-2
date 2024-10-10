package com.csse3200.game.components.tasks;

import com.csse3200.game.ai.tasks.TaskRunner;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.Task.Status;
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

  /**
   * Sets up the test environment before each test.
   */
  @BeforeEach
  void beforeEach() {
    ServiceLocator.registerPhysicsService(new PhysicsService());
    GameTime gameTime = mock(GameTime.class);
    when(gameTime.getTime()).thenReturn(0L);
    ServiceLocator.registerTimeSource(gameTime);
  }

  /**
   * Tests that the SpawnTask triggers the "spawnChicken" event when started.
   */
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

  /**
   * Tests that the SpawnTask returns the correct spawn position.
   */
  @Test
  void getSpawnPositionReturnsCorrectValue() {
    Vector2 expectedSpawnPosition = new Vector2(10f, 10f);
    SpawnTask spawn = new SpawnTask(expectedSpawnPosition, 1f);
    Vector2 actualSpawnPosition = spawn.getSpawnPosition();
    assertEquals(expectedSpawnPosition, actualSpawnPosition);
  }

  /**
   * Verifies that the SpawnTask transitions to the ACTIVE status after starting.
   */
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
  void shouldBeInactiveOutsideStartCall(){
    Vector2 target = new Vector2(0f,0f);
    SpawnTask spawn = new SpawnTask(target,1f);
    assertEquals(Status.INACTIVE, spawn.getStatus());
    spawn.stop();
    assertEquals(Status.INACTIVE, spawn.getStatus());
  }

  /**
   * Tests that the SpawnTask remains INACTIVE outside of the start method call.
   */
  @Test
  void constructorInitialisesCorrectly() {
    Vector2 target = new Vector2(10f, 10f);
    SpawnTask spawn = new SpawnTask(target, 1f);
    assertEquals(target, spawn.getSpawnPosition());
    assertEquals(1f, spawn.getSpawnDuration());
  }

  /**
 * Verifies that the SpawnTask constructor initializes the object correctly,
 * setting the spawn position and duration to the expected values.
 */
  @Test
  void getPriorityReturnsCorrectValue() {
    SpawnTask spawn = new SpawnTask(new Vector2(0f, 0f), 1f);
    assertEquals(0, spawn.getPriority()); // Verify default priority

    // Test with different spawn durations
    spawn = new SpawnTask(new Vector2(0f, 0f), 2f);
    assertEquals(0, spawn.getPriority()); // Verify priority remains the same

    // Test with different spawn positions
    spawn = new SpawnTask(new Vector2(10f, 10f), 1f);
    assertEquals(0, spawn.getPriority()); // Verify priority remains the same
  }

  /**
   * Tests that the SpawnTask updates its elapsed time correctly.
   */
  @Test
  void updatesElapsedTime() {
    TaskRunner taskRunner = mock(TaskRunner.class);
    Entity entity = mock(Entity.class);
    EventHandler events = mock(EventHandler.class);

    when(taskRunner.getEntity()).thenReturn(entity);
    when(entity.getEvents()).thenReturn(events);

    SpawnTask spawn = new SpawnTask(new Vector2(0f, 0f), 1f);
    spawn.setOwner(taskRunner);
    spawn.start();
    spawn.update();
    assertEquals(0, spawn.getElapsedTime());
  }

  /**
   * Verifies that the SpawnTask completes and stops correctly.
   */
  @Test
  void completeTaskStopped() {
    TaskRunner taskRunner = mock(TaskRunner.class);
    EventHandler events = mock(EventHandler.class);
    Entity entity = mock(Entity.class);

    when(taskRunner.getEntity()).thenReturn(entity);
    when(entity.getEvents()).thenReturn(events);

    SpawnTask spawn = new SpawnTask(new Vector2(0f, 0f), 1f);
    spawn.setOwner(taskRunner);
    spawn.start();

    spawn.completeTask();

    assertNotNull(spawn.getStatus());
    assertEquals(Status.INACTIVE, spawn.getStatus());
  }

  }

