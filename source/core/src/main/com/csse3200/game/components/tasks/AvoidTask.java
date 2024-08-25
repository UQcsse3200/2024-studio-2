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
    public AvoidTask(Entity target, int priority, float safeDistance, float minAvoidDistance) {
        // Call parent constructor
        super(target, priority, safeDistance, minAvoidDistance);
        this.safeDistance = safeDistance;
        this.minAvoidDistance = minAvoidDistance;
    }

    @Override
    public void start() {
        super.start();

        movementTask = new MovementTask(getAvoidanceTarget());
        movementTask.create(owner);
        movementTask.start();

        this.owner.getEntity().getEvents().trigger("avoidStart");
    }

    @Override
    public void update() {
        movementTask.setTarget(getAvoidanceTarget());
        movementTask.update();
        if (movementTask.getStatus() != Status.ACTIVE) {
            movementTask.start();
        }
    }

    @Override
    public int getPriority() {
        if (status == Status.ACTIVE) {
            return getActivePriority();
        }

        return getInactivePriority();
    }

    private Vector2 getAvoidanceTarget() {
        Vector2 entityPosition = owner.getEntity().getPosition();
        Vector2 targetPosition = target.getPosition();
        Vector2 directionAway = entityPosition.cpy().sub(targetPosition).nor();
        return entityPosition.cpy().add(directionAway.scl(minAvoidDistance));
    }

    @Override
    protected int getActivePriority() {
        float dst = super.getDistanceToTarget();
        if (dst > safeDistance || !isTargetVisible()) {
            return -1; // Safe distance reached, stop avoiding
        }
        return priority;
    }

    @Override
    protected int getInactivePriority() {
        float dst = getDistanceToTarget();
        if (dst < safeDistance && isTargetVisible()) {
            return priority;
        }
        return -1;
    }
}
