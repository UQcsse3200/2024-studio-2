package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;

/**
 * A task that spawns an entity at a specified position after a certain duration.
 * */
public class SpawnTask extends DefaultTask implements PriorityTask {
    private final Vector2 spawnPosition;
    private final float spawnDuration;
    private float elapsedTime;

    /**
     * Constructs a new SpawnTask with the specified spawn position and duration.
     *
     * @param spawnPosition the position where the entity will be spawned
     * @param spawnDuration the duration of the spawn task
     */
    public SpawnTask(Vector2 spawnPosition, float spawnDuration) {
        this.spawnPosition = spawnPosition;
        this.spawnDuration = spawnDuration;
    }

    /**
     * Returns the position where the entity will be spawned.
     *
     * @return the spawn position
     */
    public Vector2 getSpawnPosition() {
        return spawnPosition;
    }

    /**
     * Returns the duration of the spawn task.
     *
     * @return the spawn duration
     */
    public float getSpawnDuration(){
        return spawnDuration;
    }

    /**
     * Returns the elapsed time since the spawn task started.
     *
     * @return the elapsed time
     */
    public float getElapsedTime(){
        return elapsedTime;
    }

    /**
     * Returns the priority of this task. Higher values indicate higher priority.
     *
     * @return the priority of this task
     */
    @Override
    public int getPriority() {
        return 0; // Highest priority for spawn task
    }

    /**
     * Starts the spawn task, positioning the entity at the spawn location.
     */
    @Override
    public void start() {
        super.start();
        elapsedTime = 0;
        // Position the NPC at the spawn location
        this.owner.getEntity().getEvents().trigger("spawnChicken");
    }

    /**
     * Completes the spawn task, stopping it and cleaning up if necessary.
     */
    public void completeTask() {
        this.stop();
    }
}
