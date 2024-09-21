package com.csse3200.game.minigames.maze.entities.factories;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.minigames.maze.components.npc.MazeEntityAnimationController;
import com.csse3200.game.components.tasks.ChaseTask;
import com.csse3200.game.components.tasks.WanderTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.lighting.components.LightingComponent;
import com.csse3200.game.minigames.maze.components.MazeCombatStatsComponent;
import com.csse3200.game.minigames.maze.components.MazeTouchAttackComponent;
import com.csse3200.game.minigames.maze.components.tasks.MazeChaseTask;
import com.csse3200.game.minigames.maze.entities.configs.MazeEntityConfig;
import com.csse3200.game.minigames.maze.entities.configs.MazeNPCConfigs;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.rendering.FaceMoveDirectionXComponent;
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
public class MazeNPCFactory {
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
    MazeEntityConfig config = configs.angler;

    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService()
                            .getAsset("images/minigames/Angler.atlas", TextureAtlas.class));
    animator.addAnimation("Walk", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("Attack", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("Idle", 0.2f, Animation.PlayMode.LOOP);

    angler
            .addComponent(new MazeCombatStatsComponent(config.health, config.baseAttack))
            .addComponent(animator)
            .addComponent(new MazeEntityAnimationController())
            .addComponent(new LightingComponent()
                    .attach(LightingComponent.createPointLight(3f, Color.GREEN)));

    angler.getComponent(AnimationRenderComponent.class).scaleEntity();
    angler.setScale(.5f,.5f);
    PhysicsUtils.setScaledCollider(angler, .4f, .4f);
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
            .addTask(new MazeChaseTask(target, 10, 2f, 3f));
    Entity npc =
        new Entity()
            .addComponent(new PhysicsComponent())
            .addComponent(new PhysicsMovementComponent())
            .addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
            .addComponent(new MazeTouchAttackComponent(PhysicsLayer.PLAYER, 1.5f))
            .addComponent(new FaceMoveDirectionXComponent())
            .addComponent(aiComponent);

    PhysicsUtils.setScaledCollider(npc, 0.9f, 0.4f);
    return npc;
  }

  private MazeNPCFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
