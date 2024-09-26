package com.csse3200.game.minigames.maze.components.tasks;

import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.minigames.maze.Maze;
import com.csse3200.game.minigames.maze.areas.terrain.MazeTerrainFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Move to a given position, finishing when you get close enough. Requires an entity with a
 * PhysicsMovementComponent.
 */
public class MazeHuntTask extends DefaultTask implements PriorityTask {
    private static final Logger logger = LoggerFactory.getLogger(MazeHuntTask.class);
    private final Entity target;  // Target for the entity to get to
    private MazePathFindingTask pathFindingTask;  // Instance of the path finding task
    private final Maze maze;  // The maze instance
    private final int priority;  // The priority of this task

    /**
     * Initialiser for the MazeHuntTask
     * @param target the target to get to
     * @param maze the maze instance
     * @param priority the priority of this task
     */
    public MazeHuntTask(Entity target, Maze maze, int priority) {
        this.target = target;
        this.maze = maze;
        this.priority = priority;
    }

    /**
     * Get the targets position
     * @return the targets position
     */
    private GridPoint2 getTargetGridPoint() {
        return MazeTerrainFactory.worldPosToGridPos(target.getCenterPosition());
    }

    /**
     * Starts this task
     */
    @Override
    public void start() {
        super.start();
        pathFindingTask = new MazePathFindingTask(getTargetGridPoint(), maze);
        pathFindingTask.create(owner);
        pathFindingTask.start();
        this.owner.getEntity().getEvents().trigger("wanderStart");
        logger.debug("Starting movement towards {}", target);
    }

    /**
     * Updates the task, updates the targets position and starts the task if it is idle
     */
    @Override
    public void update() {
        pathFindingTask.setTarget(getTargetGridPoint());
        pathFindingTask.update();
        if (pathFindingTask.getStatus() != Status.ACTIVE) {
            pathFindingTask.start();
        }
    }

    /**
     * Stops this task
     */
    @Override
    public void stop() {
        super.stop();
        pathFindingTask.stop();
        logger.debug("Stopping movement");
    }

    /**
     * Get task priority
     * @return the priority of the MazeHuntTask
     */
    @Override
    public int getPriority() {
        return priority;
    }
}
