package com.csse3200.game.components.tasks;

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
import com.csse3200.game.rendering.AnimationRenderComponent;

/** Chases a target entity until they get too far away or line of sight is lost */
public class ChaseTask extends DefaultTask implements PriorityTask {
  protected final Entity target;
  protected final int priority;
  protected final float viewDistance;
  protected final float maxChaseDistance;
  protected final PhysicsEngine physics;
  protected final DebugRenderer debugRenderer;
  protected final RaycastHit hit = new RaycastHit();
  protected MovementTask movementTask;
  private Music heartbeatSound;
  private final boolean isBoss;
  private static final String heartbeat = "sounds/heartbeat.mp3";
  private final Vector2 bossSpeed;

  /**
   * @param target The entity to chase.
   * @param priority Task priority when chasing (0 when not chasing).
   * @param viewDistance Maximum distance from the entity at which chasing can start.
   * @param maxChaseDistance Maximum distance from the entity while chasing before giving up.
   */
  public ChaseTask(Entity target, int priority, float viewDistance, float maxChaseDistance, boolean isBoss) {
    this.target = target;
    this.priority = priority;
    this.viewDistance = viewDistance;
    this.maxChaseDistance = maxChaseDistance;
    physics = ServiceLocator.getPhysicsService().getPhysics();
    debugRenderer = ServiceLocator.getRenderService().getDebug();
    bossSpeed = Vector2Utils.TWOHALF;
    this.isBoss = isBoss;
  }

  public boolean isBoss() {
    return isBoss;
  }

  /**
   * Moves the entity in the direction of the entities target (often the player)
   */
  @Override
  public void start() {
    super.start();

    String event = this.isBoss ? "kangaChaseStart" : "chaseStart";

      // Set movementTask based on npc type
      Vector2 currentPos = owner.getEntity().getPosition();
      Vector2 targetPos = target.getPosition();
      movementTask = this.isBoss ? new MovementTask(target.getPosition(), bossSpeed) :
              new MovementTask(targetPos);
      movementTask.create(owner);
      movementTask.start();

      this.owner.getEntity().getEvents().trigger(event);

      if (this.isBoss) {
          playTensionSound();
          this.target.getEvents().trigger("startHealthBarBeating");
      }
      if (targetPos.x - currentPos.x < 0 && !this.isBoss) {
          this.owner.getEntity().getEvents().trigger("chaseLeft");
      } else {
          this.owner.getEntity().getEvents().trigger("chaseRight");

    }

      this.owner.getEntity().getEvents().trigger("chaseStart");
  }

    void playTensionSound() {
        if (heartbeatSound == null && ServiceLocator.getResourceService() != null) {
      heartbeatSound = ServiceLocator.getResourceService().getAsset(heartbeat, Music.class);
      heartbeatSound.setLooping(true);
      heartbeatSound.setVolume(1.0f);
    }
    if (heartbeatSound != null) {
      ForestGameArea.pauseMusic();
      heartbeatSound.play();
    }
  }

  void stopTensionSound() {
    if (heartbeatSound != null) {
      ForestGameArea.playMusic();
      heartbeatSound.stop();
    }
  }

  /**
   * Updates the direction in which the entity needs to move in, checks every
   * frame to see where the player is to determine this.
   */
  @Override
  public void update() {
    Vector2 currentPos = owner.getEntity().getPosition();
    Vector2 targetPos = target.getPosition();

    movementTask.setTarget(targetPos);
    movementTask.update();
    if (movementTask.getStatus() != Status.ACTIVE) {
      movementTask.start();
    }

    if (targetPos.x - currentPos.x < 0) {
      this.owner.getEntity().getEvents().trigger("chaseLeft");
    } else {
      this.owner.getEntity().getEvents().trigger("chaseRight");
    }

//    owner.getEntity().getComponent(AnimationRenderComponent.class).startAnimation("alert");

  }

  public float getViewDistance() {
    return this.viewDistance;
  }

  @Override
  public void stop() {
    super.stop();
    movementTask.stop();

    if (this.isBoss) {
        stopTensionSound();
        this.target.getEvents().trigger("stopHealthBarBeating");
    }
  }

  @Override
  public int getPriority() {
    if (status == Status.ACTIVE) {
      return getActivePriority();
    }

    return getInactivePriority();
  }

  protected float getDistanceToTarget() {
    return owner.getEntity().getPosition().dst(target.getPosition());
  }

  protected int getActivePriority() {
    float dst = getDistanceToTarget();
    if (dst > maxChaseDistance || !isTargetVisible()) {
      return -1; // Too far, stop chasing
    }
    return priority;
  }

  protected int getInactivePriority() {
    float dst = getDistanceToTarget();
    if (dst < viewDistance && isTargetVisible()) {
      return priority;
    }
    return -1;
  }

  protected boolean isTargetVisible() {
    Vector2 from = owner.getEntity().getCenterPosition();
    Vector2 to = target.getCenterPosition();

    // If there is an obstacle in the path to the player, not visible.
    if (physics.raycast(from, to, PhysicsLayer.OBSTACLE, hit)) {
      debugRenderer.drawLine(from, hit.point);
      return false;
    }
    debugRenderer.drawLine(from, to);
    return true;
  }
}
