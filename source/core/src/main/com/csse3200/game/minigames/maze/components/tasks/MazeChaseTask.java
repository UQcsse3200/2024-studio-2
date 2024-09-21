package com.csse3200.game.minigames.maze.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.components.tasks.ChaseTask;
import com.csse3200.game.components.tasks.MovementTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.raycast.RaycastHit;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.services.ServiceLocator;

/** Chases a target entity until they get too far away or line of sight is lost.
 *  Differs from the main game ChaseTask in that all movement is relative to the center
 *  of both the entity this is attached to and the entity being chased. */
public class MazeChaseTask extends ChaseTask {
  /**
   * @param target The entity to chase.
   * @param priority Task priority when chasing (0 when not chasing).
   * @param viewDistance Maximum distance from the entity at which chasing can start.
   * @param maxChaseDistance Maximum distance from the entity while chasing before giving up.
   */
  public MazeChaseTask(Entity target, int priority, float viewDistance, float maxChaseDistance) {
    super(target, priority, viewDistance, maxChaseDistance, false);
  }

  @Override
  public void start() {
    super.start();
    movementTask = new MovementTask(MovementRelativeToCenterPos.adjustPos(target.getCenterPosition(), owner.getEntity()));
    movementTask.create(owner);
    movementTask.start();

    this.owner.getEntity().getEvents().trigger("chaseStart");
  }

  @Override
  public void update() {
    movementTask.setTarget(MovementRelativeToCenterPos.adjustPos(target.getCenterPosition(), owner.getEntity()));
    movementTask.update();
    if (movementTask.getStatus() != Status.ACTIVE) {
      movementTask.start();
    }
  }

  @Override
  protected float getDistanceToTarget() {
    return owner.getEntity().getCenterPosition().dst(target.getCenterPosition());
  }

  @Override
  protected boolean isTargetVisible() {
    Vector2 to = target.getCenterPosition();
    Entity e = owner.getEntity();
    Vector2[] points = {
            e.getPosition(),
            e.getCenterPosition(),
            e.getPosition().add(e.getScale().x, 0),
            e.getPosition().add(0, e.getScale().y),
            e.getPosition().add(e.getScale().x, e.getScale().y)
    };
    for (Vector2 from : points) {
      // If there is an obstacle in the path to the player, not visible.
      Vector2 offsetTo = to.cpy().sub(e.getCenterPosition().sub(from));
      if (physics.raycast(from, offsetTo, PhysicsLayer.OBSTACLE, hit)) {
        debugRenderer.drawLine(from, hit.point);
        return false;
      }
      debugRenderer.drawLine(from, offsetTo);
    }
    return true;
  }
}
