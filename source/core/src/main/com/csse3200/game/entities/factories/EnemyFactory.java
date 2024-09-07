package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.components.npc.ChickenAnimationController;
import com.csse3200.game.components.npc.FrogAnimationController;
import com.csse3200.game.components.npc.KangaBossAnimationController;
import com.csse3200.game.components.npc.MonkeyAnimationController;
import com.csse3200.game.components.tasks.*;
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
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.services.ServiceLocator;

/**
 * Factory to create non-playable character (NPC) entities with predefined components.
 *
 * <p>Each NPC entity type should have a creation method that returns a corresponding entity.
 * Predefined entity properties can be loaded from configs stored as json files which are defined in
 * "NPCConfigs".
 *
 * <p>If needed, this factory can be separated into more specific factories for entities with
 * similar characteristics.
 */
public class EnemyFactory {
  private static final NPCConfigs configs =
      FileLoader.readClass(NPCConfigs.class, "configs/enemyNPCs.json");

  /**
   * types of enemies
   */
  private enum EnemyType {
    FROG,
    CHICKEN,
    MONKEY;
  }

  /**
   * Creates a chicken enemy.
   *
   * @param target entity to chase (player in most cases, but does not have to be)
   * @return enemy chicken entity
   */
  public static Entity createChicken(Entity target) {
    Entity chicken = createBaseEnemy(target, EnemyType.CHICKEN);
    BaseEnemyEntityConfig config = configs.chicken;

    TextureAtlas chickenAtlas = ServiceLocator.getResourceService().getAsset(config.getSpritePath(), TextureAtlas.class);

    AnimationRenderComponent animator = new AnimationRenderComponent(chickenAtlas);

    animator.addAnimation("spawn", 1.0f, Animation.PlayMode.NORMAL);
    animator.addAnimation("walk", 0.25f, Animation.PlayMode.LOOP);


    chicken
            .addComponent(animator)
            .addComponent(new CombatStatsComponent(config.health + (int)(Math.random() * 2) - 1, 0, config.baseAttack + (int)(Math.random() * 2), 0, 0, 0))
            .addComponent(new ChickenAnimationController());

    chicken.getComponent(AnimationRenderComponent.class).scaleEntity();
    chicken.getComponent(PhysicsMovementComponent.class).changeMaxSpeed(new Vector2(config.speed, config.speed));

    return chicken;
  }

  /**
   * Creates a frog enemy.
   *
   * @param target entity to chase (player in most cases, but does not have to be)
   * @return enemy frog entity
   */
  public static Entity createFrog(Entity target) {
    Entity frog = createBaseEnemy(target, EnemyType.FROG);
    BaseEnemyEntityConfig config = configs.frog;

    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset(config.getSpritePath(), TextureAtlas.class));
    animator.addAnimation("angry_float", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("float", 0.1f, Animation.PlayMode.LOOP);

    frog
            .addComponent(new CombatStatsComponent(config.health + (int)(Math.random() * 2) - 1, 0, config.baseAttack + (int)(Math.random() * 5) - 2, 0, 0, 0))
            .addComponent(animator)
            .addComponent(new FrogAnimationController());

    frog.getComponent(AnimationRenderComponent.class).scaleEntity();
    frog.getComponent(PhysicsMovementComponent.class).changeMaxSpeed(new Vector2(config.speed, config.speed));

    return frog;
  }

