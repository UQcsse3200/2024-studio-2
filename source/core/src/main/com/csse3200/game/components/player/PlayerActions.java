package com.csse3200.game.components.player;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.csse3200.game.GdxGame;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.audio.DogSoundPlayer;
import com.csse3200.game.components.tasks.ChaseTask;
import com.csse3200.game.components.tasks.WanderTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.overlays.Overlay;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.screens.MainGameScreen;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerActions extends Component {
  private static final Logger logger = LoggerFactory.getLogger(PlayerActions.class);

  private PhysicsComponent physicsComponent;
  private Vector2 walkDirection = Vector2.Zero.cpy();
  private boolean moving = false;
  private final Entity player;
  private final String selectedAnimal;
  private final GdxGame game;

  // Sound-related components
  private DogSoundPlayer dogSoundPlayer;

  // CombatStatsComponent to access speed
  private CombatStatsComponent combatStats;

  public PlayerActions(GdxGame game, Entity player, String selectedAnimal) {
    this.game = game;
    this.player = player;
    this.selectedAnimal = selectedAnimal;
  }

  @Override
  public void create() {
    physicsComponent = entity.getComponent(PhysicsComponent.class);
    combatStats = entity.getComponent(CombatStatsComponent.class);

    if (combatStats == null) {
      logger.error("CombatStatsComponent is missing from the player entity.");
      return;
    }

    // Register event listeners
    entity.getEvents().addListener("walk", this::walk);
    entity.getEvents().addListener("walkStop", this::stopWalking);
    entity.getEvents().addListener("attack", this::attack);
    entity.getEvents().addListener("restMenu", this::restMenu);
    entity.getEvents().addListener("quest", this::quest);
    entity.getEvents().addListener("startCombat", this::startCombat);

    // Initialize animal-specific sounds
    initializeAnimalSounds();
  }

  @Override
  public void update() {
    // Update sounds if applicable
    if (dogSoundPlayer != null) {
      dogSoundPlayer.updatePantingSound(moving, 1.0f);
    }

    // Update movement
    if (moving) {
      updateSpeed();
    }
  }

  private void initializeAnimalSounds() {
    switch (selectedAnimal.toLowerCase()) {
      case "dog":
        Sound pantingSound = ServiceLocator.getResourceService()
                .getAsset("sounds/animal/panting.mp3", Sound.class);
        Sound barkingSound = ServiceLocator.getResourceService()
                .getAsset("sounds/animal/bark.mp3", Sound.class);
        dogSoundPlayer = new DogSoundPlayer(pantingSound, barkingSound);
        break;
      // Initialize other animals' sounds here
      default:
        // No specific sounds
        break;
    }
  }

  private void updateSpeed() {
    Body body = physicsComponent.getBody();
    Vector2 velocity = body.getLinearVelocity();

    // Use a base speed and multiply it by the animal's speed stat
    float baseSpeed = 1f; // Adjust this value to get the desired overall movement speed
    float animalSpeedMultiplier = combatStats.getSpeed() / 10f; // Normalize the speed to a 0-1 range
    float finalSpeed = baseSpeed * animalSpeedMultiplier;

    Vector2 desiredVelocity = walkDirection.cpy().scl(finalSpeed);
    Vector2 impulse = desiredVelocity.sub(velocity).scl(body.getMass());

    body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
  }

  void walk(Vector2 direction) {
    this.walkDirection = direction.nor(); // Ensure direction is normalized
    this.moving = true;
    logger.info("Player started moving in direction: {}", direction);
    player.getEvents().trigger("steps");
  }

  void stopWalking() {
    this.walkDirection = Vector2.Zero.cpy();
    updateSpeed(); // Stop the player by setting velocity to zero
    this.moving = false;
    logger.info("Player stopped moving.");
  }

  void attack() {
    if (dogSoundPlayer != null) {
      dogSoundPlayer.playBarkingSound(1.0f);
    }
    Sound attackSound = ServiceLocator.getResourceService()
            .getAsset("sounds/Impact4.ogg", Sound.class);
    attackSound.play();
    player.getEvents().trigger("attackTask");
  }

  private void restMenu() {
    logger.info("Opening Rest Menu Overlay.");
    MainGameScreen mainGameScreen = (MainGameScreen) game.getScreen();
    mainGameScreen.addOverlay(Overlay.OverlayType.PAUSE_OVERLAY);
  }

  private void quest() {
    logger.debug("Triggering Quest Overlay.");
    MainGameScreen mainGameScreen = (MainGameScreen) game.getScreen();
    mainGameScreen.addOverlay(Overlay.OverlayType.QUEST_OVERLAY);
  }

  public void startCombat(Entity enemy) {
    AITaskComponent aiTaskComponent = enemy.getComponent(AITaskComponent.class);
    if (aiTaskComponent == null) {
      logger.warn("Enemy entity does not have an AITaskComponent.");
      return;
    }

    PriorityTask currentTask = aiTaskComponent.getCurrentTask();

    if (currentTask instanceof WanderTask && ((WanderTask) currentTask).isBoss() ||
            currentTask instanceof ChaseTask && ((ChaseTask) currentTask).isBoss()) {
      currentTask.stop();
      game.addBossCutsceneScreen(player, enemy);
    } else {
      game.enterCombatScreen(player, enemy);
    }
  }
}