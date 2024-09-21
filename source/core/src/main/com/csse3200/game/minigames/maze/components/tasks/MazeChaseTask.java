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
    movementTask.setTarget(targetReachablePoint());
    movementTask.update();
    if (movementTask.getStatus() != Status.ACTIVE) {
      movementTask.start();
    }
  }

  @Override
  protected float getDistanceToTarget() {
    return owner.getEntity().getCenterPosition().dst(target.getCenterPosition());
  }

  private Vector2 targetReachablePoint() {
    Vector2 reachablePoint = null;
    Entity e = owner.getEntity();
    float PADDING = 0.03f;
    Vector2[] from = {
            e.getPosition().add(-PADDING, -PADDING),
            e.getPosition().add(e.getScale().x, 0).add(PADDING, -PADDING),
            e.getPosition().add(e.getScale().x, e.getScale().y).add(PADDING, PADDING),
            e.getPosition().add(0, e.getScale().y).add(-PADDING, PADDING)
    };
    Vector2[] to = {
            target.getPosition().add(PADDING, PADDING),
            target.getPosition().add(target.getScale().x, 0).add(-PADDING, PADDING),
            target.getPosition().add(target.getScale().x, target.getScale().y).add(-PADDING, -PADDING),
            target.getPosition().add(0, target.getScale().y).add(PADDING, -PADDING)
    };

    Vector2[] resultRect = new Vector2[4];


    for (Vector2 a : from) {
      for (Vector2 b : to) {
        int success = 0;
        int i = 0;
        for (Vector2 test : from) {
          resultRect[i] = b.cpy().sub(a.cpy().sub(test));
          // If there is an obstacle in the path to the player, not visible.
          if (physics.raycast(test, resultRect[i], PhysicsLayer.OBSTACLE, hit)) {
            debugRenderer.drawLine(test, hit.point);
            break;
          }
          success++;
          debugRenderer.drawLine(test, resultRect[i]);
          i++;
        }

        if (success == from.length) {
          for (int rectPoint = 0; rectPoint < 4; rectPoint++) {
            // If there is an obstacle in the path to the player, not visible.
            if (physics.raycast(resultRect[rectPoint], resultRect[(rectPoint+1)%4], PhysicsLayer.OBSTACLE, hit)) {
              debugRenderer.drawLine(resultRect[rectPoint], hit.point);
              break;
            }
            success++;
            debugRenderer.drawLine(resultRect[rectPoint], resultRect[(rectPoint+1)%4]);
          }
          if (success == from.length + 4) {
            if (reachablePoint == null || a.dst(b) < e.getPosition().dst(reachablePoint))
              reachablePoint = e.getPosition().sub(a.cpy().sub(b));
          }
        }
      }
    }
    return reachablePoint;
  }

  @Override
  protected boolean isTargetVisible() {
    return targetReachablePoint() != null;
  }
}
