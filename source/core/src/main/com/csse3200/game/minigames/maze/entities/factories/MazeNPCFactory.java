package com.csse3200.game.minigames.maze.entities.factories;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.minigames.maze.components.npc.MazeEntityAnimationController;
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
import com.csse3200.game.rendering.AnimationRenderWithAudioComponent;
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
    Entity angler = createBaseNPC();

    // Add in the ai component
    AITaskComponent aiComponent =
            new AITaskComponent()
                    .addTask(new WanderTask(new Vector2(2f, 2f), 2f, false))
                    .addTask(new MazeChaseTask(target, 10, 2f, 3f));

    MazeEntityConfig config = configs.angler;

    AnimationRenderWithAudioComponent animator =
            new AnimationRenderWithAudioComponent(
                    ServiceLocator.getResourceService()
                            .getAsset("images/minigames/Angler.atlas", TextureAtlas.class));
    animator.addAnimation("Walk", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("Attack", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("Idle", 0.2f, Animation.PlayMode.LOOP);

    animator.addSound("sounds/minigames/angler-chomp.mp3", "Attack", 4);

    angler
            .addComponent(new MazeCombatStatsComponent(config.health, config.baseAttack))
            .addComponent(animator)
            .addComponent(new MazeEntityAnimationController())
            .addComponent(new LightingComponent()
                    .attach(LightingComponent.createPointLight(3f, Color.GREEN)))
            .addComponent(aiComponent);

    angler.getComponent(AnimationRenderWithAudioComponent.class).scaleEntity();
    angler.setScale(.2f,.2f);
    PhysicsUtils.setScaledCollider(angler, 1f, 1f);
    return angler;
  }

  /**
   * Creates a jellyfish entity.
   * Jellyfish do not actively seek the player, they only wander around.
   * @return the jellyfish entity
   */
  public static Entity createJellyfish() {
    Entity jellyfish = createBaseNPC();
    MazeEntityConfig config = configs.jellyfish;

    // Jellyfish only has Wander task
    AITaskComponent aiComponent =
            new AITaskComponent()
                    .addTask(new WanderTask(new Vector2(2f, 2f), 2f, false));


    // Add in jellyfish atlas files
    AnimationRenderWithAudioComponent animator =
            new AnimationRenderWithAudioComponent(
                    ServiceLocator.getResourceService()
                            .getAsset("images/minigames/Angler.atlas", TextureAtlas.class));

    animator.addAnimation("Walk", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("Attack", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("Idle", 0.2f, Animation.PlayMode.LOOP);

    // Change sounds to jellyfish if there are any
    animator.addSound("sounds/minigames/angler-chomp.mp3", "Attack", 4);

    jellyfish
            .addComponent(new MazeCombatStatsComponent(config.health, config.baseAttack))
            .addComponent(animator)
            .addComponent(new MazeEntityAnimationController())
            .addComponent(new LightingComponent()
                    .attach(LightingComponent.createPointLight(3f, Color.GREEN)))
            .addComponent(aiComponent);

    // Will need to change these based off jellyfish sprite
    jellyfish.getComponent(AnimationRenderWithAudioComponent.class).scaleEntity();
    jellyfish.setScale(.2f,.2f);
    PhysicsUtils.setScaledCollider(jellyfish, 1f, 1f);
    return jellyfish;
  }

  /**
   * Creates a generic NPC to be used as a base entity by more specific NPC creation methods.
   *
   * @return entity
   */
  private static Entity createBaseNPC() {

    Entity npc =
        new Entity()
            .addComponent(new PhysicsComponent())
            .addComponent(new PhysicsMovementComponent())
            .addComponent(new ColliderComponent().setGroupIndex((short)-1)) // NPC's don't collide with each other
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
            .addComponent(new MazeTouchAttackComponent(PhysicsLayer.PLAYER, .8f))
            .addComponent(new FaceMoveDirectionXComponent());

    PhysicsUtils.setScaledCollider(npc, 0.9f, 0.4f);
    npc.getComponent(ColliderComponent.class).setDensity(1.5f);

    return npc;
  }

  private MazeNPCFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
