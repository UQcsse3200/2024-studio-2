package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.ai.tasks.Task;
import com.csse3200.game.utils.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wander around by moving a random position within a range of the starting position. Wait a little
 * bit between movements. Requires an entity with a PhysicsMovementComponent.
 */
public class SpecialWanderTask extends DefaultTask implements PriorityTask {
  private static final Logger logger = LoggerFactory.getLogger(SpecialWanderTask.class);

  private final Vector2 wanderRange;
  private final float waitTime;
  private Vector2 startPos;
  private MovementTask movementTask;
  private WaitTask waitTask;
  private Task currentTask;
  private boolean isSpawned = false;

  /**
   * @param wanderRange Distance in X and Y the entity can move from its position when start() is
   *     called.
   * @param waitTime How long in seconds to wait between wandering.
   */
  public SpecialWanderTask(Vector2 wanderRange, float waitTime) {
    this.wanderRange = wanderRange;
    this.waitTime = waitTime;
  }

  @Override
  public int getPriority() {
    return 1; // Low priority task
  }

  @Override
  public void start() {
    super.start();
    startPos = owner.getEntity().getPosition();
    Vector2 newPos = getRandomPosInRange();

      if (!isSpawned) {
          logger.debug("Triggering spawn event");
          this.owner.getEntity().getEvents().trigger("spawnStart");
          isSpawned = true;

          // Wait for the spawn event to complete or for a specified duration before starting to wander
          waitTask = new WaitTask(2.0f); // Adjust the wait time if needed
          waitTask.create(owner);
          swapTask(waitTask);
      }

      triggerDirection(newPos, startPos);
  }

  @Override
  public void update() {
    if (currentTask.getStatus() != Status.ACTIVE) {
      if (currentTask == waitTask && isSpawned) {
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
    Vector2 targetPos = getRandomPosInRange();
    Vector2 currentPos = owner.getEntity().getPosition();
    triggerDirection(targetPos, currentPos);

    logger.debug("Starting wandering");
    movementTask = new MovementTask(targetPos, wanderRange);
    movementTask.create(owner);
    movementTask.start();
    currentTask = movementTask;
  }

  private void startWaiting() {
    logger.debug("Starting waiting");
    waitTask = new WaitTask(waitTime);
    waitTask.create(owner);
    swapTask(waitTask);
  }

  private void startMoving() {
    Vector2 targetPos = getRandomPosInRange();

    triggerDirection(targetPos, startPos);

    movementTask.setTarget(targetPos);
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

  private void triggerDirection(Vector2 targetPos, Vector2 startPos) {
    float deltaX = targetPos.x - startPos.x;
    float deltaY = targetPos.y - startPos.y;
    if (deltaY > 0) { // Moving Up
      if (deltaX > 0) {
        this.owner.getEntity().getEvents().trigger("wanderRight");
        this.owner.getEntity().getEvents().trigger("runRightUp");
      } else if (deltaX < 0) {
        this.owner.getEntity().getEvents().trigger("wanderLeft");
        this.owner.getEntity().getEvents().trigger("runLeftUp");
      } else {
        this.owner.getEntity().getEvents().trigger("runUp");
      }
    } else if (deltaY < 0) { // Moving Down
      if (deltaX > 0) {
        this.owner.getEntity().getEvents().trigger("wanderRight");
        this.owner.getEntity().getEvents().trigger("runRightDown");
      } else if (deltaX < 0) {
        this.owner.getEntity().getEvents().trigger("wanderLeft");
        this.owner.getEntity().getEvents().trigger("runLeftDown");
      } else {
        this.owner.getEntity().getEvents().trigger("runDown");
      }
    } else { // Horizontal Movement
      if (deltaX > 0) {
        this.owner.getEntity().getEvents().trigger("runRight");
      } else {
        this.owner.getEntity().getEvents().trigger("runLeft");
      }
    }
  }

}
