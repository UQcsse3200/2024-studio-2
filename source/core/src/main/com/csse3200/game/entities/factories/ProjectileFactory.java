package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.ProjectileAttackComponent;
import com.csse3200.game.components.npc.BananaAnimationController;
import com.csse3200.game.components.npc.WaterSpiralAnimationController;
import com.csse3200.game.components.npc.WindGustAnimationController;
import com.csse3200.game.components.tasks.ProjectileMovementTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.BaseEnemyEntityConfig;
import com.csse3200.game.entities.configs.NPCConfigs;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;

/**
 * Factory to create non-playable projectile entities with predefined components.
 *
 * <p>Each projectile entity type has a creation method that returns a corresponding entity.
 * Predefined entity properties can be loaded from configurations stored as JSON files defined in
 * "NPCConfigs". This factory can be expanded or separated into more specific factories for entities
 * with similar characteristics.
 */
public class ProjectileFactory {

  private static final NPCConfigs configs =
          FileLoader.readClass(NPCConfigs.class, "configs/enemyNPCs.json");

  /**
   * Creates a banana projectile entity.
   *
   * <p>This projectile will chase the specified target entity, such as a player, using predefined
   * AI tasks and animations.
   *
   * @param target The entity that the projectile will target and chase.
   * @return A new banana projectile entity.
   */
  public static Entity createBanana(Entity target) {
    Entity banana = createBaseProjectile(target);
    BaseEnemyEntityConfig config = configs.banana;

    AITaskComponent aiTaskComponent = new AITaskComponent();
    aiTaskComponent.addTask(new ProjectileMovementTask(target, 10));

    banana.addComponent(aiTaskComponent);

    TextureAtlas bananaAtlas = ServiceLocator.getResourceService().getAsset("images/banana.atlas", TextureAtlas.class);

    AnimationRenderComponent animator = new AnimationRenderComponent(bananaAtlas);
    animator.addAnimation("fire", 0.25f, Animation.PlayMode.LOOP);
    
    banana
            .addComponent(animator)
            .addComponent(new BananaAnimationController());
    banana.setScale(0.5f, 0.5f);
    
    banana.getComponent(PhysicsMovementComponent.class).changeMaxSpeed(new Vector2(config.getSpeed(), config.getSpeed()));

    return banana;
  }

  public static Entity createWaterSpiral(Entity target) {
    Entity waterSpiral = createBaseProjectile(target);
    BaseEnemyEntityConfig config = configs.waterSpiral;

    AITaskComponent aiTaskComponent = new AITaskComponent();
    aiTaskComponent.addTask(new ProjectileMovementTask(target, 10));

    waterSpiral.addComponent(aiTaskComponent);

    TextureAtlas waterSpiralAtlas = ServiceLocator.getResourceService().getAsset(config.getSpritePath(), TextureAtlas.class);

    AnimationRenderComponent animator = new AnimationRenderComponent(waterSpiralAtlas);
    animator.addAnimation("waterSpiral", 0.1f, Animation.PlayMode.LOOP);

    waterSpiral
            .addComponent(animator)
            .addComponent(new WaterSpiralAnimationController());
    waterSpiral.setScale(3.0f, 3.0f);

    waterSpiral.getComponent(PhysicsMovementComponent.class).changeMaxSpeed(new Vector2(config.getSpeed(), config.getSpeed()));

    return waterSpiral;
  }

  public static Entity createWindGust(Entity target) {
    Entity windGust = createBaseProjectile(target);
    BaseEnemyEntityConfig config = configs.windGust;

    AITaskComponent aiTaskComponent = new AITaskComponent();
    aiTaskComponent.addTask(new ProjectileMovementTask(target, 10));

    windGust.addComponent(aiTaskComponent);

    TextureAtlas windGustAtlas = ServiceLocator.getResourceService().getAsset(config.getSpritePath(), TextureAtlas.class);

    AnimationRenderComponent animator = new AnimationRenderComponent(windGustAtlas);
    animator.addAnimation("windGust", 0.1f, Animation.PlayMode.LOOP);

    windGust
            .addComponent(animator)
            .addComponent(new WindGustAnimationController());
    windGust.setScale(5.0f, 5.0f);

    windGust.getComponent(PhysicsMovementComponent.class).changeMaxSpeed(new Vector2(config.getSpeed(), config.getSpeed()));

    return windGust;
  }

  /**
   * Creates a base projectile entity with common components and sets the target layer for
   * the projectile's attacks.
   *
   * @param target The entity that the projectile will target.
   * @return A new projectile entity with basic components.
   */
  private static Entity createBaseProjectile(Entity target) {
    Entity projectile =
        new Entity()
            .addComponent(new PhysicsComponent())
            .addComponent(new PhysicsMovementComponent())
            .addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PROJECTILE))
            .addComponent(new ProjectileAttackComponent((short)(PhysicsLayer.PLAYER + PhysicsLayer.OBSTACLE)));

    PhysicsUtils.setScaledCollider(projectile, 0.9f, 0.4f);
    return projectile;
  }

  private ProjectileFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
