package com.csse3200.game.minigames.maze.entities.factories;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.npc.GhostAnimationController;
import com.csse3200.game.components.npc.MazeEntityAnimationController;
import com.csse3200.game.components.tasks.ChaseTask;
import com.csse3200.game.components.tasks.WanderTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.GhostKingConfig;
import com.csse3200.game.entities.configs.NPCConfigs;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.lighting.components.LightingComponent;
import com.csse3200.game.minigames.maze.components.MazeTouchAttackComponent;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
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
public class MazeEnemyFactory {
  private static final MazeNPCConfigs configs =
      FileLoader.readClass(MazeNPCConfigs.class, "configs/minigames/maze/NPCs.json");

  /**
   * Creates an angler entity.
   *
   * @param target entity to chase
   * @return entity
   */
  public static Entity createAngler(Entity target) {
    Entity angler = createBaseNPC(target);
    GhostKingConfig config = configs.angler;

    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService()
                            .getAsset("images/minigames/Angler.atlas", TextureAtlas.class));
    animator.addAnimation("Idle", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("Attack", 0.2f, Animation.PlayMode.LOOP);

    angler
            .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
            .addComponent(animator)
            .addComponent(new MazeEntityAnimationController())
            .addComponent(new LightingComponent(LightingComponent.createPointLight(0.5f, new Color(0.7f, 0.7f, 0.7f, 0.7f))));

    angler.getComponent(AnimationRenderComponent.class).scaleEntity();
    angler.setScale(.1f,.1f);
    PhysicsUtils.setScaledCollider(angler, 0.1f, 0.1f);
    return angler;
  }

  /**
   * Creates a generic NPC to be used as a base entity by more specific NPC creation methods.
   *
   * @return entity
   */
  private static Entity createBaseNPC(Entity target) {
    AITaskComponent aiComponent =
        new AITaskComponent()
            .addTask(new WanderTask(new Vector2(2f, 2f), 2f, false))
            .addTask(new ChaseTask(target, 10, 3f, 4f, false));
    Entity npc =
        new Entity()
            .addComponent(new PhysicsComponent())
            .addComponent(new PhysicsMovementComponent())
            .addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
            .addComponent(new MazeTouchAttackComponent(PhysicsLayer.PLAYER, 1.5f))
            .addComponent(aiComponent);

    PhysicsUtils.setScaledCollider(npc, 0.9f, 0.4f);
    return npc;
  }

  private MazeEnemyFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
