package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.raycast.RaycastHit;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;

/** Moves away from a target entity until a safe distance is reached or line of sight is lost */
public class AvoidTask extends DefaultTask implements PriorityTask {
    private final Entity target;
    private final int priority;
    private final float safeDistance;
    private final float minAvoidDistance;
    private final PhysicsEngine physics;
    private final DebugRenderer debugRenderer;
    private final RaycastHit hit = new RaycastHit();
    private MovementTask movementTask;

    /**
     * @param target The entity to avoid.
     * @param priority Task priority when avoiding (0 when not avoiding).
     * @param safeDistance Minimum distance to maintain from the target.
     * @param minAvoidDistance Minimum distance to avoid moving away from the target.
     */
    public AvoidTask(Entity target, int priority, float safeDistance, float minAvoidDistance) {
        this.target = target;
        this.priority = priority;
        this.safeDistance = safeDistance;
        this.minAvoidDistance = minAvoidDistance;
        this.physics = ServiceLocator.getPhysicsService().getPhysics();
        this.debugRenderer = ServiceLocator.getRenderService().getDebug();
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
    public void stop() {
        super.stop();
        movementTask.stop();
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

    private float getDistanceToTarget() {
        return owner.getEntity().getPosition().dst(target.getPosition());
    }

    private int getActivePriority() {
        float dst = getDistanceToTarget();
        if (dst > safeDistance || !isTargetVisible()) {
            return -1; // Safe distance reached, stop avoiding
        }
        return priority;
    }

    private int getInactivePriority() {
        float dst = getDistanceToTarget();
        if (dst < safeDistance && isTargetVisible()) {
            return priority;
        }
        return -1;
    }

    private boolean isTargetVisible() {
        Vector2 from = owner.getEntity().getCenterPosition();
        Vector2 to = target.getCenterPosition();

        // If there is an obstacle in the path to the target, not visible.
        if (physics.raycast(from, to, PhysicsLayer.OBSTACLE, hit)) {
            debugRenderer.drawLine(from, hit.point);
            return false;
        }
        debugRenderer.drawLine(from, to);
        return true;
    }
}
