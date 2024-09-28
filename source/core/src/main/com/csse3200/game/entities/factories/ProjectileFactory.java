package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.ProjectileAttackComponent;
import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.components.npc.BananaAnimationController;
import com.csse3200.game.components.npc.OrbAnimationController;
import com.csse3200.game.components.tasks.ProjectileMovementTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.BaseEnemyEntityConfig;
import com.csse3200.game.entities.configs.BaseEntityConfig;
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
    
    return createBaseProjectile(target, configs.banana, 0.5f, animator, new BananaAnimationController());
  }
  
  /**
   * Creates an orb projectile entity (for eel).
   *
   * <p>This projectile will chase the specified target entity, such as a player, using predefined
   * AI tasks and animations.
   *
   * @param target The entity that the projectile will target and chase.
   * @return A new banana projectile entity.
   */
  public static Entity createElectricOrb(Entity target) {
    String path = configs.electricOrb.getSpritePath();
    TextureAtlas atlas = ServiceLocator.getResourceService().getAsset(path, TextureAtlas.class);
    
    AnimationRenderComponent animator = new AnimationRenderComponent(atlas);
    animator.addAnimation("down", 0.25f, Animation.PlayMode.LOOP);
    animator.addAnimation("left", 0.25f, Animation.PlayMode.LOOP);
    animator.addAnimation("diagonal", 0.25f, Animation.PlayMode.LOOP);
    
    return createBaseProjectile(target, configs.electricOrb, 1f, animator, new OrbAnimationController());
  }
  
  private static Entity createBaseProjectile(Entity target, BaseEnemyEntityConfig config, float scale,
                                             AnimationRenderComponent animator, Component controler) {
    Entity projectile =
        new Entity()
            .addComponent(new PhysicsComponent())
            .addComponent(new PhysicsMovementComponent())
            .addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PROJECTILE))
            .addComponent(new ProjectileAttackComponent((short)(PhysicsLayer.PLAYER + PhysicsLayer.OBSTACLE), 2));

    PhysicsUtils.setScaledCollider(projectile, 0.9f, 0.4f);
    
    AITaskComponent aiTaskComponent = new AITaskComponent();
    aiTaskComponent.addTask(new ProjectileMovementTask(target, 10));
    
    projectile.addComponent(aiTaskComponent);
    
    projectile
            .addComponent(animator)
            .addComponent(controler);
    projectile.setScale(scale, scale);
    
    projectile.getComponent(PhysicsMovementComponent.class).changeMaxSpeed(new Vector2(config.getSpeed(), config.getSpeed()));
    
    return projectile;
  }

  private ProjectileFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
