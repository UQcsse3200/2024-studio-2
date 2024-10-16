package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.EventHandler;
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
}