package com.csse3200.game.components.tasks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.areas.ForestGameArea;
import com.csse3200.game.components.ProjectileAttackComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.raycast.RaycastHit;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.utils.math.Vector2Utils;

/** Chases a target entity until they get too far away or line of sight is lost */
public class ProjectileMovementTask extends DefaultTask implements PriorityTask {
  protected final int priority;
  protected final PhysicsEngine physics;
  public MovementTask movementTask;
  private Vector2 targetPosition;

  /**
   * @param target The entity to chase.
   * @param priority Task priority when chasing (0 when not chasing).
   */
  public ProjectileMovementTask(Entity target, int priority) {
    this.targetPosition = target.getPosition();
    this.priority = priority;
    physics = ServiceLocator.getPhysicsService().getPhysics();
  }

  /**
   * Moves the entity in the direction of the entities target (often the player)
   */
  @Override
  public void start() {
    super.start();
      movementTask = new MovementTask(targetPosition);
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
    if (movementTask.getStatus() != Status.ACTIVE) {
      owner.getEntity().setEnabled(false);
      AnimationRenderComponent animationRenderComponent = owner.getEntity().getComponent(AnimationRenderComponent.class);
      animationRenderComponent.stopAnimation();
      owner.getEntity().specialDispose();
    }
    movementTask.update();
  }

  @Override
  public int getPriority() {
    return priority;
  }
}
