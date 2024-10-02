
package com.csse3200.game.components.tasks;

import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A task that allows an entity to wait for a set time and then shoot a projectile at a target.
 */
public class ShootTask extends DefaultTask implements PriorityTask {
  private static final Logger logger = LoggerFactory.getLogger(ShootTask.class);
  private static final int PRIORITY = 5;
  private final float waitTime; // Time to wait between firing
  private final Entity target;  // The target entity to aim at
  private final float range;    // Range within which to start shooting
  private final GameTime timer; // Game timer for tracking shot intervals
  private long lastShotTime;    // Time of the last shot
  private int numShots = 0;     // Number of shots fired

  /**
   * A task that allows an entity to wait for a set time and then shoot a projectile at a target.
   *
   * @param waitTime Time in seconds to wait between shots.
   * @param target   The target entity to shoot at.
   * @param range    The distance within which the entity will stop chasing and start shooting.
   */
  public ShootTask(float waitTime, Entity target, float range) {
    this.waitTime = waitTime;
    this.target = target;
    this.range = range;
    this.timer = ServiceLocator.getTimeSource();  // Use the GameTime service from ServiceLocator
    this.lastShotTime = timer.getTime();
  }

  @Override
  public int getPriority() {
    if (status == Status.ACTIVE) {
      return getActivePriority();
    }
    return getInactivePriority();
  }

  @Override
  public void start() {
    super.start();
    startWaiting();  // Start the task in a waiting state
  }

  @Override
  public void update() {
    // Check if the entity should switch from waiting to shooting
    if (((timer.getTime() - lastShotTime) > waitTime) || numShots == 0) {
      startShooting();  // Start shooting if enough time has passed or no shots have been fired yet
    }
    // this.owner.getEntity().getEvents().trigger("standing");  // Update the current state
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
    if (dst > range || (timer.getTimeSince(lastShotTime) < waitTime)) {
      return -1; // Too far, stop shooting
    }
    return PRIORITY;
  }

  /**
   * Gets the priority when the task is inactive, based on the distance to the target.
   *
   * @return The priority value, or -1 if out of range.
   */
  private int getInactivePriority() {
    float dst = getDistanceToTarget();
    if (dst <= range && (timer.getTimeSince(lastShotTime) > waitTime)) {
      return PRIORITY;
    }
    return -1;
  }

  /**
   * Starts the waiting state before shooting.
   */
  private void startWaiting() {
    logger.debug("Starting waiting");
    this.owner.getEntity().getEvents().trigger("wait");
    // Perform any additional logic for waiting, like animations or sounds
  }

  /**
   * Fires a projectile at the target.
   */
  private void startShooting() {
    logger.debug("Shooting at target");
    // this.owner.getEntity().getEvents().trigger("shooting");  // Trigger shooting event
    lastShotTime = timer.getTime();  // Update the time of the last shot
    numShots++;  // Increase shot count
    owner.getEntity().getEvents().trigger("Shoot", owner.getEntity(), owner.getEntity().getEnemyType());
  }
}