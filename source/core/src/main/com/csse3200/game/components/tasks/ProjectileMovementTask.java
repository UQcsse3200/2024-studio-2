package com.csse3200.game.components.tasks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.areas.ForestGameArea;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.raycast.RaycastHit;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.utils.math.Vector2Utils;

/** Chases a target entity until they get too far away or line of sight is lost */
public class ProjectileMovementTask extends DefaultTask implements PriorityTask {
  protected final Entity target;
  protected final int priority;
  protected final PhysicsEngine physics;
  protected MovementTask movementTask;

  /**
   * @param target The entity to chase.
   * @param priority Task priority when chasing (0 when not chasing).
   */
  public ProjectileMovementTask(Entity target, int priority) {
    this.target = target;
    this.priority = priority;
    physics = ServiceLocator.getPhysicsService().getPhysics();
  }

  /**
   * Moves the entity in the direction of the entities target (often the player)
   */
  @Override
  public void start() {
    super.start();
      System.out.println("firing banana");
      // Set movementTask based on npc type
      Vector2 currentPos = owner.getEntity().getPosition();
      Vector2 targetPos = target.getPosition();
      movementTask = new MovementTask(targetPos);
      movementTask.create(owner);
      movementTask.start();

      this.owner.getEntity().getEvents().trigger("ProjectileMove");
  }
  /**
   * Updates the direction in which the entity needs to move in, checks every
   * frame to see where the player is to determine this.
   */
  @Override
  public void update() {
    movementTask.update();
    if (movementTask.getStatus() != Status.ACTIVE) {
      Gdx.app.postRunnable(owner.getEntity()::dispose);
    }
    System.out.println("firing banana");
  }

  @Override
  public int getPriority() {
    return priority;
  }
}
