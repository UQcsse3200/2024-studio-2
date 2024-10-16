package com.csse3200.game.minigames.maze.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;

/**
 * Useful utility functions for physics in the maze mini-game.
 */
public class MazePhysicsUtils {

    private MazePhysicsUtils() {
        throw new IllegalStateException("Instantiating static util class");
    }

    /**
     * Set the collider to the base of the entity, scaled relative to the entity size.
     * @param entity the entitie to set the collider for
     * @param scaleX the x scale
     * @param scaleY the y scale
     */
    public static void setScaledOvalCollider(Entity entity, float scaleX, float scaleY) {
        // Use oval shape for player to remove ghost collisions
        PolygonShape clipped = new PolygonShape();
        Vector2[] vertices = new Vector2[8];
        Vector2 center = entity.getScale().scl(0.5f);
        Vector2 delta = center.cpy().scl(scaleX, scaleY);
        for (int i = 0; i < vertices.length; i++) {
            double angle = 2 * Math.PI / vertices.length * i;
            vertices[i] = new Vector2((float) Math.cos(angle), (float) Math.sin(angle)).scl(delta).add(center);
        }
        clipped.set(vertices);
        entity.getComponent(ColliderComponent.class).setShape(clipped);
    }

    /**
     * Set the hitbox to the base of the entity, scaled relative to the entity size.
     * @param entity the entitie to set the hitbox for
     * @param scaleX the x scale
     * @param scaleY the y scale
     */
    public static void setScaledHitBox(Entity entity, float scaleX, float scaleY) {
        Vector2 boundingBox = entity.getScale().cpy().scl(scaleX, scaleY);
        entity
                .getComponent(HitboxComponent.class)
                .setAsBox(boundingBox);
    }

    /**
     * Set the hitbox and collider to the base of the entity, scaled relative to the entity size.
     * @param entity the entitie to set the hitbox and collider for
     * @param scaleX the x scale
     * @param scaleY the y scale
     */
    public static void setScaledColliderAndHitBox(Entity entity, float scaleX, float scaleY) {
        setScaledOvalCollider(entity, scaleX, scaleY);
        setScaledHitBox(entity, scaleX, scaleY);
    }
}
