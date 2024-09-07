package com.csse3200.game.entities.factories;

import com.badlogic.gdx.audio.Sound;
import com.csse3200.game.areas.ForestGameArea;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.npc.FriendlyNPCAnimationController;
import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.components.npc.KangaBossAnimationController;
import com.csse3200.game.components.tasks.ChaseTask;
import com.csse3200.game.components.tasks.WanderTask;
import com.csse3200.game.components.tasks.PauseTask;
import com.csse3200.game.components.tasks.AvoidTask;
import com.csse3200.game.components.ConfigComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityChatService;
import com.csse3200.game.entities.configs.*;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceContainer;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import java.util.List;
import java.util.ArrayList;

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
   * Base method to create a friendly NPC.
   *
   * @param target   entity to move towards when in range.
   * @param enemies  list of enemy entities.
   * @param config  the specific configuration object.
   * @return entity
   */
  private static Entity createFriendlyNPC(Entity target, List<Entity> enemies, BaseEntityConfig config) {
    Entity npc = createFriendlyBaseNPC(target, enemies);

    AnimationRenderComponent animator = init_animator(config);
    animator.addAnimation("float", config.getAnimationSpeed(), Animation.PlayMode.LOOP);

    npc.addComponent(new CombatStatsComponent(config.getHealth(), config.getBaseAttack(), 0, 0, 0,0))
            .addComponent(animator)
            .addComponent(new FriendlyNPCAnimationController())
            .addComponent(new ConfigComponent<>(config));

    npc.getComponent(AnimationRenderComponent.class).scaleEntity();

    // Add Sounds Effect to FNPCs
    String[] animalSoundPaths = config.getSoundPath();
    if (animalSoundPaths != null && animalSoundPaths.length > 0) {
      String eventPausedStart = String.format("PauseStart%s", config.getAnimalName());
      String eventPausedEnd = String.format("PauseEnd%s", config.getAnimalName());
      npc.getEvents().addListener(eventPausedStart, (String[][] hintText) -> initiateDialogue(animalSoundPaths, hintText));
      npc.getEvents().addListener(eventPausedEnd, () -> endDialogue());
    }

    return npc;
  }

  /**
   * Creates a Cow NPC.
   */
  public static Entity createCow(Entity target, List<Entity> enemies) {
    CowConfig config = NPCConfigs.cow;
    return createFriendlyNPC(target, enemies, config);
  }

  /**
   * Creates a Lion NPC.
   */
  public static Entity createLion(Entity target, List<Entity> enemies) {
    LionConfig config = NPCConfigs.lion;
    return createFriendlyNPC(target, enemies, config);
  }

  /**
   * Creates a Turtle NPC.
   */
  public static Entity createTurtle(Entity target, List<Entity> enemies) {
    TurtleConfig config = NPCConfigs.turtle;
    return createFriendlyNPC(target, enemies, config);
  }

  /**
   * Creates an Eagle NPC.
   */
  public static Entity createEagle(Entity target, List<Entity> enemies) {
    EagleConfig config = NPCConfigs.eagle;
    return createFriendlyNPC(target, enemies, config);
  }

  /**
   * Creates a Snake NPC.
   */
  public static Entity createSnake(Entity target, List<Entity> enemies) {
    SnakeConfig config = NPCConfigs.snake;
    return createFriendlyNPC(target, enemies, config);
  }

  private static AnimationRenderComponent init_animator(BaseEntityConfig entity_config) {
    return new AnimationRenderComponent(
            ServiceLocator.getResourceService()
                    .getAsset(entity_config.getSpritePath(), TextureAtlas.class));
  }

  private static void initiateDialogue(String[] animalSoundPaths, String[][] hintText) {
    EntityChatService chatOverlayService = ServiceLocator.getEntityChatService();
    chatOverlayService.updateText(hintText);

    if (animalSoundPaths != null && animalSoundPaths.length > 0) {
      for (String animalSoundPath : animalSoundPaths) {
        Sound animalSound = ServiceLocator.getResourceService().getAsset(animalSoundPath, Sound.class);
        long soundId = animalSound.play();
        animalSound.setVolume(soundId, 0.3f);
        animalSound.setLooping(soundId, false);
      }
    }

  }

  private static void endDialogue() {
    EntityChatService chatOverlayService = ServiceLocator.getEntityChatService();
    chatOverlayService.hideCurrentOverlay();
  }
  
  // TODO: Fix this so comment this method out if need be
  public static void registerFriendlyNPC(Entity npc) {
    // Register the NPC as a friendly one
    BaseEntityConfig chicken = NPCConfigs.chicken;
    createFriendlyNPC(npc, ForestGameArea.enemies, chicken);
    npc.getEvents().trigger("onFriendlyNPCCreated");
    // Need to add additional logic
  }
  
  /**
   * Creates a generic Friendly NPC to be used as a base entity by more specific NPC creation methods.
   *
   * @return entity
   */
  private static Entity createFriendlyBaseNPC(Entity target, List<Entity> enemies) {
    AITaskComponent aiComponent =
            new AITaskComponent()
                    .addTask(new WanderTask(new Vector2(2f, 2f), 2f, false))
                    .addTask(new PauseTask(target, 10, 2f, 1.2f, false));

    // Avoid all the enemies on the game
    for (Entity enemy : enemies) {
      aiComponent.addTask(new AvoidTask(enemy, 10, 3f, 3f, false));
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
     * Creates a Kangaroo Boss entity. This is the NPC for the final boss of the game.
     *
     * @param target entity to chase
     * @return entity
     */
    public static Entity createKangaBossEntity(Entity target) {
        Entity kangarooBoss = createBossNPC(target);
        BaseEntityConfig config = NPCConfigs.kangarooBoss;

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
        BaseEntityConfig config = configs.kangarooBoss;

        kangarooBoss
                .addComponent(new TextureRenderComponent("images/final_boss_kangaroo_idle.png"))
                .addComponent(new CombatStatsComponent(config.health, 100, 100, 100, 100, 100));

        kangarooBoss.scaleHeight(3.0f);

        return kangarooBoss;
    }

  /**
   * Creates a generic NPC to be used as a base entity by more specific NPC creation methods.
   *
   * @return entity
   */
  private static Entity createBaseNPC(Entity target) {
    AITaskComponent aiComponent =
            new AITaskComponent()
                    .addTask(new WanderTask(new Vector2(2f, 2f), 2f, true))
                    .addTask(new ChaseTask(target, 10, 3f, 4f, true));
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


    private NPCFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
