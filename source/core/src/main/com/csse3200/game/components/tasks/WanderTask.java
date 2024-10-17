package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.ai.tasks.Task;
import com.csse3200.game.minigames.maze.entities.mazenpc.MazeEntity;
import com.csse3200.game.utils.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wander around by moving a random position within a range of the starting position. Wait a little
 * bit between movements. Requires an entity with a PhysicsMovementComponent.
 */
public class WanderTask extends DefaultTask implements PriorityTask {
    private static final Logger logger = LoggerFactory.getLogger(WanderTask.class);
    
    private final Vector2 wanderRange;
    private final float waitTime;
    private Vector2 startPos;
    private MovementTask movementTask;
    private WaitTask waitTask;
    private Task currentTask;
    private boolean isSpawned = false;
    private final boolean isBoss;
    private static final String LEFT = "wanderLeft";
    private static final String RIGHT = "wanderRight";
    
    /**
     * @param wanderRange Distance in X and Y the entity can move from its position when start() is
     *     called.
     * @param waitTime How long in seconds to wait between wandering.
     */
    public WanderTask(Vector2 wanderRange, float waitTime, boolean isBoss) {
        this.wanderRange = wanderRange;
        this.waitTime = waitTime;
        this.isBoss = isBoss;
    }
    
    public boolean isBoss() {
        return isBoss;
    }
    
    @Override
    public int getPriority() {
        return 1; // Low priority task
    }
    
    /**
     * Checks if the entity has spawned yet, if not waits, else it wanders
     */
    @Override
    public void start() {
        super.start();
        startPos = owner.getEntity().getPosition();
        Vector2 newPos = getRandomPosInRange();
        if (this.isBoss) {
            // Wait for the spawn event to complete or for a specified duration before starting to wander
            waitTask = new WanderIdleTask(2.0f); // Adjust the wait time if needed
            waitTask.create(owner);
            movementTask = new MovementTask(getRandomPosInRange());
            movementTask.create(owner);
            movementTask.start();
            
            currentTask = movementTask;
        } else if(!isSpawned) {
            logger.debug("Triggering spawn event");
            if (owner.getEntity().isNormalEnemy()) {
                owner.getEntity().getEvents().trigger("animate", newPos, startPos);
                isSpawned = true;
            } else {
                this.owner.getEntity().getEvents().trigger("spawnStart");
                isSpawned = true;
            }
            // Wait for the spawn event to complete or for a specified duration before starting to wander
            waitTask = new WanderIdleTask(2.0f); // Adjust the wait time if needed
            waitTask.create(owner);
            waitTask.start();
            swapTask(waitTask);
        } else if (newPos.x - startPos.x < 0) {
            logger.debug("wandering right");
            this.owner.getEntity().getEvents().trigger(LEFT);
        } else {
            logger.debug("wandering left");
            this.owner.getEntity().getEvents().trigger(RIGHT);
        }
        this.owner.getEntity().getEvents().trigger("wanderStart");
        
    }
    
    @Override
    public void stop() {
        currentTask.stop();
    }
    
    @Override
    public void update() {
        if (currentTask.getStatus() != Status.ACTIVE) {
            if (currentTask == waitTask && isSpawned && !isBoss) {
                startWandering();
            } else if (currentTask == movementTask) {
                startWaiting();
            } else {
                startMoving();
            }
        }
        currentTask.update();
    }
    
    private void startWandering() {
        logger.debug("Starting wandering");
        Vector2 newPos = getRandomPosInRange();
        if (owner.getEntity().isNormalEnemy()) {
            owner.getEntity().getEvents().trigger("animate", newPos, owner.getEntity().getPosition());
        } else if (newPos.x - startPos.x < 0) {
            logger.debug("wandering right");
            this.owner.getEntity().getEvents().trigger(LEFT);
        } else {
            logger.debug("wandering left");
            this.owner.getEntity().getEvents().trigger(RIGHT);
        }
        
        if (owner.getEntity() instanceof MazeEntity) {
            movementTask = new MovementTask(newPos);
        } else {
            movementTask = new MovementTask(newPos, wanderRange);
        }
        movementTask.create(owner);
        movementTask.start();
        currentTask = movementTask;
    }
    
    private void startWaiting() {
        logger.debug("Starting waiting");
        owner.getEntity().getEvents().trigger("wait"); //plays still animation
        
        waitTask = new WaitTask(waitTime);
        waitTask.create(owner);
        swapTask(waitTask);
    }

  private void startMoving() {
    Vector2 newPos = getRandomPosInRange();

    if (isBoss) {
        logger.debug("Starting moving");
        movementTask.setTarget(getRandomPosInRange());
        swapTask(movementTask);
        return;
    }
    else if (newPos.x - startPos.x < 0) {
      this.owner.getEntity().getEvents().trigger(LEFT);
    } else {
      this.owner.getEntity().getEvents().trigger(RIGHT);
    }
    logger.debug("Starting moving");

    movementTask.setTarget(newPos);
    swapTask(movementTask);
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
    return RandomUtils.random(min, max);
  }
}
