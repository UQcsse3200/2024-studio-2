package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.InGameTime;
import com.csse3200.game.services.ServiceLocator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class GriffinTaskTest {

    @Mock
    private Entity ownerEntity;

    @Mock
    private Entity targetEntity;

    @Mock
    private EventHandler eventHandler;
    @Mock
    private MovementTask movementTask;

    private GriffinTask griffinTask;

    private final int priority = 10;
    private final float viewDistance = 30f;
    private final float waitTime = 300;
    private final float shootRange = 100f;

    @Before
    public void setUp() {
        // Initialize the mocks
        MockitoAnnotations.initMocks(this);

        // Mock the event handler for the owner entity
        when(ownerEntity.getEvents()).thenReturn(eventHandler);

        // Create the GriffinTask with the mocked entities
        ServiceLocator.registerPhysicsService(new PhysicsService() {});
        ServiceLocator.registerRenderService(new RenderService() {});
        ServiceLocator.registerTimeSource(new GameTime());
        griffinTask = new GriffinTask(targetEntity, priority, viewDistance, waitTime, shootRange);

        // Mock the owner entity to be linked with the task
        when(ownerEntity.getPosition()).thenReturn(new Vector2(0, 0));  // Owner's position
        when(targetEntity.getPosition()).thenReturn(new Vector2(3, 4)); // Target's position within range

        // Set up the task with the mock owner
        griffinTask.create(() -> ownerEntity);
    }

    @Test
    public void shouldReturnCorrectPriorityWhenActiveAndInRange() {
        // Simulate task is active
        griffinTask.start();

        // The target is within the spawn range, so priority should be the defined PRIORITY
        assertEquals(10, griffinTask.getPriority());
    }

    @Test
    public void shouldReturnNegativePriorityWhenOutOfRange() {
        // Set target's position out of range
        when(targetEntity.getPosition()).thenReturn(new Vector2(10, 10));

        // Simulate task is inactive and far from the target
        griffinTask.update();
        assertEquals(-1, griffinTask.getPriority());
    }

    @Test
    public void shouldReturnCorrectViewDistanceWhenActiveAndInRange() {}

    @Test
    public void shouldReturnCorrectWaitTimeWhenActiveAndInRange() {}

    @Test
    public void shouldReturnCorrectViewDistanceWhenOutOfRange() {}

    @Test
    public void shouldNotShootWhenOutOfRange() {}
}