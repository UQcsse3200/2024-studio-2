package com.csse3200.game.minigames.maze.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.components.tasks.ChaseTask;
import com.csse3200.game.components.tasks.MovementTask;
import com.csse3200.game.entities.Entity;

import javax.swing.*;

import static com.csse3200.game.minigames.maze.components.tasks.MazeMovementUtils.PADDING;
import static com.csse3200.game.minigames.maze.components.tasks.MazeMovementUtils.getHitBoxCorners;

/**
 * Chases a target entity until they get too far away or line of sight is lost.
 * Differs from the main game ChaseTask in that all movement is relative to the center
 * of both the entity this is attached to and the entity being chased.
 */
public class PatrolTask extends DefaultTask implements PriorityTask {
    private Vector2 bestTargetPoint;
    private Vector2[] patrolPoints;
    private int priority;
    private int currentPoint;

    protected MovementTask movementTask;

    /**
     * @param priority Task priority when chasing (0 when not chasing).
     * @param patrolPoints Points to patrol between.
     */
    public PatrolTask(int priority, Vector2[] patrolPoints) {
        super();
        this.priority = priority;
        this.patrolPoints = patrolPoints;
    }

    /**
     * Starts patrolling
     */
    @Override
    public void start() {
        super.start();
        currentPoint = 0;
        movementTask = new MovementTask(patrolPoints[currentPoint]);

        movementTask.create(owner);
        movementTask.start();

        this.owner.getEntity().getEvents().trigger("wanderStart");
    }

    /**
     * Updates the target position of the movement task to the current patrol point and
     * updates the patrol point to the next one if close to the current one.
     */
    @Override
    public void update() {
        Vector2 currentPos = owner.getEntity().getPosition();
        Vector2 targetPos = patrolPoints[currentPoint];
        if (currentPos.dst(targetPos) < 0.1f) {
            currentPoint = (currentPoint + 1) % patrolPoints.length;
            targetPos = patrolPoints[currentPoint];
        }
        movementTask.setTarget(targetPos);
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
        return priority;
    }
}
