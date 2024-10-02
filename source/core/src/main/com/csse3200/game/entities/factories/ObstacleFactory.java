package com.csse3200.game.entities.factories;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;

/**
 * Factory to create obstacle entities.
 *
 * <p>Each obstacle entity type should have a creation method that returns a corresponding entity.
 */
public class ObstacleFactory {

  /**
   * Creates a tree entity.
   * @return entity
   */
  public static Entity createTree() {
    Entity tree =
        new Entity()
            .addComponent(new TextureRenderComponent("images/tree.png"))
            .addComponent(new PhysicsComponent())
            .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

    tree.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    tree.getComponent(TextureRenderComponent.class).scaleEntity();
    tree.scaleHeight(2.5f);
    PhysicsUtils.setScaledCollider(tree, 0.5f, 0.2f);
    return tree;
  }

  /**
   * Creates a cloud entity.
   * @return entity
   */
  public static Entity createCloud() {
    Entity cloud =
            new Entity()
                    .addComponent(new TextureRenderComponent("images/cloud.png"))
                    .addComponent(new PhysicsComponent())
                    .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

    cloud.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    cloud.getComponent(TextureRenderComponent.class).scaleEntity();
    cloud.scaleHeight(2.5f);
    PhysicsUtils.setScaledCollider(cloud, 0.5f, 0.2f);
    return cloud;
  }

  /**
   * Creates a seaweed entity.
   * @return entity
   */
  public static Entity createSeaweed() {
    Entity seaweed =
            new Entity()
                    .addComponent(new TextureRenderComponent("images/seaweed.png"))
                    .addComponent(new PhysicsComponent())
                    .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

    seaweed.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    seaweed.getComponent(TextureRenderComponent.class).scaleEntity();
    seaweed.scaleHeight(2.5f);
    PhysicsUtils.setScaledCollider(seaweed, 0.5f, 0.2f);
    return seaweed;
  }

  /**
   * Creates a starfish entity.
   * @return entity
   */
  public static Entity createStarfish() {
    Entity starfish =
            new Entity()
                    .addComponent(new TextureRenderComponent("images/starfish.png"))
                    .addComponent(new PhysicsComponent())
                    .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

    starfish.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    starfish.getComponent(TextureRenderComponent.class).scaleEntity();
    starfish.scaleHeight(2.5f);
    PhysicsUtils.setScaledCollider(starfish, 0.5f, 0.2f);
    return starfish;
  }

  /**
   * Creates an invisible physics wall.
   * @param width Wall width in world units
   * @param height Wall height in world units
   * @return Wall entity of given width and height
   */
  public static Entity createWall(float width, float height) {
    Entity wall = new Entity()
        .addComponent(new PhysicsComponent().setBodyType(BodyType.StaticBody))
        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));
    wall.setScale(width, height);
    return wall;
  }

  private ObstacleFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }

  /**
   * Create a visible physics wall.
   * @param width Wall width in world units
   * @param height Wall height in world units
   * @return Wall entity of given width and height
   */
  public static Entity createVisibleWall(float width, float height) {
    Entity wall = new Entity()
        .addComponent(new TextureRenderComponent("images/water_tile_2_around_grass/middle_water_2_around_grass.jpg"))
        .addComponent(new PhysicsComponent().setBodyType(BodyType.StaticBody))
        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));
    wall.setScale(width, height);
    return wall;
  }
}
