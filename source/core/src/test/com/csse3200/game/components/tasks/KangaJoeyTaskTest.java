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
    public void shouldReturnNegativePriorityWhenTooFar() {
        // Set target's position out of range
        when(targetEntity.getPosition()).thenReturn(new Vector2(10, 10)); // Far out of range

        // Simulate task is active
        kangaJoeyTask.start();

        // Check that the priority is -1 when too far from the target
        assertEquals(-1, kangaJoeyTask.getPriority());
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

    @Test
    public void shouldNotSpawnJoeyWhenMaxSpawnsReached() {
        // Set target's position within range
        when(targetEntity.getPosition()).thenReturn(new Vector2(3, 3));

        // Simulate reaching the maximum number of spawns
        for (int i = 0; i < maxSpawns; i++) {
            kangaJoeyTask.update(); // This should spawn a joey each time
        }

        // Try to spawn another Joey, but maxSpawns has been reached
        kangaJoeyTask.update();

        // Verify that no more than maxSpawns Joeys are spawned
        verify(eventHandler, times(maxSpawns)).trigger(eq("spawnJoey"), eq(ownerEntity));
    }

    @Test
    public void shouldReturnPriorityWhenWithinRange() {
        // Set target's position within range
        when(targetEntity.getPosition()).thenReturn(new Vector2(3, 3));

        // Simulate task is inactive
        kangaJoeyTask.update(); // This should set it to inactive

        // Check that the priority is the defined PRIORITY when in range
        assertEquals(5, kangaJoeyTask.getPriority());
    }

}
