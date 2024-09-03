package com.csse3200.game.areas;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.ForestGameAreaConfigs.*;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.areas.terrain.TerrainFactory.TerrainType;
import com.csse3200.game.components.ProximityComponent;
import com.csse3200.game.components.quests.QuestPopup;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.*;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.utils.math.RandomUtils;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.components.gamearea.GameAreaDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Supplier;

/** Forest area for the demo game with trees, a player, and some enemies. */
public class ForestGameArea extends GameArea {
  private static final Logger logger = LoggerFactory.getLogger(ForestGameArea.class);
  private static final ForestGameAreaConfig config = new ForestGameAreaConfig();
  private static final GridPoint2 MAP_SIZE = new GridPoint2(5000, 5000);
  private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(2500, 2500);
  private static final GridPoint2 KANGAROO_BOSS_SPAWN = new GridPoint2(25, 10);
  private static final float WALL_WIDTH = 0.1f;
  private final TerrainFactory terrainFactory;
  private final List<Entity> enemies;
  private Entity player;

  private final GdxGame game;

  // Boolean to ensure that only a single boss entity is spawned when a trigger happens
  private boolean kangarooBossSpawned = false;

  /**
   * Initialise this ForestGameArea to use the provided TerrainFactory.
   * @param terrainFactory TerrainFactory used to create the terrain for the GameArea.
   * @param game GdxGame needed for creating the player
   * @requires terrainFactory != null
   */
  public ForestGameArea(TerrainFactory terrainFactory, GdxGame game) {
    super();
    this.enemies = new ArrayList<>();
    this.terrainFactory = terrainFactory;
    this.game = game;
  }

  /**
   * Create the game area, including terrain, static entities (trees),
   * dynamic entities (player and NPCs), and ui
   * */
  @Override
  public void create() {
    loadAssets();

    displayUI();

    spawnTerrain();
    spawnTrees();
    player = spawnPlayer();

    //Enemies
//      for (int i = 0;i < configs.NUM_CHICKENS; i++) {
//        spawnChicken();
//      }
//      for (int i = 0; i< configs.NUM_FROGS; i++) {
//        spawnFrog();
//      }
      for (int i = 0; i< config.spawns.NUM_MONKEYS; i++) {
        spawnMonkey();
      }

      // items
    spawnHealthPotions();
    spawnApples();

    //Friendlies
    spawnCow();
    spawnLion();
    spawnTurtle();
    spawnCow();
    spawnEagle();
    spawnSnake();
    playMusic();
    player.getEvents().addListener("spawnKangaBoss", this::spawnKangarooBoss);
    kangarooBossSpawned = false;
  }

  /**
   * gets the player
   * @return player entity
   */
  public Entity getPlayer () {
    return player;
  }

  public void displayUI() {
    Entity ui = new Entity();
    ui.addComponent(new GameAreaDisplay("Box Forest"));
    ui.addComponent(new QuestPopup());
    spawnEntity(ui);
  }

  private void spawnTerrain() {
    // Background terrain
    terrain = terrainFactory.createTerrain(TerrainType.FOREST_DEMO, PLAYER_SPAWN, MAP_SIZE);
    spawnEntity(new Entity().addComponent(terrain));

    // // Terrain walls
    // float tileSize = terrain.getTileSize();
    // GridPoint2 tileBounds = terrain.getMapBounds(0);
    // Vector2 worldBounds = new Vector2(tileBounds.x * tileSize, tileBounds.y * tileSize);

    //  // Left
    //  spawnEntityAt(
    //      ObstacleFactory.createWall(WALL_WIDTH, worldBounds.y), GridPoint2Utils.ZERO, false, false);
    //  // Right
    //  spawnEntityAt(
    //      ObstacleFactory.createWall(WALL_WIDTH, worldBounds.y),
    //      new GridPoint2(tileBounds.x, 0),
    //      false,
    //      false);
    //  // Top
    //  spawnEntityAt(
    //      ObstacleFactory.createWall(worldBounds.x, WALL_WIDTH),
    //      new GridPoint2(0, tileBounds.y),
    //      false,
    //      false);
    //  // Bottom
    //  spawnEntityAt(
    //      ObstacleFactory.createWall(worldBounds.x, WALL_WIDTH), GridPoint2Utils.ZERO, false, false);
  }

  private void updateTerrain(GridPoint2 playerPosition) {
    terrain = terrainFactory.createTerrain(TerrainType.FOREST_DEMO, playerPosition, new GridPoint2(20, 20));
      spawnEntity(new Entity().addComponent(terrain));
  }

  public void onPlayerMove(GridPoint2 newPlayerPosition) {
    updateTerrain(newPlayerPosition);
  }

  private void spawnTrees() {
    GridPoint2 minPos = new GridPoint2(PLAYER_SPAWN.x - 10, PLAYER_SPAWN.y - 10);
    GridPoint2 maxPos = new GridPoint2(PLAYER_SPAWN.x + 10, PLAYER_SPAWN.y + 10);

    for (int i = 0; i < config.spawns.NUM_TREES; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      Entity tree = ObstacleFactory.createTree();
      spawnEntityAt(tree, randomPos, true, false);
    }
  }

  /**
   * Creates the player entity for this screen
   * @return the player entity
   */
  private Entity spawnPlayer() {
    Entity newPlayer = PlayerFactory.createPlayer(game);
    newPlayer.addComponent(this.terrainFactory.getCameraComponent());
    spawnEntityAt(newPlayer, PLAYER_SPAWN, true, true);
    return newPlayer;
  }

