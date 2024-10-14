package com.csse3200.game.components.player;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.MapHandler;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.overlays.Overlay.OverlayType;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.screens.MainGameScreen;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.csse3200.game.components.audio.DogSoundPlayer;
import com.csse3200.game.components.audio.AirAnimalSoundPlayer;
import com.csse3200.game.components.audio.WaterAnimalSoundPlayer;

import java.util.Objects;

import static com.csse3200.game.areas.forest.ForestGameArea.MAP_SIZE;

public class PlayerActions extends Component {
  private static final float BASE_SPEED = 3f; // Base speed in meters per second
  private static final float SPEED_MULTIPLIER = 0.03f; // Adjust this to balance speed differences
  private static final int SOUND_INTERVAL = 10000; // milli-secs
  private static final int SOUND_LENGTH = 1000; // milli-secs

  private PhysicsComponent physicsComponent;
  private CombatStatsComponent combatStatsComponent;
  private TextureRenderComponent textureRenderComponent;
  private Vector2 walkDirection = Vector2.Zero.cpy();
  private boolean moving = false;
  private static final Logger logger = LoggerFactory.getLogger(PlayerActions.class);
  private final Entity player;
  private DogSoundPlayer dogSoundPlayer;
  private AirAnimalSoundPlayer airAnimalSoundPlayer;
  private WaterAnimalSoundPlayer waterAnimalSoundPlayer;
  private String selectedAnimal;
  private final GdxGame game;
  private final float areaBorderY;

  private GameTime gameTime;
  private long lastTimeSoundPlayed = 0;

  public PlayerActions(GdxGame game, Entity player, String selectedAnimal, TerrainComponent terrain) {
    this.game = game;
    this.player = player;
    this.selectedAnimal = selectedAnimal;
    this.areaBorderY = terrain.tileToWorldPosition(new GridPoint2(0, MAP_SIZE.y / 3)).y;
  }

  @Override
  public void create() {
    gameTime = ServiceLocator.getTimeSource();
    physicsComponent = entity.getComponent(PhysicsComponent.class);
    combatStatsComponent = entity.getComponent(CombatStatsComponent.class);
    textureRenderComponent = entity.getComponent(TextureRenderComponent.class);
    entity.getEvents().addListener("walk", this::walk);
    entity.getEvents().addListener("walkStop", this::stopWalking);
    entity.getEvents().addListener("attack", this::attack);
    entity.getEvents().addListener("restMenu", this::restMenu);
    entity.getEvents().addListener("quest", this::quest);
    entity.getEvents().addListener("statsInfo", this::statsInfo);
    entity.getEvents().addListener("startCombat", this::startCombat);
    entity.getEvents().addListener("unlockNextArea", this::unlocknextarea);
    entity.getEvents().addListener("stoF", this::stof);

    switch (selectedAnimal) {
      case "images/dog.png":
        // Updated to use sound paths
        String pantingSoundPath = "sounds/animal/panting.mp3";
        String barkingSoundPath = "sounds/animal/bark.mp3";
        dogSoundPlayer = new DogSoundPlayer(pantingSoundPath, barkingSoundPath);
        break;
      case "images/bird.png":
        String flappingSound = "sounds/animal/flap.mp3";
        String screechSound = "sounds/animal/birdscreech.mp3";
        airAnimalSoundPlayer = new AirAnimalSoundPlayer(flappingSound, screechSound);
        break;
      case "images/croc.png":
        String swimmingSound = "sounds/animal/waterwhoosh.mp3";
        waterAnimalSoundPlayer = new WaterAnimalSoundPlayer(swimmingSound);
        break;
      default:
        logger.error("Unknown animal file path");
    }
  }

  /**
   * Switches to the forest map.
   */
  private void stof() {
    MainGameScreen mainGameScreen = (MainGameScreen) game.getScreen();
    mainGameScreen.setMap(MapHandler.MapType.FOREST);
  }

  /**
   * Switches to the water map.
   */
  private void switchMap() {
    MainGameScreen mainGameScreen = (MainGameScreen) game.getScreen();
    if (MapHandler.getUnlockedOcean()) {
      mainGameScreen.setMap(MapHandler.MapType.WATER);
    } else {
      mainGameScreen.setMap(MapHandler.MapType.FOG);
    }
  }

  /**
   * Unlocks the next area.
   */
  public void unlocknextarea() {
    MapHandler.unlockNextArea();
  }

  /**
   * Checks if the boss in current area is defeated to unlock the ocean area
   *
   * @param player entity to check last triggered events
   */
  public void unlockOceanMap(Entity player) {
    if (Objects.equals(player.getEvents().getLastTriggeredEvent(), "kangaDefeated")) {
      MapHandler.updateBossDefeatCount();
      MapHandler.setUnlockedWater(true);
    }
  }

