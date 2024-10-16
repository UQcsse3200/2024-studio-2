package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.tasks.StealTask;
import com.csse3200.game.components.tasks.WaitTask;
import com.csse3200.game.components.npc.FrogAnimationController;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StealTaskTest {
    @Mock
    private AITaskComponent ownerEntity;

    @Mock
    private FrogAnimationController animationController;

    @Mock
    private Entity mockEntity;

    private StealTask stealTask;
    private Map<Integer, Entity> items;

    @Mock
    private EventHandler eventHandler; // Mock the EventHandler

    @BeforeEach
    public void setup() {

        // Mock rendering, physics, game time
        RenderService renderService = new RenderService();
        renderService.setDebug(mock(DebugRenderer.class));
        ServiceLocator.registerRenderService(renderService);
        GameTime gameTime = mock(GameTime.class);
        when(gameTime.getDeltaTime()).thenReturn(20f / 1000);  // Mock delta time
        when(gameTime.getTime()).thenReturn(0L);  // Mock initial game time
        ServiceLocator.registerTimeSource(gameTime);
        ServiceLocator.registerPhysicsService(new PhysicsService());

        MockitoAnnotations.openMocks(this);

        // Create a mock map of items
        items = new HashMap<>();
        Entity itemEntity = mock(Entity.class);
        when(itemEntity.getPosition()).thenReturn(new Vector2(1, 1));
        items.put(1, itemEntity);

        // Set up the ownerEntity to return the mocked Entity
        when(ownerEntity.getEntity()).thenReturn(mockEntity);
        when(mockEntity.getComponent(FrogAnimationController.class)).thenReturn(animationController);
        when(mockEntity.getPosition()).thenReturn(new Vector2(0, 0));

        // Mock the events
        when(mockEntity.getEvents()).thenReturn(eventHandler); // Ensure getEvents() returns the mock

        // Create the StealTask
        stealTask = new StealTask(items, 2.0f);
        stealTask.create(ownerEntity); // Pass the AITaskComponent mock
    }


    @Test
    public void testStartStealTask() {
        // Start the task
        stealTask.start();

        // Verify that the spawn event is triggered
        verify(mockEntity.getEvents()).trigger("spawnStart");

        // Verify that the task is swapped to waitTask
        assertInstanceOf(WaitTask.class, stealTask.getCurrentTask());
    }

    @Test
    public void testReturnToOriginalPosition() {
        // Start the task and simulate moving toward the item
        stealTask.start();
        stealTask.update(); // Move to waiting

        // Simulate reaching the item
        when(mockEntity.getPosition()).thenReturn(new Vector2(1, 1));
        stealTask.update(); // Should dispose of the item

        // Check returning to origin
        when(mockEntity.getPosition()).thenReturn(new Vector2(0, 0));
        stealTask.update(); // Move back to original position

        // Verify if the current task is now waiting after returning
        assertTrue(stealTask.getCurrentTask() instanceof WaitTask);
    }
}
