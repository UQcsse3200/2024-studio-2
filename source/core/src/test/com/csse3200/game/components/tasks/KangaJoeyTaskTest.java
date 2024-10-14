package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.EventHandler;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class KangaJoeyTaskTest {

    @Mock
    private Entity ownerEntity;

    @Mock
    private Entity targetEntity;

    @Mock
    private EventHandler eventHandler;

    private KangaJoeyTask kangaJoeyTask;

    private final float spawnRange = 5f;
    private final int maxSpawns = 3;

    @Before
    public void setUp() {
        // Initialize the mocks
        MockitoAnnotations.initMocks(this);

        // Mock the event handler for the owner entity
        when(ownerEntity.getEvents()).thenReturn(eventHandler);

        // Create the KangaJoeyTask with the mocked entities
        kangaJoeyTask = new KangaJoeyTask(targetEntity, spawnRange, maxSpawns);

        // Mock the owner entity to be linked with the task
        when(ownerEntity.getPosition()).thenReturn(new Vector2(0, 0));  // Owner's position
        when(targetEntity.getPosition()).thenReturn(new Vector2(3, 4)); // Target's position within range

        // Set up the task with the mock owner
        kangaJoeyTask.create(() -> ownerEntity);
    }

    @Test
    public void shouldReturnCorrectPriorityWhenActiveAndInRange() {
        // Simulate task is active
        kangaJoeyTask.start();

        // The target is within the spawn range, so priority should be the defined PRIORITY
        assertEquals(5, kangaJoeyTask.getPriority());
    }

    @Test
    public void shouldReturnNegativePriorityWhenOutOfRange() {
        // Set target's position out of range
        when(targetEntity.getPosition()).thenReturn(new Vector2(10, 10));

        // Simulate task is inactive and far from the target
        kangaJoeyTask.update();
        assertEquals(-1, kangaJoeyTask.getPriority());
    }

    @Test
    public void shouldSpawnJoeyWhenInRangeAndUnderMaxSpawns() {
        // Set the task's internal state
        kangaJoeyTask.update(); // Update should check distance and trigger spawning

        // Verify that the "spawnJoey" event was triggered
        verify(eventHandler, times(1)).trigger(eq("spawnJoey"), eq(ownerEntity));
    }

    @Test
    public void shouldNotSpawnJoeyWhenMaxSpawnsReached() {
        // Simulate spawning Joeys up to the max spawn limit
        for (int i = 0; i < maxSpawns; i++) {
            kangaJoeyTask.update();
        }

        // Try to update after max spawns
        kangaJoeyTask.update();

        // Verify that the event was triggered only the maximum number of times (3 in this case)
        verify(eventHandler, times(maxSpawns)).trigger(eq("spawnJoey"), eq(ownerEntity));
    }

    @Test
    public void shouldStopSpawningWhenOutOfRange() {
        // Set target's position within range initially
        when(targetEntity.getPosition()).thenReturn(new Vector2(3, 3));
        kangaJoeyTask.update();

        // Move the target out of range
        when(targetEntity.getPosition()).thenReturn(new Vector2(10, 10));
        kangaJoeyTask.update();

        // Verify that the priority is negative when out of range
        assertEquals(-1, kangaJoeyTask.getPriority());

        // Also verify no new Joey spawns after the target is out of range
        verify(eventHandler, times(1)).trigger(eq("spawnJoey"), eq(ownerEntity));
    }
}
