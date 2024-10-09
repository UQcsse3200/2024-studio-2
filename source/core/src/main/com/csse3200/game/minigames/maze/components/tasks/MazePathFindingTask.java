package com.csse3200.game.minigames.maze.components.tasks;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.components.tasks.MovementTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.minigames.maze.Maze;
import com.csse3200.game.minigames.maze.areas.terrain.MazeTerrainFactory;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.raycast.RaycastHit;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.csse3200.game.minigames.maze.components.tasks.MazeMovementUtils.getHitBoxCorners;

/**
 * Move to a given position, finishing when you get close enough. Requires an entity with a
 * PhysicsMovementComponent.
 */
public class MazePathFindingTask extends DefaultTask {
    private static final Logger logger = LoggerFactory.getLogger(MazePathFindingTask.class);
    private GridPoint2 target;
    private MovementTask movementTask;
    private final Maze maze;
    List<GridPoint2> path;
    private final PhysicsEngine physics;
    private final DebugRenderer debugRenderer;
    private final RaycastHit hit = new RaycastHit();

    public MazePathFindingTask(GridPoint2 target, Maze maze) {
        this.target = target;
        this.maze = maze;
        physics = ServiceLocator.getPhysicsService().getPhysics();
        debugRenderer = ServiceLocator.getRenderService().getDebug();
    }

    /**
     * Compute the path to get to the target
     */
    private void computePath() {
        Vector2 entityWorldPos = owner.getEntity().getCenterPosition();
        GridPoint2 entityGridPos = MazeTerrainFactory.worldPosToGridPos(entityWorldPos);
        Maze.BreadthFirstSearch bfs = maze.new BreadthFirstSearch(entityGridPos);
        path = bfs.getShortestPath(target).reversed();
    }

    /**
     * Gets the next move in the path computed
     *
     * @return the next movement
     */
    private Vector2 getNextMovement() {
        if (path.size() > 1) {
            Entity e = owner.getEntity();
            Vector2 centerTo = new Vector2(path.get(path.size() - 2).x + .5f, path.get(path.size() - 2).y + .5f);
            int success = 0;
            Vector2[] corners = getHitBoxCorners(e, 0);
            for (Vector2 from : corners) {
                Vector2 to = centerTo.cpy().sub(e.getCenterPosition().sub(from));

                // If there is an obstacle in the path to the player, not visible.
                if (physics.raycast(from, to, PhysicsLayer.OBSTACLE, hit)) {
                    debugRenderer.drawLine(from, hit.getPoint());
                    break;
                }
                debugRenderer.drawLine(from, to);
                success++;
            }
            if (success == 4) {
                path.removeLast();
            }
        }

        return MazeMovementUtils.adjustPos(new Vector2(path.getLast().x + .5f, path.getLast().y + .5f), owner.getEntity());
    }

    /**
     * Starts this task and the movement task
     */
    @Override
    public void start() {
        super.start();
        computePath();
        movementTask = new MovementTask(getNextMovement());
        movementTask.create(owner);
        movementTask.start();
    }

    /**
     * Updates this task, updates the next target in the movement task, computes new path
     */
    @Override
    public void update() {
        movementTask.setTarget(getNextMovement());
        movementTask.update();
        if (movementTask.getStatus() != Status.ACTIVE) {
            computePath();
            movementTask.start();
        }
    }

    /**
     * Sets the target to find the path to
     *
     * @param target the target to find the path to
     */
    public void setTarget(GridPoint2 target) {
        this.target = target;
        computePath();
    }

    /**
     * Stops this task from running
     */
    @Override
    public void stop() {
        super.stop();
        movementTask.stop();
        logger.debug("Stopping path finding");
    }
}
