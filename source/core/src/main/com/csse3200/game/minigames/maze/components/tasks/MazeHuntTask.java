package com.csse3200.game.minigames.maze.components.tasks;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.components.tasks.MovementTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.minigames.maze.Maze;
import com.csse3200.game.minigames.maze.areas.terrain.MazeTerrainFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Move to a given position, finishing when you get close enough. Requires an entity with a
 * PhysicsMovementComponent.
 */
public class MazeHuntTask extends DefaultTask implements PriorityTask {
  private static final Logger logger = LoggerFactory.getLogger(MazeHuntTask.class);
  private Entity target;

  private MazePathFindingTask pathFindingTask;

  private final Maze maze;

  private final int priority;

  public MazeHuntTask(Entity target, Maze maze, int priority) {
    this.target = target;
    this.maze = maze;
    this.priority = priority;
  }

  private GridPoint2 getTargetGridPoint() {
      return MazeTerrainFactory.worldPosToGridPos(target.getCenterPosition());
  }

  @Override
  public void start() {
    super.start();
    pathFindingTask = new MazePathFindingTask(getTargetGridPoint(), maze);
    pathFindingTask.create(owner);
    pathFindingTask.start();
    this.owner.getEntity().getEvents().trigger("wanderStart");
    logger.debug("Starting movement towards {}", target);
  }

  @Override
  public void update() {
    pathFindingTask.setTarget(getTargetGridPoint());
    pathFindingTask.update();
    if (pathFindingTask.getStatus() != Status.ACTIVE) {
      pathFindingTask.start();
    }
  }

  @Override
  public void stop() {
    super.stop();
    pathFindingTask.stop();
    logger.debug("Stopping movement");
  }

  @Override
  public int getPriority() {
    return priority;
  }
}
