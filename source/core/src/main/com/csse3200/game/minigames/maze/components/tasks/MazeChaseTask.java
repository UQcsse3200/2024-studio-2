package com.csse3200.game.minigames.maze.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.tasks.ChaseTask;
import com.csse3200.game.components.tasks.MovementTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsLayer;

import static com.csse3200.game.minigames.maze.components.tasks.MazeMovementUtils.PADDING;

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
    movementTask = new MovementTask(MazeMovementUtils.adjustPos(target.getCenterPosition(), owner.getEntity()));
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
    Vector2 bestTargetPoint = null;
    Entity e = owner.getEntity();

    Vector2[] from = {
            e.getPosition(),
            e.getPosition().add(e.getScale().x, 0),
            e.getPosition().add(e.getScale().x, e.getScale().y),
            e.getPosition().add(0, e.getScale().y),
            e.getCenterPosition()
    };

    Vector2[] to = {
            target.getPosition().add(PADDING, PADDING),
            target.getPosition().add(target.getScale().x, 0).add(-PADDING, PADDING),
            target.getPosition().add(target.getScale().x, target.getScale().y).add(-PADDING, -PADDING),
            target.getPosition().add(0, target.getScale().y).add(PADDING, -PADDING),
            target.getCenterPosition()
    };

    // for each corner of this entity and each corner of target entity
    // is it possible to move in a straight line so that these corners collide?
    for (Vector2 a : from) {
      for (Vector2 b : to) {
        Vector2 moveToPos = e.getPosition().add(b).sub(a);

        if (MazeMovementUtils.canBeeLineTo(moveToPos, e)
                && (bestTargetPoint == null
                || a.dst(b) < e.getPosition().dst(bestTargetPoint))) {
          bestTargetPoint = moveToPos;
        }

      }
    }
    return bestTargetPoint;
  }

  @Override
  protected boolean isTargetVisible() {
    return targetReachablePoint() != null;
  }
}
