package com.csse3200.game.components.player;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.csse3200.game.GdxGame;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.overlays.Overlay.OverlayType;
import com.csse3200.game.components.tasks.ChaseTask;
import com.csse3200.game.components.tasks.WanderTask;
import com.csse3200.game.screens.MainGameScreen;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.csse3200.game.components.audio.DogSoundPlayer;

/**
 * Action component for interacting with the player. Player events should be initialised in create()
 * and when triggered should call methods within this class.
 */
public class PlayerActions extends Component {
  private static final Vector2 MAX_SPEED = new Vector2(3f, 3f); // Metres per second

  private PhysicsComponent physicsComponent;
  private Vector2 walkDirection = Vector2.Zero.cpy();
  private boolean moving = false;
  private static final Logger logger = LoggerFactory.getLogger(PlayerActions.class);
  private final Entity player;
  private DogSoundPlayer dogSoundPlayer;
  private final String selectedAnimal;
  private final GdxGame game;

  public PlayerActions(GdxGame game, Entity player, String selectedAnimal) {
    this.game = game;
    this.player = player;
    this.selectedAnimal = selectedAnimal;
  }

  @Override
  public void create() {
    physicsComponent = entity.getComponent(PhysicsComponent.class);
    entity.getEvents().addListener("walk", this::walk);
    entity.getEvents().addListener("walkStop", this::stopWalking);
    entity.getEvents().addListener("attack", this::attack);
    entity.getEvents().addListener("restMenu", this::restMenu);
    entity.getEvents().addListener("quest", this::quest);
    entity.getEvents().addListener("startCombat", this::startCombat);

    if ("images/dog.png".equals(selectedAnimal)) {
      Sound pantingSound = ServiceLocator.getResourceService().getAsset("sounds/animal/panting.mp3", Sound.class);
      Sound barkingSound = ServiceLocator.getResourceService().getAsset("sounds/animal/bark.mp3", Sound.class);
      dogSoundPlayer = new DogSoundPlayer(pantingSound, barkingSound);
    }
    // Handle other animals (e.g., cat) here
  }

  @Override
  public void update() {
    if (dogSoundPlayer != null) {
      dogSoundPlayer.updatePantingSound(moving, 1.0f);
    }
    if (moving) {
      updateSpeed();
    }
  }

  private void updateSpeed() {
    Body body = physicsComponent.getBody();
    Vector2 velocity = body.getLinearVelocity();
    Vector2 desiredVelocity = walkDirection.cpy().scl(MAX_SPEED);
    Vector2 impulse = desiredVelocity.sub(velocity).scl(body.getMass());
    body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
  }

  void walk(Vector2 direction) {
    this.walkDirection = direction;
    moving = true;
    logger.info("Player started moving in direction: " + direction);
    player.getEvents().trigger("steps");
  }

  void stopWalking() {
    this.walkDirection = Vector2.Zero.cpy();
    updateSpeed();
    moving = false;
    logger.info("Player stopped moving.");
  }

  void attack() {
    if (dogSoundPlayer != null) {
      dogSoundPlayer.playBarkingSound(1.0f);
    }
    Sound attackSound = ServiceLocator.getResourceService().getAsset("sounds/Impact4.ogg", Sound.class);
    attackSound.play();
    player.getEvents().trigger("attackTask");
  }

  private void restMenu() {
    logger.info("Sending Pause");
    MainGameScreen mainGameScreen = (MainGameScreen) game.getScreen();
    mainGameScreen.addOverlay(OverlayType.PAUSE_OVERLAY);
  }

  private void quest() {
    logger.debug("Triggering addOverlay for QuestOverlay");
    MainGameScreen mainGameScreen = (MainGameScreen) game.getScreen();
    mainGameScreen.addOverlay(OverlayType.QUEST_OVERLAY);
  }

  public void startCombat(Entity enemy) {
    AITaskComponent aiTaskComponent = enemy.getComponent(AITaskComponent.class);
    PriorityTask currentTask = aiTaskComponent.getCurrentTask();

    if ((currentTask instanceof WanderTask && ((WanderTask) currentTask).isBoss()) ||
            (currentTask instanceof ChaseTask && ((ChaseTask) currentTask).isBoss())) {
      currentTask.stop();
      game.addBossCutsceneScreen(player, enemy);
    } else {
      game.enterCombatScreen(player, enemy);
    }
  }
}
