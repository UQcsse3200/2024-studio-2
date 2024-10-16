package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
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

    @Mock
    private EventHandler eventHandler; // Mock the EventHandler

    /**
     * Sets up the test environment before each test.
     */
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

        //Set up frog
        when(ownerEntity.getEntity()).thenReturn(frogEntity);
        when(frogEntity.getComponent(FrogAnimationController.class)).thenReturn(animationController);
        when(frogEntity.getPosition()).thenReturn(new Vector2(0, 0));

        // Set up player
        when(targetEntity.getPosition()).thenReturn(new Vector2(3, 3));

        // Mock the events
        when(frogEntity.getEvents()).thenReturn(eventHandler);

        // Create PullTask
        pullTask = new PullTask(1, targetEntity, 5.0f, 2.0f);
        pullTask.create(ownerEntity);
    }


    /**
     * Test PullTask is started correctly and spawn is triggered
     */
    @Test
    public void testStartPullTask() {
        // Start the task
        pullTask.start();

        // Verify that the spawn event is triggered
        verify(frogEntity.getEvents()).trigger("spawn");

        assertTrue(pullTask.getPriority() > 0);
    }

    /**
     * Test when pull task started, the player is pulled by enemy within the pull range
     *
     */
    @Test
    public void testPullToPlayerWithinRange() {
        pullTask.start();
        when(frogEntity.getPosition()).thenReturn(new Vector2(0, 0));
        when(targetEntity.getPosition()).thenReturn(new Vector2(1.5f, 1.5f)); // Within pull distance

        // Call update to trigger pulling logic
        pullTask.update();

        // Verify that the frog attempts to pull the player
        verify(frogEntity.getEvents()).trigger(anyString());

        // Check if the target has moved closer (position updated)
        Vector2 expectedPosition = new Vector2(1.5f, 1.5f);
        assertEquals(expectedPosition, targetEntity.getPosition());
    }

    /**
     * Test when player is out of enemy's pull range
     */
    @Test
    public void testPullToPlayerOutOfRange() {
        pullTask.start();
        when(targetEntity.getPosition()).thenReturn(new Vector2(10, 10)); // Out of pull range

        // Call update, no pulling should occur
        pullTask.update();

        // Verify no pulling events are triggered
        verify(frogEntity.getEvents(), never()).trigger(anyString());
    }

    /**
     * Test the case where the target entity is not visible
     */
    @Test
    public void testTargetNotVisible() {
        when(targetEntity.getPosition()).thenReturn(new Vector2(2, 2)); // Within range but blocked
        when(frogEntity.getPosition()).thenReturn(new Vector2(0, 0));

        assertEquals(-1, pullTask.getPriority()); // Task should not activate if target is not visible
    }
}
