package com.csse3200.game.areas;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.areas.terrain.TerrainFactory.TerrainType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.entities.factories.ObstacleFactory;
import com.csse3200.game.entities.factories.PlayerFactory;
import com.csse3200.game.utils.math.GridPoint2Utils;
import com.csse3200.game.utils.math.RandomUtils;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.components.gamearea.GameAreaDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.ArrayList;
import com.csse3200.game.entities.factories.ItemFactory;
import java.util.function.Supplier;

/** Forest area for the demo game with trees, a player, and some enemies. */
public class ForestGameArea extends GameArea {
  private static final Logger logger = LoggerFactory.getLogger(ForestGameArea.class);
  private static final int NUM_TREES = 7;
  private  static final int NUM_APPLES = 5;
  private  static final int NUM_HEALTH_POTIONS = 3;
  private static final int NUM_GHOSTS = 7;
  private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(10, 10);
  private static final float WALL_WIDTH = 0.1f;
  private static final String[] forestTextures = {
    "images/box_boy_leaf.png",
    "images/tree.png",
    "images/ghost_king.png",
    "images/Cow.png",
    "images/snake.png",
    "images/eagle.png",
    "images/lion.png",
    "images/turtle.png",
    "images/ghost_1.png",
    "images/grass_1.png",
    "images/grass_2.png",
    "images/grass_3.png",
    "images/hex_grass_1.png",
    "images/hex_grass_2.png",
    "images/hex_grass_3.png",
    "images/iso_grass_1.png",
    "images/iso_grass_2.png",
    "images/iso_grass_3.png",
          "images/dog.png",
          "images/croc.png",
          "images/bird.png",
          "images/Healthpotion.png",
          "images/foodtextures/apple.png",
  };
  private static final String[] forestTextureAtlases = {
    "images/terrain_iso_grass.atlas", "images/ghost.atlas", "images/ghostKing.atlas", "images/Cow.atlas",
          "images/snake.atlas", "images/lion.atlas", "images/eagle.atlas", "images/turtle.atlas"
  };
  private static final String[] forestSounds = {"sounds/Impact4.ogg"};
  private static final String[] cowSounds = {"sounds/mooing-cow.mp3"};
  private static final String[] lionSounds = {"sounds/tiger-roar.mp3"};
  private static final String[] turtleSounds = {"sounds/turtle-hiss.mp3"};
  private static final String[] eagleSounds = {"sounds/eagle-scream.mp3"};
  private static final String backgroundMusic = "sounds/BGM_03_mp3.mp3";
  private static final String[] forestMusic = {backgroundMusic};

  private final TerrainFactory terrainFactory;
  private List<Entity> enemies;
  private Entity player;

  /**
   * Initialise this ForestGameArea to use the provided TerrainFactory.
   * @param terrainFactory TerrainFactory used to create the terrain for the GameArea.
   * @requires terrainFactory != null
   */
  public ForestGameArea(TerrainFactory terrainFactory) {
    super();
    this.terrainFactory = terrainFactory;
    this.enemies = new ArrayList<>();
  }

  /** Create the game area, including terrain, static entities (trees), dynamic entities (player) */
  @Override
  public void create() {
    loadAssets();

    displayUI();

    spawnTerrain();
    spawnTrees();
    player = spawnPlayer();
    spawnHealthPotions();
    spawnApples();
    spawnCow();
    spawnLion();
    spawnTurtle();
    spawnCow();
    spawnEagle();
    //spawnSnake();
    //playMusic();
  }

  private void displayUI() {
    Entity ui = new Entity();
    ui.addComponent(new GameAreaDisplay("Box Forest"));
    spawnEntity(ui);
  }

  private void spawnTerrain() {
    // Background terrain
    terrain = terrainFactory.createTerrain(TerrainType.FOREST_DEMO);
    spawnEntity(new Entity().addComponent(terrain));

    // Terrain walls
    float tileSize = terrain.getTileSize();
    GridPoint2 tileBounds = terrain.getMapBounds(0);
    Vector2 worldBounds = new Vector2(tileBounds.x * tileSize, tileBounds.y * tileSize);

    // Left
    spawnEntityAt(
        ObstacleFactory.createWall(WALL_WIDTH, worldBounds.y), GridPoint2Utils.ZERO, false, false);
    // Right
    spawnEntityAt(
        ObstacleFactory.createWall(WALL_WIDTH, worldBounds.y),
        new GridPoint2(tileBounds.x, 0),
        false,
        false);
    // Top
    spawnEntityAt(
        ObstacleFactory.createWall(worldBounds.x, WALL_WIDTH),
        new GridPoint2(0, tileBounds.y),
        false,
        false);
    // Bottom
    spawnEntityAt(
        ObstacleFactory.createWall(worldBounds.x, WALL_WIDTH), GridPoint2Utils.ZERO, false, false);
  }

