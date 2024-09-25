package com.csse3200.game.minigames.maze.physics;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;

public class MazePhysicsUtils {

  // Set the collider to the base of the entity, scaled relative to the entity size.
  public static void setScaledCollider(Entity entity, float scaleX, float scaleY) {
    Vector2 boundingBox = entity.getScale().cpy().scl(scaleX, scaleY);
    entity
        .getComponent(ColliderComponent.class)
        .setAsBox(boundingBox);
  }

  // Set the collider to the base of the entity, scaled relative to the entity size.
  public static void setScaledHitBox(Entity entity, float scaleX, float scaleY) {
    Vector2 boundingBox = entity.getScale().cpy().scl(scaleX, scaleY);
    entity
            .getComponent(HitboxComponent.class)
            .setAsBox(boundingBox);
  }

  // Set the collider to the base of the entity, scaled relative to the entity size.
  public static void setScaledColliderAndHitBox(Entity entity, float scaleX, float scaleY) {
    setScaledCollider(entity, scaleX, scaleY);
    setScaledHitBox(entity, scaleX, scaleY);
  }


  private MazePhysicsUtils() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
