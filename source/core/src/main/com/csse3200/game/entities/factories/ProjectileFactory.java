package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.ProjectileAttackComponent;
import com.csse3200.game.components.npc.*;
import com.csse3200.game.components.tasks.HiveTask;
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

import java.util.List;

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
   * Creates a banana projectile entity (for monkey).
   *
   * <p>This projectile will chase the specified target entity, such as a player, using predefined
   * AI tasks and animations.
   *
   * @param target The entity that the projectile will target and chase.
   * @return A new banana projectile entity.
   */
  public static Entity createBanana(Entity target) {
    String path = configs.banana.getSpritePath();
    TextureAtlas atlas = ServiceLocator.getResourceService().getAsset(path, TextureAtlas.class);
    
    AnimationRenderComponent animator = new AnimationRenderComponent(atlas);
    animator.addAnimation("fire", 0.25f, Animation.PlayMode.LOOP);
    
    Entity banana = createBaseProjectile(target, configs.banana, 0.5f, animator, new BananaAnimationController());
    banana.setEnemyType(Entity.EnemyType.BANANA);
    return banana;
  }

  public static Entity createWaterSpiral(Entity target) {
    BaseEnemyEntityConfig config = configs.waterSpiral;
    
    TextureAtlas waterSpiralAtlas = ServiceLocator.getResourceService().getAsset(config.getSpritePath(), TextureAtlas.class);
    AnimationRenderComponent animator = new AnimationRenderComponent(waterSpiralAtlas);
    animator.addAnimation("waterSpiral", 0.1f, Animation.PlayMode.LOOP);
    
    Entity waterSpiral = createBaseProjectile(target, config, 3f, animator, new WaterSpiralAnimationController());

    AITaskComponent aiTaskComponent = new AITaskComponent();
    aiTaskComponent.addTask(new ProjectileMovementTask(target, 10));

    waterSpiral.addComponent(aiTaskComponent);
    
    waterSpiral.getComponent(PhysicsMovementComponent.class).changeMaxSpeed(new Vector2(config.getSpeed(), config.getSpeed()));

    return waterSpiral;
  }

  public static Entity createWindGust(Entity target) {
    BaseEnemyEntityConfig config = configs.windGust;
    
    TextureAtlas windGustAtlas = ServiceLocator.getResourceService().getAsset(config.getSpritePath(), TextureAtlas.class);
    AnimationRenderComponent animator = new AnimationRenderComponent(windGustAtlas);
    animator.addAnimation("windGust", 0.1f, Animation.PlayMode.LOOP);
    
    Entity windGust = createBaseProjectile(target, config, 5f, animator, new WindGustAnimationController());

    AITaskComponent aiTaskComponent = new AITaskComponent();
    aiTaskComponent.addTask(new ProjectileMovementTask(target, 10));

    windGust.addComponent(aiTaskComponent);

    windGust.getComponent(PhysicsMovementComponent.class).changeMaxSpeed(new Vector2(config.getSpeed(), config.getSpeed()));

    return windGust;
  }
  
  /**
   * Creates an orb projectile entity (for eel).
   *
   * <p>This projectile will chase the specified target entity, such as a player, using predefined
   * AI tasks and animations.
   *
   * @param target The entity that the projectile will target and chase.
   * @return A new orb projectile entity.
   */
  public static Entity createElectricOrb(Entity target) {
    String path = configs.electricOrb.getSpritePath();
    TextureAtlas atlas = ServiceLocator.getResourceService().getAsset(path, TextureAtlas.class);
    
    AnimationRenderComponent animator = new AnimationRenderComponent(atlas);
    animator.addAnimation("down", 0.25f, Animation.PlayMode.LOOP);
    animator.addAnimation("left", 0.25f, Animation.PlayMode.LOOP);
    animator.addAnimation("diagonal", 0.25f, Animation.PlayMode.LOOP);
    
    Entity orb = createBaseProjectile(target, configs.electricOrb, 1f, animator, new OrbAnimationController());
    orb.setEnemyType(Entity.EnemyType.ELECTRICORB);
    return orb;
  }

  /**
   * Creates an worm projectile entity (for eel).
   *
   * <p>This projectile will chase the specified target entity, such as a player, using predefined
   * AI tasks and animations.
   *
   * @param target The entity that the projectile will target and chase.
   * @return A new worm projectile entity.
   */
  public static Entity createWorm(Entity target) {
    String path = configs.worm.getSpritePath();
    TextureAtlas atlas = ServiceLocator.getResourceService().getAsset(path, TextureAtlas.class);

    AnimationRenderComponent animator = new AnimationRenderComponent(atlas);
    animator.addAnimation("fire", 0.25f, Animation.PlayMode.LOOP);

    Entity worm = createBaseProjectile(target, configs.worm, 0.5f, animator, new BananaAnimationController());
    worm.setEnemyType(Entity.EnemyType.WORM);
    return worm;
  }

  /**
   * Creates a hive enemy.
   *
   * @param target entity which the spawned bees will chase
   * @return enemy hive entity
   */
  public static Entity createHive(Entity target, List<Entity> enemies) {
    BaseEnemyEntityConfig config = configs.hive;

    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset(config.getSpritePath(), TextureAtlas.class));
    animator.addAnimation("float", 0.1f, Animation.PlayMode.LOOP);

    Entity hive = createBaseProjectile(target, config, 2f, animator, new HiveAnimationController());

    hive.getComponent(AITaskComponent.class).addTask(new HiveTask(target));
    hive.setEnemyType(Entity.EnemyType.HIVE);

    hive.setEnemies(enemies);
    
    return hive;
  }
  
  /**
   *
   * @param target Entity
   * @param config BaseEnemyEntityConfig
   * @param scale float
   * @param animator AnimationRenderComponent
   * @param controller Component (animation controller
   * @return Projectile Entity
   */
  private static Entity createBaseProjectile(Entity target, BaseEnemyEntityConfig config, float scale,
                                             AnimationRenderComponent animator, Component controller) {
    Entity projectile =
        new Entity()
            .addComponent(new PhysicsComponent())
            .addComponent(new PhysicsMovementComponent())
            .addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PROJECTILE))
            .addComponent(new ProjectileAttackComponent((short)(PhysicsLayer.PLAYER + PhysicsLayer.OBSTACLE), config.getBaseAttack(), target));

    PhysicsUtils.setScaledCollider(projectile, 0.9f, 0.4f);
    
    AITaskComponent aiTaskComponent = new AITaskComponent();
    aiTaskComponent.addTask(new ProjectileMovementTask(target, 10));
    
    projectile.addComponent(aiTaskComponent);
    
    projectile
            .addComponent(animator)
            .addComponent(controller);
    projectile.setScale(scale, scale);
    
    projectile.getComponent(PhysicsMovementComponent.class).changeMaxSpeed(new Vector2(config.getSpeed(), config.getSpeed()));
    
    return projectile;
  }

  private ProjectileFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
