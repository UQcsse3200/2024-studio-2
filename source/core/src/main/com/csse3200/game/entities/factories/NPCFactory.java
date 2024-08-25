package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.npc.GhostAnimationController;
import com.csse3200.game.components.npc.FriendlyNPCAnimationController;
import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.components.tasks.ChaseTask;
import com.csse3200.game.components.tasks.WanderTask;
import com.csse3200.game.components.tasks.PauseTask;
import com.csse3200.game.components.tasks.AvoidTask;
import com.csse3200.game.components.ConfigComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.*;
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
import com.csse3200.game.entities.EntityChatService;

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
public class NPCFactory {
  private static final NPCConfigs configs =
      FileLoader.readClass(NPCConfigs.class, "configs/NPCs.json");

  /**
   * Creates a ghost entity.
   *
   * @param target entity to chase
   * @return entity
   */
  public static Entity createGhost(Entity target) {
    Entity ghost = createBaseNPC(target);
    BaseEntityConfig config = configs.ghost;

    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset("images/ghost.atlas", TextureAtlas.class));
    animator.addAnimation("angry_float", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("float", 0.1f, Animation.PlayMode.LOOP);

    ghost
        .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
        .addComponent(animator)
        .addComponent(new GhostAnimationController());

    ghost.getComponent(AnimationRenderComponent.class).scaleEntity();

    return ghost;
  }

  /**
   * Creates a ghost king entity.
   *
   * @param target entity to chase
   * @return entity
   */
  public static Entity createGhostKing(Entity target) {
    Entity ghostKing = createBaseNPC(target);
    GhostKingConfig config = configs.ghostKing;

    AnimationRenderComponent animator =
        new AnimationRenderComponent(
            ServiceLocator.getResourceService()
                .getAsset("images/ghostKing.atlas", TextureAtlas.class));
    animator.addAnimation("float", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("angry_float", 0.1f, Animation.PlayMode.LOOP);

    ghostKing
        .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
        .addComponent(animator)
        .addComponent(new GhostAnimationController());

    ghostKing.getComponent(AnimationRenderComponent.class).scaleEntity();
    return ghostKing;
  }


  /**
   * Base method to create a friendly NPC.
   *
   * @param target   entity to move towards when in range.
   * @param enemies  list of enemy entities.
   * @param atlasPath path to the texture atlas for the NPC.
   * @param animationSpeed speed of the animation.
   * @param config  the specific configuration object.
   * @return entity
   */
  private static Entity createFriendlyNPC(Entity target, List<Entity> enemies, String atlasPath, float animationSpeed, BaseEntityConfig config) {
    Entity npc = createFriendlyBaseNPC(target, enemies);

    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset(atlasPath, TextureAtlas.class));
    animator.addAnimation("float", animationSpeed, Animation.PlayMode.LOOP);

    npc.addComponent(new CombatStatsComponent(config.getHealth(), config.getBaseAttack()))
            .addComponent(animator)
            .addComponent(new FriendlyNPCAnimationController())
            .addComponent(new ConfigComponent<>(config));

    npc.getComponent(AnimationRenderComponent.class).scaleEntity();

    // Add Sounds Effect to FNPCs
    String[] animalSoundPaths = config.getSoundPath();
    if (animalSoundPaths != null && animalSoundPaths.length > 0) {
      String eventPausedStart = String.format("PauseStart%s", config.getAnimalName());
      String eventPausedEnd = String.format("PauseEnd%s", config.getAnimalName());
      npc.getEvents().addListener(eventPausedStart, (String[] hintText) -> initiateDialogue(animalSoundPaths, hintText));
      npc.getEvents().addListener(eventPausedEnd, () -> endDialogue());
    }
    
    return npc;
  }

  /**
   * Creates a Cow NPC.
   */
  public static Entity createCow(Entity target, List<Entity> enemies) {
    CowConfig config = configs.cow;
    return createFriendlyNPC(target, enemies, "images/Cow.atlas", 0.2f, config);
  }

  /**
   * Creates a Lion NPC.
   */
  public static Entity createLion(Entity target, List<Entity> enemies) {
    LionConfig config = configs.lion;
    return createFriendlyNPC(target, enemies, "images/lion.atlas", 0.2f, config);
  }

  /**
   * Creates a Turtle NPC.
   */
  public static Entity createTurtle(Entity target, List<Entity> enemies) {
    TurtleConfig config = configs.turtle;
    return createFriendlyNPC(target, enemies, "images/turtle.atlas", 0.5f, config);
  }

  /**
   * Creates an Eagle NPC.
   */
  public static Entity createEagle(Entity target, List<Entity> enemies) {
    EagleConfig config = configs.eagle;
    return createFriendlyNPC(target, enemies, "images/eagle.atlas", 0.1f, config);
  }

  /**
   * Creates a Snake NPC.
   */
  public static Entity createSnake(Entity target, List<Entity> enemies) {
    SnakeConfig config = configs.snake;
    return createFriendlyNPC(target, enemies, "images/snake.atlas", 0.1f, config);
  }

  private static void initiateDialogue(String[] animalSoundPaths, String[] hintText) {
    if (animalSoundPaths != null && animalSoundPaths.length > 0) {
      for (String animalSoundPath : animalSoundPaths) {
        Sound animalSound = ServiceLocator.getResourceService().getAsset(animalSoundPath, Sound.class);
        long soundId = animalSound.play();
        animalSound.setVolume(soundId, 0.3f);
        animalSound.setLooping(soundId, false);
      }
    }

    EntityChatService chatOverlayService = ServiceLocator.getEntityChatService();
    chatOverlayService.updateText(hintText);
  }

  private static void endDialogue() {
    EntityChatService chatOverlayService = ServiceLocator.getEntityChatService();
    chatOverlayService.disposeCurrentOverlay();
  }

  /**
   * Creates a generic Friendly NPC to be used as a base entity by more specific NPC creation methods.
   *
   * @return entity
   */
  private static Entity createFriendlyBaseNPC(Entity target, List<Entity> enemies) {
    AITaskComponent aiComponent =
            new AITaskComponent()
                    .addTask(new WanderTask(new Vector2(2f, 2f), 2f))
                    .addTask(new PauseTask(target, 10, 2f, 1f));

    // Avoid all the enemies on the game
    for (Entity enemy : enemies) {
      aiComponent.addTask(new AvoidTask(enemy, 10, 3f, 3f));
    }

    Entity npc =
            new Entity()
                    .addComponent(new PhysicsComponent())
                    .addComponent(new PhysicsMovementComponent())
                    .addComponent(new ColliderComponent())
                    .addComponent(aiComponent);

    PhysicsUtils.setScaledCollider(npc, 0.9f, 0.4f);
    return npc;
  }

  /**
   * Creates a generic NPC to be used as a base entity by more specific NPC creation methods.
   *
   * @return entity
   */
  private static Entity createBaseNPC(Entity target) {
    AITaskComponent aiComponent =
        new AITaskComponent()
            .addTask(new WanderTask(new Vector2(2f, 2f), 2f))
            .addTask(new ChaseTask(target, 10, 3f, 4f));
    Entity npc =
        new Entity()
            .addComponent(new PhysicsComponent())
            .addComponent(new PhysicsMovementComponent())
            .addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
            .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 1.5f))
            .addComponent(aiComponent);

    PhysicsUtils.setScaledCollider(npc, 0.9f, 0.4f);
    return npc;
  }

  private NPCFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
