package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.raycast.RaycastHit;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GriffinTask extends DefaultTask implements PriorityTask {
    private static final Logger logger = LoggerFactory.getLogger(GriffinTask.class);
    private final Entity target;
    private final int priority;
    private final float viewDistance;
    private final PhysicsEngine physics;
    private final DebugRenderer debugRenderer;
    private final RaycastHit hit = new RaycastHit();
    private MovementTask movementTask;

    // Shooting
    private final float waitTime; // Time to wait between firing
    private long lastShotTime;    // Time of the last shot
    private final float shootRange; // Range within which to shoot gust of wind
    private final GameTime timer; // Game timer for tracking shot intervals

    public GriffinTask(Entity target, int priority, float viewDistance, float waitTime, float shootRange) {
        this.target = target;
        this.priority = priority;
        this.viewDistance = viewDistance;
        this.physics = ServiceLocator.getPhysicsService().getPhysics();
        this.debugRenderer = ServiceLocator.getRenderService().getDebug();

        this.waitTime = waitTime;
        this.shootRange = shootRange;
        this.timer = ServiceLocator.getTimeSource();
        this.lastShotTime = timer.getTime();
    }

    @Override
    public void start() {
        super.start();
        movementTask = new MovementTask(newPosition(true));
        movementTask.create(owner);
        movementTask.start();
    }

    @Override
    public void update() {
        movementTask.setTarget(newPosition(false));
        movementTask.update();
        if (movementTask.getStatus() != Status.ACTIVE) {
            movementTask.start();
        }

        if (timer.getTime() - lastShotTime > waitTime || getDistanceToTarget() >= shootRange) {
            shootWindGust();
        }
    }

    @Override
    public void stop() {
        super.stop();
        movementTask.stop();
    }

    @Override
    public int getPriority() {
        float dst = getDistanceToTarget();
        if (dst < viewDistance || isTargetVisible()) {
            return priority;
        }
        return -1;
    }

    private float getDistanceToTarget() {
        return owner.getEntity().getPosition().dst(target.getPosition());
    }

    private boolean isTargetVisible() {
        Vector2 from = owner.getEntity().getCenterPosition();
        Vector2 to = target.getCenterPosition();

        if (physics.raycast(from, to, PhysicsLayer.OBSTACLE, hit)) {
            debugRenderer.drawLine(from, hit.point);
            return false;
        }
        debugRenderer.drawLine(from, to);
        return true;
    }

    private Vector2 newPosition(boolean trigger) {
        Vector2 currentPos = owner.getEntity().getPosition();
        Vector2 targetPos = target.getPosition();

        float deltaX = currentPos.x - targetPos.x;
        float deltaY = currentPos.y - targetPos.y;
        Vector2 newPos = new Vector2(currentPos.x + deltaX, currentPos.y + deltaY);
        if (trigger) {
            triggerDirection(newPos, owner.getEntity().getPosition());
        }

        return newPos;
    }

    private void triggerDirection(Vector2 targetPos, Vector2 startPos) {
        float deltaX = targetPos.x - startPos.x;
        if (deltaX > 0) { // Moving Left
            this.owner.getEntity().getEvents().trigger("chaseLeft");
        } else { // Moving Right
            this.owner.getEntity().getEvents().trigger("chaseRight");
        }
    }

    private void shootWindGust() {
        logger.debug("Shooting gust of wind at target");
        lastShotTime = timer.getTime();  // Update the time of the last shot
        owner.getEntity().getEvents().trigger("spawnWindGust", owner.getEntity()); // Trigger the event to shoot
    }
}
