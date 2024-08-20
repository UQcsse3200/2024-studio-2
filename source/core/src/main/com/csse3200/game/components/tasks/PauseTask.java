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
import com.csse3200.game.components.ConfigComponent;
import com.csse3200.game.entities.configs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.csse3200.game.ui.ChatOverlay;

/** Pauses near a target entity until they move too far away or out of sight */
public class PauseTask extends DefaultTask implements PriorityTask {
    private final Entity target;
    private final int priority;
    private final float viewDistance;
    private final float maxPauseDistance;
    private final PhysicsEngine physics;
    private final DebugRenderer debugRenderer;
    private final RaycastHit hit = new RaycastHit();
    private MovementTask movementTask;
    private boolean hasApproached;
    private static final Logger logger = LoggerFactory.getLogger(PauseTask.class);
    private ChatOverlay hint;

    /**
     * @param target The entity to pause when seen.
     * @param priority Task priority when pausing (0 when not pausing).
     * @param viewDistance Maximum distance from the entity at which pausing can start.
     * @param maxPauseDistance Maximum distance from the entity to pause.
     */
    public PauseTask(Entity target, int priority, float viewDistance, float maxPauseDistance) {
        this.target = target;
        this.priority = priority;
        this.viewDistance = viewDistance;
        this.maxPauseDistance = maxPauseDistance;
        this.physics = ServiceLocator.getPhysicsService().getPhysics();
        this.debugRenderer = ServiceLocator.getRenderService().getDebug();
        this.hasApproached = false;
        this.hint = null;
    }

    @Override
    public void start() {
        super.start();
        movementTask = new MovementTask(target.getPosition());
        movementTask.create(owner);
        movementTask.start();

        Entity entity = this.owner.getEntity();
        ConfigComponent<?> configComponent = (ConfigComponent<?>) entity.getComponent(ConfigComponent.class);
        if (configComponent != null) {
            Object config = configComponent.getConfig();
            if (config instanceof CowConfig) {
                entity.getEvents().trigger("PausedCow");
            } else if (config instanceof LionConfig) {
                entity.getEvents().trigger("PausedLion");
            }
        } else {
            entity.getEvents().trigger("pauseStart");
        }
    }

    @Override
    public void update() {
        float distanceToTarget = getDistanceToTarget();
        //logger.info(String.format("%s", distanceToTarget));
        if (!hasApproached && distanceToTarget > maxPauseDistance && distanceToTarget <= viewDistance) {
            // Move towards the target until within maxPauseDistance
            movementTask.setTarget(target.getPosition());
            movementTask.update();
//            if (movementTask.getStatus() != Status.ACTIVE) {
//                movementTask.start();
//            }

            this.owner.getEntity().getEvents().trigger("pauseStart");
            //movementTask.stop();

        } else if (!hasApproached && distanceToTarget <= maxPauseDistance) {
            // NPC pauses when close enough to the target
            hasApproached = true;
            this.owner.getEntity().getEvents().trigger("paused");
            if (this.hint == null) {
                hint = new ChatOverlay("This is Charlie the Cow.");
            }
            logger.info("Medium");
            movementTask.stop();
        } else if (hasApproached && distanceToTarget > maxPauseDistance) {
            // If the player moves out of viewDistance, the NPC stops but does not follow the player
            this.hasApproached = false;
            this.owner.getEntity().getEvents().trigger("pauseEnd");
            if (this.hint != null) {
                hint.dispose();
                hint = null;
            }
            logger.info("end");
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

    private float getDistanceToTarget() {
        return owner.getEntity().getPosition().dst(target.getPosition());
    }

    private int getActivePriority() {
        float distance = getDistanceToTarget();
        if (distance > viewDistance || !isTargetVisible()) {
            return -1; // Too far or not visible, stop the task
        }
        return priority;
    }

    private int getInactivePriority() {
        float distance = getDistanceToTarget();
        if (distance < viewDistance && isTargetVisible()) {
            return priority;
        }
        return -1;
    }

    private boolean isTargetVisible() {
        Vector2 from = owner.getEntity().getCenterPosition();
        Vector2 to = target.getCenterPosition();

        // If there is an obstacle in the path to the player, not visible.
        if (physics.raycast(from, to, PhysicsLayer.OBSTACLE, hit)) {
            debugRenderer.drawLine(from, hit.point);
            return false;
        }
        debugRenderer.drawLine(from, to);
        return true;
    }
}