  @Override
  public void update() {
    if (moving) {
      updateSpeed();
    }

    // Update the panting sound based on movement
    if (dogSoundPlayer != null) {
      dogSoundPlayer.updatePantingSound(moving);
    }

    // Existing logic for sound intervals (if still needed for other animals)
    if (gameTime.getTimeSince(lastTimeSoundPlayed) >= SOUND_INTERVAL) {
      playSound();
      lastTimeSoundPlayed = gameTime.getTime();
    } else if (gameTime.getTimeSince(lastTimeSoundPlayed) >= SOUND_LENGTH) {
      stopSound();
    }

    if (entity.getPosition().y > areaBorderY && selectedAnimal.equals("images/dog.png")) {
      // moved from forest to water area. change to croc.
      textureRenderComponent.setTexture("images/croc.png");
      selectedAnimal = "images/croc.png";
    } else if (entity.getPosition().y <= areaBorderY && selectedAnimal.equals("images/croc.png")) {
      // moved from water to forest area. change to dog.
      textureRenderComponent.setTexture("images/dog.png");
      selectedAnimal = "images/dog.png";
    }
  }

  private void playSound() {
    if (dogSoundPlayer != null) {
      // Panting sound is managed in updatePantingSound(), no need to play here
    } else if (airAnimalSoundPlayer != null) {
      airAnimalSoundPlayer.playFlappingSound();
    } else {
      waterAnimalSoundPlayer.playSwimmingSound();
    }
  }

  private void stopSound() {
    if (dogSoundPlayer != null) {
      // Panting sound is managed in updatePantingSound(), no need to stop here
    } else if (airAnimalSoundPlayer != null) {
      airAnimalSoundPlayer.stopFlappingSound();
    } else {
      waterAnimalSoundPlayer.stopSwimmingSound();
    }
  }

  /**
   * Gets if the player is moving or not
   *
   * @return boolean of if the player currently moving
   */
  public boolean isMoving() {
    return moving;
  }

  private void updateSpeed() {
    Body body = physicsComponent.getBody();
    Vector2 velocity = body.getLinearVelocity();
    float speedStat = combatStatsComponent.getSpeed();
    float adjustedSpeed = BASE_SPEED + (speedStat * SPEED_MULTIPLIER);
    Vector2 desiredVelocity = walkDirection.cpy().scl(adjustedSpeed);
    Vector2 impulse = desiredVelocity.sub(velocity).scl(body.getMass());
    body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
  }

  void walk(Vector2 direction) {
    this.walkDirection = direction.nor();
    moving = true;
    logger.info("Player started moving in direction: {}", direction);
    player.getEvents().trigger("steps");

    // Start panting sound if dog
    if (dogSoundPlayer != null) {
      dogSoundPlayer.playPantingSound();
    }
  }

  void stopWalking() {
    this.walkDirection = Vector2.Zero.cpy();
    updateSpeed();
    moving = false;
    logger.info("Player stopped moving.");

    // Stop panting sound if dog
    if (dogSoundPlayer != null) {
      dogSoundPlayer.stopPantingSound();
    }
  }

  void attack() {
    if (dogSoundPlayer != null) {
      dogSoundPlayer.playBarkingSound();
    } else if (airAnimalSoundPlayer != null) {
      airAnimalSoundPlayer.playScreechSound();
    } else if (waterAnimalSoundPlayer != null) {
      waterAnimalSoundPlayer.playSwimmingSound();
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

  private void statsInfo() {
    logger.debug("Triggering addOverlay for PlayerStatsOverlay");
    MainGameScreen mainGameScreen = (MainGameScreen) game.getScreen();
    mainGameScreen.addOverlay(OverlayType.PLAYER_STATS_OVERLAY);
  }

  /**
   * Initiates combat with a specified enemy entity. Depending on the type of enemy,
   * it displays an appropriate cutscene screen.
   *
   * @param enemy The enemy entity that the player is engaging in combat with.
   */
  public void startCombat(Entity enemy) {
    // Stop player movement
    stopWalking();
    // Check if the enemy is a boss type
    if (enemy.getEnemyType() == Entity.EnemyType.KANGAROO ||
            enemy.getEnemyType() == Entity.EnemyType.WATER_BOSS ||
            enemy.getEnemyType() == Entity.EnemyType.AIR_BOSS) {
      // Add a boss cutscene screen for boss encounters
      game.addBossCutsceneScreen(player, enemy);
    } else {
      // Add a standard enemy cutscene screen for regular encounters
      game.addEnemyCutsceneScreen(player, enemy);
    }
  }
}
