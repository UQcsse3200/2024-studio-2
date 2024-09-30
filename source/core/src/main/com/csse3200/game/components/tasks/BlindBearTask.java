package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.ai.tasks.Task;
import com.csse3200.game.components.npc.FrogAnimationController;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.raycast.RaycastHit;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.utils.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wander around by moving a random position within a range of the starting position. Wait a little
 * bit between movements. Requires an entity with a PhysicsMovementComponent.
 */
public class BlindBearTask extends DefaultTask implements PriorityTask {
    private static final Logger logger = LoggerFactory.getLogger(BlindBearTask.class);
    
    private final Vector2 wanderRange;
    private final float waitTime;
    private Vector2 startPos;
    private MovementTask movementTask;
    private WaitTask waitTask;
    private Task currentTask;
    private boolean isSpawned = false;
    private int priority;
    private Entity target;
    private float viewDistance;
    protected final PhysicsEngine physics;
    protected final DebugRenderer debugRenderer;
    protected final RaycastHit hit = new RaycastHit();
    
    
    /**
     * @param wanderRange Distance in X and Y the entity can move from its position when start() is
     *     called.
     * @param waitTime How long in seconds to wait between wandering.
     */
    public BlindBearTask(Vector2 wanderRange, float waitTime, int priority, Entity target, float viewDistance) {
        this.wanderRange = wanderRange;
        this.waitTime = waitTime;
        this.priority = priority;
        this.target = target;
        this.viewDistance = viewDistance;
        physics = ServiceLocator.getPhysicsService().getPhysics();
        debugRenderer = ServiceLocator.getRenderService().getDebug();
    }
    
    @Override
    public int getPriority() {
        if (getDistanceToTarget() < viewDistance && isTargetVisible()) {
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
        
        // If there is an obstacle in the path to the player, not visible.
        if (physics.raycast(from, to, PhysicsLayer.OBSTACLE, hit)) {
            debugRenderer.drawLine(from, hit.point);
            return false;
        }
        
        debugRenderer.drawLine(from, to);
        return !target.getComponent(PlayerActions.class).isMoving();
    }
    
    
    /**
     * Checks if the entity has spawned yet, if not waits, else it wanders
     */
    @Override
    public void start() {
        super.start();
        startPos = owner.getEntity().getPosition();
        Vector2 newPos = getRandomPosInRange();
        if(!isSpawned) {
            logger.debug("Triggering spawn event");
            this.owner.getEntity().getEvents().trigger("spawnStart");
            isSpawned = true;
            
            // Wait for the spawn event to complete or for a specified duration before starting to wander
            waitTask = new WaitTask(2.0f); // Adjust the wait time if needed
            waitTask.create(owner);
            swapTask(waitTask);
        } else if (newPos.x - startPos.x < 0) {
            logger.debug("wandering right");
            this.owner.getEntity().getEvents().trigger("wanderLeft");
        } else {
            logger.debug("wandering left");
            this.owner.getEntity().getEvents().trigger("wanderRight");
        }
    }
    
    @Override
    public void update() {
        if (currentTask.getStatus() != Status.ACTIVE) {
            if (currentTask == waitTask && isSpawned) {
                startWandering();
            } else if (currentTask == movementTask) {
                startWaiting();
            }
        }
        currentTask.update();
    }
    
    private void startWandering() {
        logger.debug("Starting wandering");
        Vector2 newPos = getRandomPosInRange();
        if (newPos.x - startPos.x < 0) {
            logger.debug("wandering right");
            this.owner.getEntity().getEvents().trigger("wanderLeft");
        } else {
            logger.debug("wandering left");
            this.owner.getEntity().getEvents().trigger("wanderRight");
        }
        movementTask = new MovementTask(newPos, wanderRange);
        movementTask.create(owner);
        movementTask.start();
        currentTask = movementTask;
    }
    
    private void startWaiting() {
        logger.debug("Starting waiting");
        if (owner.getEntity().getComponent(FrogAnimationController.class) != null) {
            owner.getEntity().getEvents().trigger("spawnStart"); //plays still animation
        }
        waitTask = new WaitTask(waitTime);
        waitTask.create(owner);
        swapTask(waitTask);
    }
    
    private void swapTask(Task newTask) {
        if (currentTask != null) {
            currentTask.stop();
        }
        currentTask = newTask;
        currentTask.start();
    }
    
    private Vector2 getRandomPosInRange() {
        Vector2 halfRange = wanderRange.cpy().scl(0.5f);
        Vector2 min = startPos.cpy().sub(halfRange);
        Vector2 max = startPos.cpy().add(halfRange);
        Vector2 targetPosition = RandomUtils.random(min, max);
        // get my pos
        // get player pos
        // x + 1/(my pos - player pos)
        targetPosition.add(3/(startPos.x - target.getCenterPosition().x), 3/(startPos.y - target.getCenterPosition().y));
        return targetPosition;
    }
}
