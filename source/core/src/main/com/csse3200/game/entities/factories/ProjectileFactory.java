package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.components.npc.BananaAnimationController;
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
 * Factory to create non-playable projectile entity with predefined components.
 *
 * <p>Each Projectile entity type should have a creation method that returns a corresponding entity.
 * Predefined entity properties can be loaded from configs stored as json files which are defined in
 * "NPCConfigs".
 *
 * <p>If needed, this factory can be separated into more specific factories for entities with
 * similar characteristics.
 */
public class ProjectileFactory {

  private static final NPCConfigs configs =
          FileLoader.readClass(NPCConfigs.class, "configs/enemyNPCs.json");

  /**
   * types of projectiles
   */

  /**
   * Creates a banana projectile.
   *
   * @param target entity to chase (player in most cases, but does not have to be)
   * @return enemy chicken entity
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
            .addComponent(new CombatStatsComponent(config.health + (int)(Math.random() * 2), 0, config.baseAttack, 0, config.speed, 0))
            .addComponent(new BananaAnimationController());

    banana.getComponent(AnimationRenderComponent.class).scaleEntity();
    banana.getComponent(PhysicsMovementComponent.class).changeMaxSpeed(new Vector2(config.speed, config.speed));

    return banana;
  }


  /**
   * Creates a generic Enemy with specific tasks depending on the enemy type.
   *
   * @param target the enemy target
   * @return entity
   */
  private static Entity createBaseProjectile(Entity target) {
    Entity projectile =
        new Entity()
            .addComponent(new PhysicsComponent())
            .addComponent(new PhysicsMovementComponent())
            .addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PROJECTILE))
            .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER));

    PhysicsUtils.setScaledCollider(projectile, 0.9f, 0.4f);
    return projectile;
  }

  private ProjectileFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