  private void spawnTrees() {
    GridPoint2 minPos = new GridPoint2(0, 0);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

    for (int i = 0; i < NUM_TREES; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      Entity tree = ObstacleFactory.createTree();
      spawnEntityAt(tree, randomPos, true, false);
    }
  }

  private Entity spawnPlayer() {
    Entity newPlayer = PlayerFactory.createPlayer();
    newPlayer.addComponent(this.terrainFactory.getCameraComponent());
    spawnEntityAt(newPlayer, PLAYER_SPAWN, true, true);
    return newPlayer;
  }

  private void spawnRandomItem(Supplier<Entity> creator, int numEntities) {
    GridPoint2 minPos = new GridPoint2(0, 0);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

    for (int i = 0; i < numEntities; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      Entity obstacle = creator.get();
      spawnEntityAt(obstacle, randomPos, true, false);
    }
  }


  private void spawnHealthPotions() {
    Supplier<Entity> healthPotionGenerator = () -> ItemFactory.createHealthPotion(player);
    spawnRandomItem(healthPotionGenerator, NUM_HEALTH_POTIONS);
  }

  private void spawnApples() {
    Supplier<Entity> appleGenerator = () -> ItemFactory.createApple(player);
    spawnRandomItem(appleGenerator, NUM_APPLES);
  }

  private void spawnEntityOnMap(Entity entity) {
    GridPoint2 minPos = new GridPoint2(0, 0);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);
    GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
    spawnEntityAt(entity, randomPos, true, true);
  }

  private void spawnCow() {
    Entity cow = NPCFactory.createCow(player, this.enemies);
    cow.getEvents().addListener("PausedCow", this::playCowSound);
    spawnEntityOnMap(cow);
  }

  private void spawnLion() {
    Entity lion = NPCFactory.createLion(player, this.enemies);
    lion.getEvents().addListener("PausedLion", this::playLionSound);
    spawnEntityOnMap(lion);
  }

  private void spawnTurtle() {
    Entity turtle = NPCFactory.createTurtle(player, this.enemies);
    turtle.getEvents().addListener("PausedTurtle", this::playTurtleSound);
    spawnEntityOnMap(turtle);
  }

  private void spawnEagle() {
    Entity eagle = NPCFactory.createEagle(player, this.enemies);
    eagle.getEvents().addListener("PausedEagle", this::playEagleSound);
    spawnEntityOnMap(eagle);
  }

  private void spawnSnake() {
    Entity snake = NPCFactory.createSnake(player, this.enemies);
    spawnEntityOnMap(snake);
  }

  private void playAnimalSound(String animalSoundPath) {
    Sound mooingCowSound = ServiceLocator.getResourceService().getAsset(animalSoundPath, Sound.class);
    long soundId = mooingCowSound.play();
    mooingCowSound.setVolume(soundId, 0.3f);
    mooingCowSound.setLooping(soundId, false);
  }

  private void playCowSound() {
    playAnimalSound(cowSounds[0]);
  }

  private void playLionSound() {
    playAnimalSound(lionSounds[0]);
  }

  private void playTurtleSound() {
    playAnimalSound(turtleSounds[0]);
  }

  private void playEagleSound() {
    playAnimalSound(eagleSounds[0]);
  }

  private void playMusic() {
    Music music = ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class);
    music.setLooping(true);
    music.setVolume(0.5f);
    music.play();
  }

  private void loadAssets() {
    logger.debug("Loading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.loadTextures(forestTextures);
    resourceService.loadTextureAtlases(forestTextureAtlases);
    resourceService.loadSounds(forestSounds);
    resourceService.loadSounds(cowSounds);
    resourceService.loadSounds(lionSounds);
    resourceService.loadSounds(turtleSounds);
    resourceService.loadSounds(eagleSounds);
    resourceService.loadMusic(forestMusic);

    while (!resourceService.loadForMillis(10)) {
      // This could be upgraded to a loading screen
      logger.info("Loading... {}%", resourceService.getProgress());
    }
  }

  private void unloadAssets() {
    logger.debug("Unloading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.unloadAssets(forestTextures);
    resourceService.unloadAssets(forestTextureAtlases);
    resourceService.unloadAssets(forestSounds);
    resourceService.unloadAssets(cowSounds);
    resourceService.unloadAssets(lionSounds);
    resourceService.unloadAssets(turtleSounds);
    resourceService.unloadAssets(eagleSounds);
    resourceService.unloadAssets(forestMusic);
  }

  @Override
  public void dispose() {
    super.dispose();
    ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class).stop();
    this.unloadAssets();
  }
}
