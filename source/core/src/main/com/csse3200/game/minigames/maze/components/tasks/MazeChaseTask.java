package com.csse3200.game.minigames.maze.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.tasks.ChaseTask;
import com.csse3200.game.components.tasks.MovementTask;
import com.csse3200.game.entities.Entity;

import static com.csse3200.game.minigames.maze.components.tasks.MazeMovementUtils.PADDING;
import static com.csse3200.game.minigames.maze.components.tasks.MazeMovementUtils.getHitBoxCorners;

/**
 * Chases a target entity until they get too far away or line of sight is lost.
 * Differs from the main game ChaseTask in that all movement is relative to the center
 * of both the entity this is attached to and the entity being chased.
 * <p>
 * THis is used for the eel chasing. Anglular fish uses:
 */
public class MazeChaseTask extends ChaseTask {
    private Vector2 bestTargetPoint;

    /**
     * @param target           The entity to chase.
     * @param priority         Task priority when chasing (0 when not chasing).
     * @param viewDistance     Maximum distance from the entity at which chasing can start.
     * @param maxChaseDistance Maximum distance from the entity while chasing before giving up.
     */
    public MazeChaseTask(Entity target, int priority, float viewDistance, float maxChaseDistance) {
        super(target, priority, viewDistance, maxChaseDistance, false);
    }

    /**
     * Starts chasing the target
     */
    @Override
    public void start() {
        super.start();
        movementTask = new MovementTask(bestTargetPoint);

        movementTask.create(owner);
        movementTask.start();

        this.owner.getEntity().getEvents().trigger("chaseStart");
    }

    /**
     * Updates the chasers position, target and animation
     */
    @Override
    public void update() {
        movementTask.setTarget(bestTargetPoint);
        movementTask.update();
        if (target.getCenterPosition().x < owner.getEntity().getCenterPosition().x) {
            this.owner.getEntity().getEvents().trigger("faceLeft");
        } else {
            this.owner.getEntity().getEvents().trigger("faceRight");
        }
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
        bestTargetPoint = null;
        Entity e = owner.getEntity();

        Vector2[] from = getHitBoxCorners(e, 0);
        Vector2[] to = getHitBoxCorners(target, -PADDING);

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
        return bestTargetPoint != null;
    }
}
