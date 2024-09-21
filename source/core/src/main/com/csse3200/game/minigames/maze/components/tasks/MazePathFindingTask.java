package com.csse3200.game.minigames.maze.components.tasks;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.components.tasks.MovementTask;
import com.csse3200.game.minigames.Grid;
import com.csse3200.game.minigames.maze.Maze;
import com.csse3200.game.minigames.maze.areas.MazeGameArea;
import com.csse3200.game.minigames.maze.areas.terrain.MazeTerrainFactory;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Move to a given position, finishing when you get close enough. Requires an entity with a
 * PhysicsMovementComponent.
 */
public class MazePathFindingTask extends DefaultTask implements PriorityTask {
  private static final Logger logger = LoggerFactory.getLogger(MazePathFindingTask.class);
  private GridPoint2 target;

  private MovementTask movementTask;

  private final Maze maze;

  List<GridPoint2> path;

  public MazePathFindingTask(GridPoint2 target, Maze maze) {
    this.target = target;
    this.maze = maze;
  }

  private void computePath() {
    Vector2 entityWorldPos = owner.getEntity().getCenterPosition();
    GridPoint2 entityGridPos = MazeTerrainFactory.worldPosToGridPos(entityWorldPos);
    Maze.breadthFirstSearch bfs = maze.new breadthFirstSearch(entityGridPos);
    path = bfs.getShortestPath(target).reversed();
  }

  private Vector2 getNextMovement() {
    Vector2 targetWorldPos;
    Vector2 entityWorldPos = owner.getEntity().getCenterPosition();
    while (true) {
      targetWorldPos = new Vector2(path.getLast().x + .5f, path.getLast().y + .5f);
      if (path.size() == 1 || targetWorldPos.dst(entityWorldPos) > 0.1f) {
        break;
      }
      path.removeLast();
    }
    return MovementRelativeToCenterPos.adjustPos(targetWorldPos, owner.getEntity());
  }

  @Override
  public void start() {
    super.start();
    computePath();
    movementTask = new MovementTask(getNextMovement());
    movementTask.create(owner);
    movementTask.start();
    this.owner.getEntity().getEvents().trigger("wanderStart");
    logger.debug("Starting movement towards {}", target);
  }

  @Override
  public void update() {
    movementTask.setTarget(getNextMovement());
    movementTask.update();
    if (movementTask.getStatus() != Status.ACTIVE) {
      computePath();
      movementTask.start();
    }
  }

  public void setTarget(GridPoint2 target) {
    this.target = target;
    computePath();
  }

  @Override
  public void stop() {
    super.stop();
    movementTask.stop();
    logger.debug("Stopping movement");
  }

  @Override
  public int getPriority() {
    return 2;
  }
}
