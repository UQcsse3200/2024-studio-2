package com.csse3200.game.physics.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.csse3200.game.ai.movement.MovementController;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.utils.math.Vector2Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Movement controller for a physics-based entity. */
public class PhysicsMovementComponent extends Component implements MovementController {
  private static final Logger logger = LoggerFactory.getLogger(PhysicsMovementComponent.class);
  private static final Vector2 maxSpeed = Vector2Utils.ONE;
  /**
   * Variable to set boss speed faster than player by a scale of 0.5.
   */
  private static final Vector2 maxBossSpeed = new Vector2(3.5f, 3.5f);
  private PhysicsComponent physicsComponent;
  private Vector2 targetPosition;
  private boolean movementEnabled = true;

  @Override
  public void create() {
    physicsComponent = entity.getComponent(PhysicsComponent.class);
  }

  @Override
  public void update() {
    if (movementEnabled && targetPosition != null) {
      Body body = physicsComponent.getBody();

      // 15 is the id of the boss entity. This will be changed.
      // I believe it will be necessary to make an entire boss
      // component and set up the boss npc in a similar fashion to
      // the player and use something like the PlayerAction class
      // to alter movement and scale.

      // also only works on startup as this is when
      // boss entity id is 15
      if (entity.getId() == 15) {
        updateBossDirection(body);
      } else {
        updateDirection(body);
      }
    }
  }

  /**
   * Enable/disable movement for the controller. Disabling will immediately set velocity to 0.
   *
   * @param movementEnabled true to enable movement, false otherwise
   */
  @Override
  public void setMoving(boolean movementEnabled) {
    this.movementEnabled = movementEnabled;
    if (!movementEnabled) {
      Body body = physicsComponent.getBody();
      setToVelocity(body, Vector2.Zero);
    }
  }

  @Override
  public boolean getMoving() {
    return movementEnabled;
  }

  /** @return Target position in the world */
  @Override
  public Vector2 getTarget() {
    return targetPosition;
  }

  /**
   * Set a target to move towards. The entity will be steered towards it in a straight line, not
   * using pathfinding or avoiding other entities.
   *
   * @param target target position
   */
  @Override
  public void setTarget(Vector2 target) {
    logger.trace("Setting target to {}", target);
    this.targetPosition = target;
  }

  private void updateDirection(Body body) {
    Vector2 desiredVelocity = getDirection().scl(maxSpeed);
    setToVelocity(body, desiredVelocity);
  }

  /**
   * Temporary class to update the boss speed. Will be removed
   * as this class likely shouldn't be touched.
   * @param body The entity body
   */
  private void updateBossDirection(Body body) {
    System.out.println("updating boss speed");
    Vector2 desiredVelocity = getDirection().scl(maxBossSpeed);
    setToVelocity(body, desiredVelocity);
  }



  private void setToVelocity(Body body, Vector2 desiredVelocity) {
    // impulse force = (desired velocity - current velocity) * mass
    Vector2 velocity = body.getLinearVelocity();
    Vector2 impulse = desiredVelocity.cpy().sub(velocity).scl(body.getMass());
    body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
  }

  private Vector2 getDirection() {
    // Move towards targetPosition based on our current position
    return targetPosition.cpy().sub(entity.getPosition()).nor();
  }
}
