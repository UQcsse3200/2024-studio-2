package com.csse3200.game.components.tasks;

import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.entities.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A task that allows an entity to wait for a set time and then spawn a small kangaroo joey entity near the owner.
 */
public class KangaJoeyTask extends DefaultTask implements PriorityTask {
    private static final Logger logger = LoggerFactory.getLogger(KangaJoeyTask.class);
    private final int priority = 5;
    private final Entity target;  // The target entity to aim at (could be the player or another entity).
    private final float range;    // Range within which to spawn the joey
    private int numSpawns = 0;    // Number of joeys spawned
    private final int maxSpawns;  // Max number of Joeys that can be spawned

    /**
     * A task that allows an entity to wait for a set time and then spawn a small kangaroo joey near the owner.
     *
     * @param target   The target entity (could be the player or another entity).
     * @param range    The distance within which the joey will be spawned.
     * @param maxSpawns Maximum number of Joeys that can be spawned.
     */
    public KangaJoeyTask(Entity target, float range, int maxSpawns) {
        this.target = target;
        this.range = range;
        this.maxSpawns = maxSpawns;
    }

    @Override
    public int getPriority() {
        if (status == Status.ACTIVE) {
            return getActivePriority();
        }
        update();
        return getInactivePriority();
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void update() {
        if (getDistanceToTarget() < range && numSpawns < maxSpawns) {
            spawnJoey();
        }
    }

    /**
     * Returns the distance between the current entity and the target location.
     *
     * @return The distance between the owner's entity and the target location.
     */
    private float getDistanceToTarget() {
        return owner.getEntity().getPosition().dst(target.getPosition());
    }

    /**
     * Gets the priority when the task is active, based on the distance to the target.
     *
     * @return The priority value, or -1 if out of range.
     */
    private int getActivePriority() {
        float dst = getDistanceToTarget();
        if (dst > range) {
            return -1; // Too far, stop spawning
        }
        return priority;
    }

    /**
     * Gets the priority when the task is inactive, based on the distance to the target.
     *
     * @return The priority value, or -1 if out of range.
     */
    private int getInactivePriority() {
        float dst = getDistanceToTarget();
        if (dst <= range) {
            return priority;
        }
        return -1;
    }

    /**
     * Spawns a small kangaroo joey near the owner.
     */
    private void spawnJoey() {
        numSpawns++;
        owner.getEntity().getEvents().trigger("spawnJoey", owner.getEntity());
    }
}