  /**
   * Creates a monkey enemy.
   *
   * @param target entity to chase (player in most cases, but does not have to be)
   * @return enemy monkey entity
   */
  public static Entity createMonkey(Entity target) {
    Entity monkey = createBaseEnemy(target, EnemyType.MONKEY);
    BaseEnemyEntityConfig config = configs.monkey;

    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset(config.getSpritePath(), TextureAtlas.class));
    animator.addAnimation("run_down", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("run_up", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("run_left", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("run_right", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("run_left_down", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("run_right_down", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("run_left_up", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("run_right_up", 0.1f, Animation.PlayMode.LOOP);

    monkey
            .addComponent(new CombatStatsComponent(config.health + (int)(Math.random() * 2) - 1, 0, config.baseAttack + (int)(Math.random() * 2) - 1, 0, 0, 0))
            .addComponent(animator)
            .addComponent(new MonkeyAnimationController());

    monkey.getComponent(AnimationRenderComponent.class).scaleEntity();
    monkey.getComponent(PhysicsMovementComponent.class).changeMaxSpeed(new Vector2(config.speed, config.speed));

    return monkey;
  }


  /**
   * Creates a generic Enemy with specific tasks depending on the enemy type.
   *
   * @param target the enemy target
   * @param type the enemy type
   * @return entity
   */
  private static Entity createBaseEnemy(Entity target, EnemyType type) {
    AITaskComponent aiComponent = new AITaskComponent();

    BaseEnemyEntityConfig configStats = switch (type) {
      case FROG -> configs.frog;
      case CHICKEN -> configs.chicken;
      case MONKEY -> configs.monkey;
    };

    if (type == EnemyType.MONKEY) {
      aiComponent.addTask(new SpecialWanderTask(new Vector2(configStats.speed, configStats.speed), 2f));
      aiComponent.addTask(new RunTask(target, 10, 3f));
    } else {
      aiComponent.addTask(new SpecialWanderTask(new Vector2(configStats.speed, configStats.speed), 2f));
      aiComponent.addTask(new ChaseTask(target, 10, 3f, 4f, false));
    }

    Entity npc =
        new Entity()
            .addComponent(new PhysicsComponent())
            .addComponent(new PhysicsMovementComponent())
            .addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
            .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER))
            .addComponent(aiComponent);

    PhysicsUtils.setScaledCollider(npc, 0.9f, 0.4f);
    return npc;
  }


  /**
   * Creates a Kangaroo Boss entity. This is the NPC for the final boss of the game.
   *
   * @param target entity to chase
   * @return entity
   */
  public static Entity createKangaBossEntity(Entity target) {
    Entity kangarooBoss = createBossNPC(target);
    BaseEnemyEntityConfig config = configs.kangarooBoss;

    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset("images/final_boss_kangaroo.atlas", TextureAtlas.class));
    animator.addAnimation("angry_float", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("float", 0.1f, Animation.PlayMode.LOOP);

    kangarooBoss
            .addComponent(new CombatStatsComponent(config.health, 100, 100, 100, 100, 100))
            .addComponent(animator)
            .addComponent(new KangaBossAnimationController());

    kangarooBoss.getComponent(AnimationRenderComponent.class).scaleEntity();
    kangarooBoss.scaleHeight(3.0f);

    return kangarooBoss;
  }

  /**
   * Creates a Kangaroo Boss entity for combat. This functions the same as createKangaBossEntity() however
   * there is no chase task included. This is where abilities components will be added.
   * loaded.
   *
   * @return entity
   */
  public static Entity createKangaBossCombatEntity() {
    Entity kangarooBoss = createCombatBossNPC();
    BaseEnemyEntityConfig config = configs.kangarooBoss;

    kangarooBoss
            .addComponent(new TextureRenderComponent("images/final_boss_kangaroo_idle.png"))
            .addComponent(new CombatStatsComponent(config.health, 100, 100, 100, 100, 100));

    kangarooBoss.scaleHeight(3.0f);

    return kangarooBoss;
  }

  /**
   * Creates a boss NPC to be used as a boss entity by more specific NPC creation methods.
   *
   * @return entity
   */
  public static Entity createBossNPC(Entity target) {
    AITaskComponent aiComponent =
            new AITaskComponent()
                    .addTask(new WanderTask(new Vector2(2f, 2f), 2f, true))
                    .addTask(new ChaseTask(target, 10, 6f, 8f, true));
    Entity npc =
            new Entity()
                    .addComponent(new PhysicsComponent())
                    .addComponent(new PhysicsMovementComponent())
                    .addComponent(new ColliderComponent())
                    .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                    .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER))
                    .addComponent(aiComponent);

    PhysicsUtils.setScaledCollider(npc, 0.9f, 0.4f);
    return npc;
  }

  /**
   * Creates a boss NPC to be used as a boss entity by more specific NPC creation methods.
   *
   * @return entity
   */
  public static Entity createCombatBossNPC() {
    Entity npc =
            new Entity()
                    .addComponent(new PhysicsComponent())
                    .addComponent(new PhysicsMovementComponent())
                    .addComponent(new ColliderComponent())
                    .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                    .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER));


    PhysicsUtils.setScaledCollider(npc, 0.9f, 0.4f);
    return npc;
  }
  private EnemyFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
