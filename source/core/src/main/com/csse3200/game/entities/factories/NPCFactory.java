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
import java.util.function.Supplier;

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

    AnimationRenderComponent animator = initAnimator(config);
    animator.addAnimation("float", config.getAnimationSpeed(), Animation.PlayMode.LOOP);
    animator.addAnimation("selected", config.getAnimationSpeed(), Animation.PlayMode.LOOP);

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
      npc.getEvents().addListener(eventPausedStart, (String[][] hintText, Entity entity) -> initiateDialogue(animalSoundPaths, hintText, entity));

      npc.getEvents().addListener(eventPausedEnd, () -> endDialogue());
    }

    return npc;
  }

  /** Drops an item near the player when called.
   *
   * @param itemGenerator - creates the item to drop
   * @param player - the player to drop the item next to.
   */
  private static void handleDropItem(Supplier<AbstractItem> itemGenerator, Entity player) {
    Entity itemEntity = ItemFactory.createItem(player, itemGenerator.get());
    itemEntity.setScale(new Vector2(0.4f, 0.4f));
    int radius = 2; // Spawn the item within this radius of the player
    player.getEvents().trigger("dropItems", itemEntity, radius);
  }

  /**
   * Creates a Cow NPC, configures its behavior based on the provided target and enemies,
   * and handles any conditional drops specific to the Cow NPC.
   *
   * @param target The entity that the Cow NPC will assist or follow.
   * @param enemies A list of enemy entities that the Cow NPC will be aware of or react to.
   * @return A new Cow NPC entity with the appropriate behavior and configurations,
   *         including handling conditional item drops.
   */
  public static Entity createCow(Entity target, List<Entity> enemies) {
    BaseFriendlyEntityConfig config = configs.cow;
    Entity cow = createFriendlyNPC(target, enemies, config);
    handleConditionalDrop(cow, target);

    return cow;
  }

  /**
   * Creates a Fish NPC, configures its behavior based on the provided target and enemies,
   * and handles any conditional drops specific to the Fish NPC.
   *
   * @param target The entity that the Fish NPC will assist or follow.
   * @param enemies A list of enemy entities that the Fish NPC will be aware of or react to.
   * @return A new Fish NPC entity with the appropriate behavior and configurations,
   *         including handling conditional item drops.
   */
  public static Entity createFish(Entity target, List<Entity> enemies) {
    BaseFriendlyEntityConfig config = configs.fish;
    Entity fish = createFriendlyNPC(target, enemies, config);
    handleConditionalDrop(fish, target);
    return fish;
  }

  /**
   * Creates a Lion NPC and configures its behavior based on the provided target and enemies.
   *
   * @param target The entity that the Lion NPC will assist or follow.
   * @param enemies A list of enemy entities that the Lion NPC will be aware of or react to.
   * @return A new Lion NPC entity with the appropriate behavior and configurations.
   */
  public static Entity createLion(Entity target, List<Entity> enemies) {
    BaseFriendlyEntityConfig config = configs.lion;
    return createFriendlyNPC(target, enemies, config);
  }

  /**
   * Creates a Turtle NPC and configures its behavior based on the provided target and enemies.
   *
   * @param target The entity that the Turtle NPC will assist or follow.
   * @param enemies A list of enemy entities that the Turtle NPC will be aware of or react to.
   * @return A new Turtle NPC entity with the appropriate behavior and configurations.
   */
  public static Entity createTurtle(Entity target, List<Entity> enemies) {
    BaseFriendlyEntityConfig config = configs.turtle;
    return createFriendlyNPC(target, enemies, config);
  }

  /**
   * Creates an Eagle NPC and configures its behavior based on the provided target and enemies.
   *
   * @param target The entity that the Eagle NPC will assist or follow.
   * @param enemies A list of enemy entities that the Eagle NPC will be aware of or react to.
   * @return A new Eagle NPC entity with the appropriate behavior and configurations.
   */
  public static Entity createEagle(Entity target, List<Entity> enemies) {
    BaseFriendlyEntityConfig config = configs.eagle;
    return createFriendlyNPC(target, enemies, config);
  }

  /**
   * Creates a Snake NPC and configures its behavior based on the provided target and enemies.
   *
   * @param target The entity that the Snake NPC will assist or follow.
   * @param enemies A list of enemy entities that the Snake NPC will be aware of or react to.
   * @return A new Snake NPC entity with the appropriate behavior and configurations.
   */
  public static Entity createSnake(Entity target, List<Entity> enemies) {
    BaseFriendlyEntityConfig config = configs.snake;
    return createFriendlyNPC(target, enemies, config);
  }

  /**
   * Creates a Magpie NPC and configures its behavior based on the provided target and enemies.
   *
   * @param target The entity that the Magpie NPC will assist or follow.
   * @param enemies A list of enemy entities that the Magpie NPC will be aware of or react to.
   * @return A new Magpie NPC entity with the appropriate behavior and configurations.
   */
  public static Entity createMagpie(Entity target, List<Entity> enemies) {
    BaseFriendlyEntityConfig config = configs.magpie;
    return createFriendlyNPC(target, enemies, config);
  }

  /**
   * Creates a Chicken NPC and configures its behavior based on the provided target and enemies.
   *
   * @param target The entity that the Chicken NPC will assist or follow.
   * @param enemies A list of enemy entities that the Chicken NPC will be aware of or react to.
   * @return A new Chicken NPC entity with the appropriate behavior and configurations.
   */
  public static Entity createChicken(Entity target, List<Entity> enemies) {
    BaseFriendlyEntityConfig config = configs.friendlyChicken;
    return createFriendlyNPC(target, enemies, config);
  }

  /**
   * Creates a Frog NPC and configures its behavior based on the provided target and enemies.
   *
   * @param target The entity that the Frog NPC will assist or follow.
   * @param enemies A list of enemy entities that the Frog NPC will be aware of or react to.
   * @return A new Frog NPC entity with the appropriate behavior and configurations.
   */
  public static Entity createFrog(Entity target, List<Entity> enemies) {
    BaseFriendlyEntityConfig config = configs.friendlyFrog;
    return createFriendlyNPC(target, enemies, config);
  }

  /**
   * Creates a Monkey NPC and configures its behavior based on the provided target and enemies.
   *
   * @param target The entity that the Monkey NPC will assist or follow.
   * @param enemies A list of enemy entities that the Monkey NPC will be aware of or react to.
   * @return A new Monkey NPC entity with the appropriate behavior and configurations.
   */
  public static Entity createMonkey(Entity target, List<Entity> enemies) {
    BaseFriendlyEntityConfig config = configs.friendlyMonkey;
    return createFriendlyNPC(target, enemies, config);
  }

  /**
   * Creates a Bear NPC and configures its behavior based on the provided target and enemies.
   *
   * @param target The entity that the Bear NPC will assist or follow.
   * @param enemies A list of enemy entities that the Bear NPC will be aware of or react to.
   * @return A new Bear NPC entity with the appropriate behavior and configurations.
   */
  public static Entity createBear(Entity target, List<Entity> enemies) {
    BaseFriendlyEntityConfig config = configs.friendlyBear;
    return createFriendlyNPC(target, enemies, config);
  }

  /**
   * Initializes an animation renderer for a friendly NPC entity based on its configuration.
   *
   * @param entityConfig The configuration for the entity, which contains the sprite path.
   * @return A new AnimationRenderComponent for the friendly NPC, using the entity's sprite assets.
   */
  private static AnimationRenderComponent initAnimator(BaseFriendlyEntityConfig entityConfig) {
    return new AnimationRenderComponent(
            ServiceLocator.getResourceService()
                    .getAsset(entityConfig.getSpritePath(), TextureAtlas.class));
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
    dialogueBoxService = createDialogueIfDestroyed(dialogueBoxService);
    dialogueBoxService.updateText(hintText, DialogueBoxService.DialoguePriority.FRIENDLYNPC);
    playAnimalSound(animalSoundPaths);
  }

  /***
   * Initiates a dialogue by updating the dialogue box with the given text and optionally playing sounds.
   * If the dialogue box service is not available, it creates a new instance.
   *
   * @param animalSoundPaths An array of sound asset paths to play. If null or empty, no sounds are played.
   * @param hintText An array of strings to display in the dialogue box.
   * @param entity The entity to highlight
   * */
  public static void initiateDialogue(String[] animalSoundPaths, String[][] hintText, Entity entity) {
    DialogueBoxService dialogueBoxService = ServiceLocator.getDialogueBoxService();
    dialogueBoxService = createDialogueIfDestroyed(dialogueBoxService);
    dialogueBoxService.updateText(hintText, entity, DialogueBoxService.DialoguePriority.FRIENDLYNPC);
    playAnimalSound(animalSoundPaths);
  }

  /***
   * Created a new DialgoyeBoxService if it ever happens to be deleted
   *
   * @param dialogueBoxService the current dialogue box service
   * @return dialogueBoxService the new updated dialogueBoxService
   */
  private static DialogueBoxService createDialogueIfDestroyed(DialogueBoxService dialogueBoxService) {
    if (dialogueBoxService == null) {
      Stage stage = ServiceLocator.getRenderService().getStage();
      ServiceLocator.registerDialogueBoxService(new DialogueBoxService(stage));
      dialogueBoxService = ServiceLocator.getDialogueBoxService();
    }
    return dialogueBoxService;
  }

  /***
   * Plays the specified animal path sounds (typically one sound)
   *
   * @param animalSoundPaths the entity's animal sound path
   */
  private static void playAnimalSound(String[] animalSoundPaths) {
    if (animalSoundPaths != null) {
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
      Supplier<AbstractItem> itemGenerator;
      switch (npcName) {
          case "Cow":
            itemGenerator = () -> new Foods.Milk(1);
            break;
          case "Fish":
            itemGenerator = () -> new Foods.Sushi(1);
            break;
          default:
            return;
        }
      entity.getEvents().addListener(
              "PlayerFinishedInteracting",
              () -> handleDropItem(itemGenerator, target)
      );
    }
  }

  /**
   * Creates a generic Friendly NPC to be used as a base entity by more specific NPC creation methods.
   *
   * @param target the target (typically player) the entity should know of
   * @param enemies the enemies the FNPC has to avoid
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
