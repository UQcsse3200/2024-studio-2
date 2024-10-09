package com.csse3200.game.components.tasks;

import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.listeners.EventListener0;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class HiveTaskTest {

  @Mock
  GameTime gameTime;

  @BeforeEach
  void beforeEach() {
    ServiceLocator.registerTimeSource(gameTime);
  }

  /**
   * Tests that the HiveTask triggers the "float" event when started.
   */
  @Test
  void shouldTriggerFloatEvent() {
    HiveTask hiveTask = new HiveTask(new Entity());

    AITaskComponent aiTaskComponent = new AITaskComponent().addTask(hiveTask);
    Entity entity = new Entity().addComponent(aiTaskComponent).addComponent(new PhysicsMovementComponent());
    entity.create();

    // Register callback for the "float" event
    EventListener0 callback = mock(EventListener0.class);
    entity.getEvents().addListener("float", callback);

    // Start the HiveTask, which should trigger the "float" event
    hiveTask.start();

    // Verify that the "float" event is triggered
    verify(callback).handle();
  }

  /**
   * Tests that the correct priority is used.
   */
  @Test
  void shouldReturnCorrectPriority() {
    HiveTask hiveTask = new HiveTask(new Entity());

    // Ensure that the priority is the expected value (in this case, 11 as per your code)
    assertEquals(11, hiveTask.getPriority(), "HiveTask should have a priority of 11.");
  }

}
