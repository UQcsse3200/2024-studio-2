package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.npc.FrogAnimationController;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.minigames.snake.controller.Events;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class PullTaskTest {

    @Mock
    private AITaskComponent ownerEntity;

    @Mock
    private FrogAnimationController animationController;

    @Mock
    private Entity frogEntity;

    @Mock
    private Entity targetEntity;

    private PullTask pullTask;

    private int priority = 1; // Example priority value
    private float viewDistance = 5.0f; // Example view distance
    private float pullDistance = 3.0f; // Example pull distance

    @Mock
    private EventHandler eventHandler; // Mock the EventHandler

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // Mock rendering, physics, game time
        RenderService renderService = new RenderService();
        renderService.setDebug(mock(DebugRenderer.class));
        ServiceLocator.registerRenderService(renderService);
        GameTime gameTime = mock(GameTime.class);
        when(gameTime.getDeltaTime()).thenReturn(20f / 1000);  // Mock delta time
        when(gameTime.getTime()).thenReturn(0L);  // Mock initial game time
        ServiceLocator.registerTimeSource(gameTime);
        ServiceLocator.registerPhysicsService(new PhysicsService());

        // Set up frog
        when(ownerEntity.getEntity()).thenReturn(frogEntity);
        when(frogEntity.getComponent(FrogAnimationController.class)).thenReturn(animationController);
        when(frogEntity.getEvents()).thenReturn(eventHandler); // Ensure eventHandler is set up

        // Set up player
        when(targetEntity.getPosition()).thenReturn(new Vector2(3, 3));

        // Create PullTask
        pullTask = new PullTask(priority, targetEntity, viewDistance, pullDistance);
        pullTask.create(ownerEntity);
    }

    /**
     * Test PullTask is started correctly and spawn is triggered
     */
    @Test
    public void testStartPullTask() {
        // Act: Start the pull task
        pullTask.start();

        // Assert: Verify that the task has started and that the correct event was triggered
        verify(frogEntity.getEvents()).trigger(anyString());
    }

    /**
     * Test when pull task started, the player is pulled by enemy within the pull range
     */
    @Test
    public void testPullToPlayerWithinRange() {
        // Mock the position of frogEntity
        when(frogEntity.getPosition()).thenReturn(new Vector2(0, 0)); // Example position for frogEntity

        // Act: Start the pull task
        pullTask.start();

        // Call update to trigger pulling logic (make sure to implement this in your PullTask class)
        pullTask.update();

        // Assert: Verify that the frog attempts to pull the player
        verify(frogEntity.getEvents()).trigger(anyString());
    }
}