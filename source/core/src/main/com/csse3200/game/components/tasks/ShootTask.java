
package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Task that waits for a set amount of time and then fires a projectile at a target.
 */
public class ShootTask extends DefaultTask implements PriorityTask {
  private static final Logger logger = LoggerFactory.getLogger(ShootTask.class);
  private final int priority = 5;
  private final float waitTime; // Time to wait between firing
  private final Entity target;  // The target entity to aim at
  private final float range;    // Range within which to start shooting
  private final GameTime timer; // Game timer for tracking shot intervals
  private long lastShotTime;    // Time of the last shot
  private int numShots = 0;     // Number of shots fired

  /**
   * Creates a shoot task that waits and then fires a projectile at the target.
   *
   * @param waitTime How long in seconds to wait between firing a projectile.
   * @param target   The target for shooting.
   * @param range    The range before the entity stops chasing and starts shooting.
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
    if ((timer.getTime() - lastShotTime > waitTime * 1000) || numShots == 0) {
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
   * Returns the priority if the task is currently active.
   *
   * @return The current priority.
   */
  private int getActivePriority() {
    float dst = getDistanceToTarget();
    if (dst > range) {
      return -1; // Too far, stop chasing
    }
    return priority;
  }

  /**
   * Returns the priority if the task is currently inactive.
   *
   * @return The current priority.
   */
  private int getInactivePriority() {
    float dst = getDistanceToTarget();
    if (dst <= range) {
      return priority;
    }
    return -1;
  }

  /**
   * Makes the entity wait before shooting again.
   */
  private void startWaiting() {
    logger.debug("Starting waiting");
    this.owner.getEntity().getEvents().trigger("wait");
    // Perform any additional logic for waiting, like animations or sounds
  }

  /**
   * Makes the entity shoot at the target.
   */
  private void startShooting() {
    logger.debug("Shooting at target");
    // this.owner.getEntity().getEvents().trigger("shooting");  // Trigger shooting event
    lastShotTime = timer.getTime();  // Update the time of the last shot
    numShots++;  // Increase shot count
    Entity banana = ProjectileFactory.createBanana(owner.getEntity());
    // spawn banana
  }
}