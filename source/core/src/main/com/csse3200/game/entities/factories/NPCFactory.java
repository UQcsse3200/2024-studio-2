package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.npc.FriendlyNPCAnimationController;
import com.csse3200.game.components.tasks.WanderTask;
import com.csse3200.game.components.tasks.PauseTask;
import com.csse3200.game.components.tasks.AvoidTask;
import com.csse3200.game.components.ConfigComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.inventory.items.AbstractItem;
import com.csse3200.game.inventory.items.food.AbstractFood;
import com.csse3200.game.inventory.items.food.Foods;
import com.csse3200.game.lighting.components.FadeLightsDayTimeComponent;
import com.csse3200.game.lighting.components.LightingComponent;
import com.csse3200.game.services.DialogueBoxService;
import com.csse3200.game.entities.configs.*;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.AudioManager;
import com.csse3200.game.services.ServiceLocator;
import java.util.List;

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
  private static Entity createFriendlyNPC(Entity target, List<Entity> enemies, BaseFriendlyEntityConfig config) {
    Entity npc = createFriendlyBaseNPC(target, enemies);

    AnimationRenderComponent animator = init_animator(config);
    animator.addAnimation("float", config.getAnimationSpeed(), Animation.PlayMode.LOOP);
    InputComponent inputComponent =
            ServiceLocator.getInputService().getInputFactory().createForDialogue();

    npc.addComponent(new CombatStatsComponent(config.getHealth(), config.getBaseAttack(), 0, 0, 0, 0, 100, false, false, 1))
            .addComponent(animator)
            .addComponent(new FriendlyNPCAnimationController())
            .addComponent(inputComponent)
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

  /** Drops an item near the player when called.
   *
   * @param item - the item to drop
   * @param player - the player to drop the item next to.
   */
  private static void handleDropItem(AbstractItem item, Entity player) {
    Entity itemEntity = ItemFactory.createItem(player, item);
    itemEntity.setScale(new Vector2(0.4f, 0.4f));
    int radius = 2; // Spawn the item within this radius of the player
    player.getEvents().trigger("dropItems", itemEntity, radius);
  }

  /**
   * Creates a Cow NPC.
   */
  public static Entity createCow(Entity target, List<Entity> enemies) {
    BaseFriendlyEntityConfig config = configs.cow;
    Entity cow = createFriendlyNPC(target, enemies, config);
    handleConditionalDrop(cow, target);

    return cow;
  }

  /**
   * Creates a Fish NPC.
   */
  public static Entity createFish(Entity target, List<Entity> enemies) {
    BaseFriendlyEntityConfig config = configs.fish;
    Entity fish = createFriendlyNPC(target, enemies, config);
    handleConditionalDrop(fish, target);
    return fish;
  }

  /**
   * Creates a Lion NPC.
   */
  public static Entity createLion(Entity target, List<Entity> enemies) {
    BaseFriendlyEntityConfig config = configs.lion;
    return createFriendlyNPC(target, enemies, config);
  }

  /**
   * Creates a Turtle NPC.
   */
  public static Entity createTurtle(Entity target, List<Entity> enemies) {
    BaseFriendlyEntityConfig config = configs.turtle;
    return createFriendlyNPC(target, enemies, config);
  }

  /**
   * Creates an Eagle NPC.
   */
  public static Entity createEagle(Entity target, List<Entity> enemies) {
    BaseFriendlyEntityConfig config = configs.eagle;
    return createFriendlyNPC(target, enemies, config);
  }

  /**
   * Creates a Snake NPC.
   */
  public static Entity createSnake(Entity target, List<Entity> enemies) {
    BaseFriendlyEntityConfig config = configs.snake;
    return createFriendlyNPC(target, enemies, config);
  }

  /**
   * Creates a Magpie NPC.
   */
  public static Entity createMagpie(Entity target, List<Entity> enemies) {
    BaseFriendlyEntityConfig config = configs.magpie;
    return createFriendlyNPC(target, enemies, config);
  }
  
  public static Entity createChicken(Entity target, List<Entity> enemies) {
    BaseFriendlyEntityConfig config = configs.friendlyChicken;
    return createFriendlyNPC(target, enemies, config);
  }
  
  public static Entity createFrog(Entity target, List<Entity> enemies) {
    BaseFriendlyEntityConfig config = configs.friendlyFrog;
    return createFriendlyNPC(target, enemies, config);
  }
  
  public static Entity createMonkey(Entity target, List<Entity> enemies) {
    BaseFriendlyEntityConfig config = configs.friendlyMonkey;
    return createFriendlyNPC(target, enemies, config);
  }

  private static AnimationRenderComponent init_animator(BaseFriendlyEntityConfig entity_config) {
    return new AnimationRenderComponent(
            ServiceLocator.getResourceService()
                    .getAsset(entity_config.getSpritePath(), TextureAtlas.class));
  }

  /**
   * Initiates a dialogue by updating the dialogue box with the given text and optionally playing sounds.
   * If the dialogue box service is not available, it creates a new instance.
   *
   * @param animalSoundPaths An array of sound asset paths to play. If null or empty, no sounds are played.
   * @param hintText An array of strings to display in the dialogue box.
   */
  public static void initiateDialogue(String[] animalSoundPaths, String[][] hintText) {
    DialogueBoxService dialogueBoxService = ServiceLocator.getDialogueBoxService();

    // Needs new chatOverlayService when screen recovered from preserving screen (e.g. to play mini-game)
    if (dialogueBoxService == null) {
      Stage stage = ServiceLocator.getRenderService().getStage();
      ServiceLocator.registerDialogueBoxService(new DialogueBoxService(stage));
      dialogueBoxService = ServiceLocator.getDialogueBoxService();
    }

    dialogueBoxService.updateText(hintText, -2);

    if (animalSoundPaths != null && animalSoundPaths.length > 0) {
      for (String animalSoundPath : animalSoundPaths) {
        AudioManager.playSound(animalSoundPath);
      }
    }
  }

  /**
   * Ends a dialogue and takes it off the screen
   */
  public static void endDialogue () {
    DialogueBoxService dialogueBoxService = ServiceLocator.getDialogueBoxService();

    // Needs new chatOverlayService when screen recovered from preserving screen (e.g. to play mini-game)
    if (dialogueBoxService == null) {
      Stage stage = ServiceLocator.getRenderService().getStage();
      ServiceLocator.registerDialogueBoxService(new DialogueBoxService(stage));
    } else {
      dialogueBoxService.hideCurrentOverlay();
    }
  }

  /**
   * Handles a conditional item drop for an entity.
   *
   * @param entity The entity to which the event listener will be added.
   * @param target The target entity near which the item will be dropped.
   */
  private static void handleConditionalDrop(Entity entity, Entity target) {
    BaseFriendlyEntityConfig configComponent = (BaseFriendlyEntityConfig) (entity.getComponent(ConfigComponent.class)).getConfig();
    float probability = configComponent.getItemProbability();
    String npcName = configComponent.getAnimalName();

    if (Math.random() > probability) { // Attach according to probabilities
      AbstractFood item = null;
      switch (npcName) {
        case "Cow":
          item = new Foods.Milk(1);
          break;
        case "Fish":
          item = new Foods.Sushi(1);
          break;
        default:
          return;
      }
      AbstractFood finalItem = item;
      entity.getEvents().addListener(
              "PlayerFinishedInteracting",
              () -> handleDropItem(finalItem, target)
      );
    }
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
                    .addComponent(aiComponent)
                    .addComponent(new LightingComponent().attach(LightingComponent.createPointLight(2f, Color.FOREST)))
                    .addComponent(new FadeLightsDayTimeComponent());;

    PhysicsUtils.setScaledCollider(npc, 0.9f, 0.4f);
    return npc;
  }

  private NPCFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