    private void spawnKangarooBoss() {
        if (!kangarooBossSpawned) {
            Entity kangarooBoss = EnemyFactory.createKangaBossEntity(player);
            spawnEntityOnMap(kangarooBoss);
            kangarooBossSpawned = true;
        }
    }

  /**
   * Spawns a chicken enemy, with the player entity as its target
   */
   private void spawnChicken() {
    GridPoint2 minPos = new GridPoint2(PLAYER_SPAWN.x - 10, PLAYER_SPAWN.y - 10);
    GridPoint2 maxPos = new GridPoint2(PLAYER_SPAWN.x + 10, PLAYER_SPAWN.y + 10);

    GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
    Entity chicken = EnemyFactory.createChicken(player);
    enemies.add(chicken);

    float proximityRange = 0.05f; // Set a suitable proximity range
    chicken.addComponent(new ProximityComponent(player, proximityRange)); // Add ProximityComponent

    spawnEntityAt(chicken, randomPos, true, true);
  }
  /**
   * spawns a frog enemy, with the player entity as its target
   */
  private void spawnFrog() {
    GridPoint2 minPos = new GridPoint2(PLAYER_SPAWN.x - 20, PLAYER_SPAWN.y - 10);
    GridPoint2 maxPos = new GridPoint2(PLAYER_SPAWN.x + 20, PLAYER_SPAWN.y + 10);

    GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
    Entity frog = EnemyFactory.createFrog(player);
    enemies.add(frog);

    float proximityRange = 0.05f; // Set a suitable proximity range
    frog.addComponent(new ProximityComponent(player, proximityRange)); // Add ProximityComponent

    spawnEntityAt(frog, randomPos, true, true);

  }

  /**
   * spawns a monkey enemy, with the player entity as its target
   */
  private void spawnMonkey() {
    GridPoint2 minPos = new GridPoint2(PLAYER_SPAWN.x - 20, PLAYER_SPAWN.y - 10);
    GridPoint2 maxPos = new GridPoint2(PLAYER_SPAWN.x + 20, PLAYER_SPAWN.y + 10);

    GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
    Entity monkey = EnemyFactory.createMonkey(player);
    enemies.add(monkey);

    float proximityRange = 0.05f; // Set a suitable proximity range
    monkey.addComponent(new ProximityComponent(player, proximityRange)); // Add ProximityComponent

    spawnEntityAt(monkey, randomPos, true, true);
  }

  private void spawnHealthPotions() {
    Supplier<Entity> healthPotionGenerator = () -> ItemFactory.createHealthPotion(player);
    spawnRandomItem(healthPotionGenerator, config.spawns.NUM_HEALTH_POTIONS);
  }

  private void spawnApples() {
    Supplier<Entity> appleGenerator = () -> ItemFactory.createApple(player);
    spawnRandomItem(appleGenerator, config.spawns.NUM_APPLES);
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

   private void spawnEntityOnMap(Entity entity) {
       GridPoint2 minPos = new GridPoint2(PLAYER_SPAWN.x - 10, PLAYER_SPAWN.y - 10);
       GridPoint2 maxPos = new GridPoint2(PLAYER_SPAWN.x + 10, PLAYER_SPAWN.y + 10);
        GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
        spawnEntityAt(entity, randomPos, true, true);
    }

    private void spawnCow() {
        Entity cow = NPCFactory.createCow(player, this.enemies);
        spawnEntityOnMap(cow);
    }

    private void spawnLion() {
        Entity lion = NPCFactory.createLion(player, this.enemies);
        spawnEntityOnMap(lion);
    }

    private void spawnTurtle() {
        Entity turtle = NPCFactory.createTurtle(player, this.enemies);
        spawnEntityOnMap(turtle);
    }

    private void spawnEagle() {
        Entity eagle = NPCFactory.createEagle(player, this.enemies);
        spawnEntityOnMap(eagle);
    }

  private void spawnSnake() {
    Entity snake = NPCFactory.createSnake(player, this.enemies);
    spawnEntityOnMap(snake);
  }

  public static void playMusic() {
    Music music = ServiceLocator.getResourceService().getAsset(config.sounds.backgroundMusic,
            Music.class);
    music.setLooping(true);
    music.setVolume(0.5f);
    music.play();
  }

  public static void pauseMusic() {
    Music music = ServiceLocator.getResourceService().getAsset(config.sounds.backgroundMusic, Music.class);
    music.pause();
  }

  public void loadAssets() {
    logger.debug("LOADING ASSETS");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.loadTextures(config.textures.forestTextures);
    resourceService.loadTextureAtlases(config.textures.forestTextureAtlases);
    resourceService.loadSounds(config.sounds.gameSounds);
    resourceService.loadMusic(config.sounds.gameMusic);

    while (!resourceService.loadForMillis(10)) {
      // This could be upgraded to a loading screen
      logger.debug("Loading... {}%", resourceService.getProgress());
    }
  }

  public void unloadAssets() {
    logger.debug("UNLOADING ASSETS");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.unloadAssets(config.textures.forestTextures);
    resourceService.unloadAssets(config.textures.forestTextureAtlases);
    resourceService.unloadAssets(config.sounds.gameSounds);
    resourceService.unloadAssets(config.sounds.gameMusic);
  }

  @Override
  public void dispose() {
    super.dispose();
    ServiceLocator.getResourceService().getAsset(config.sounds.backgroundMusic, Music.class).stop();
    this.unloadAssets();
  }
}