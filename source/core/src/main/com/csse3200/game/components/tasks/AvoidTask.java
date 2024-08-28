package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;

/** Moves away from a target entity until a safe distance is reached or line of sight is lost */
public class AvoidTask extends ChaseTask {

    private final float safeDistance;
    private final float minAvoidDistance;

    /**
     * @param target The entity to avoid.
     * @param priority Task priority when avoiding (0 when not avoiding).
     * @param safeDistance Minimum distance to maintain from the target.
     * @param minAvoidDistance Minimum distance to avoid moving away from the target.
     */
    public AvoidTask(Entity target, int priority, float safeDistance, float minAvoidDistance, boolean isBoss) {
        // Call parent constructor
        super(target, priority, safeDistance, minAvoidDistance, isBoss);
        this.safeDistance = safeDistance;
        this.minAvoidDistance = minAvoidDistance;
    }

    /**
     * Starts the avoidance behavior by initializing and starting the movement task.
     * Triggers the "avoidStart" event to indicate the beginning of avoidance.
     */
    @Override
    public void start() {
        super.start();

        movementTask = new MovementTask(getAvoidanceTarget());
        movementTask.create(owner);
        movementTask.start();

        this.owner.getEntity().getEvents().trigger("avoidStart");
    }


    /**
     * Updates the avoidance behavior by setting the current target for the movement task
     * and updating its state. If the movement task is not active, it restarts the task.
     */
    @Override
    public void update() {
        movementTask.setTarget(getAvoidanceTarget());
        movementTask.update();
        if (movementTask.getStatus() != Status.ACTIVE) {
            movementTask.start();
        }
    }

    /**
     * Returns the priority level of the avoidance behavior.
     * Determines the priority based on whether the task is currently active or inactive.
     *
     * @return the priority level of the avoidance behavior.
     */
    @Override
    public int getPriority() {
        if (status == Status.ACTIVE) {
            return getActivePriority();
        }

        return getInactivePriority();
    }

    /**
     * Calculates the avoidance target position based on the entity's current position,
     * the target's position, and the minimum avoidance distance.
     * The avoidance target is calculated to be away from the target's position.
     *
     * @return a Vector2 representing the target position to move towards for avoidance.
     */
    private Vector2 getAvoidanceTarget() {
        Vector2 entityPosition = owner.getEntity().getPosition();
        Vector2 targetPosition = target.getPosition();
        Vector2 directionAway = entityPosition.cpy().sub(targetPosition).nor();
        return entityPosition.cpy().add(directionAway.scl(minAvoidDistance));
    }

    /**
     * Returns the priority level when the avoidance behavior is active.
     * If the distance to the target is greater than the safe distance or the target is not visible,
     * the avoidance behavior should stop and the method returns -1.
     *
     * @return the active priority level or -1 if the behavior should stop.
     */
    @Override
    protected int getActivePriority() {
        float dst = super.getDistanceToTarget();
        if (dst > safeDistance || !isTargetVisible()) {
            return -1; // Safe distance reached, stop avoiding
        }
        return priority;
    }

    /**
     * Returns the priority level when the avoidance behavior is inactive.
     * If the distance to the target is less than the safe distance and the target is visible,
     * the method returns the set priority; otherwise, it returns -1.
     *
     * @return the inactive priority level or -1 if the behavior should not activate.
     */
    @Override
    protected int getInactivePriority() {
        float dst = getDistanceToTarget();
        if (dst < safeDistance && isTargetVisible()) {
            return priority;
        }
        return -1;
    }
}
