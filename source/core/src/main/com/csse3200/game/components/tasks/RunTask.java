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

/** Chases a target entity until they get too far away or line of sight is lost */
public class RunTask extends DefaultTask implements PriorityTask {
  private final Entity target;
  private final int priority;
  private final float viewDistance;
  private final PhysicsEngine physics;
  private final DebugRenderer debugRenderer;
  private final RaycastHit hit = new RaycastHit();
  private MovementTask movementTask;

  /**
   * @param target The entity to chase.
   * @param priority Task priority when chasing (0 when not chasing).
   * @param viewDistance Maximum distance from the entity at which chasing can start.
   */
  public RunTask(Entity target, int priority, float viewDistance) {
    this.target = target;
    this.priority = priority;
    this.viewDistance = viewDistance;
    physics = ServiceLocator.getPhysicsService().getPhysics();
    debugRenderer = ServiceLocator.getRenderService().getDebug();
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

  }

  @Override
  public void stop() {
    super.stop();
    movementTask.stop();
  }

  @Override
  public int getPriority() {
    float dst = getDistanceToTarget();
    if (dst < viewDistance && isTargetVisible()) {
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
    float deltaY = targetPos.y - startPos.y;
    if (deltaY > 0) { // Moving Up
      if (deltaX > 0) {
        this.owner.getEntity().getEvents().trigger("runRightUp");
      } else if (deltaX < 0) {
        this.owner.getEntity().getEvents().trigger("runLeftUp");
      } else {
        this.owner.getEntity().getEvents().trigger("runUp");
      }
    } else if (deltaY < 0) { // Moving Down
      if (deltaX > 0) {
        this.owner.getEntity().getEvents().trigger("runRightDown");
      } else if (deltaX < 0) {
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
