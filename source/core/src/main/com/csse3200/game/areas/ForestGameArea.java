package com.csse3200.game.areas;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.ForestGameAreaConfigs.*;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.areas.terrain.TerrainFactory.TerrainType;
import com.csse3200.game.areas.terrain.TerrainLoader;
import com.csse3200.game.components.ProximityComponent;
import com.csse3200.game.components.quests.QuestPopup;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.*;
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
  // private final List<Entity> staticItems;
  // private final List<Entity> dynamicItems;

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

    // Terrain
    spawnTerrain();

    // Player
    player = spawnPlayer();
    TerrainLoader.setChunk(player.getPosition());

    // Obstacles
    spawnTrees();

    //Enemies
    spawnEnemies();

    // items
    spawnItems();

    //Friendlies
    spawnNPCs();

    playMusic();
    player.getEvents().addListener("setPosition", this::handleNewChunks);
    player.getEvents().addListener("spawnKangaBoss", this::spawnKangarooBoss);
    kangarooBossSpawned = false;
  }

   private void handleNewChunks(Vector2 playerPos) {
     if (TerrainLoader.movedChunk(playerPos)) {
       logger.info("Player position is: ({}, {})", playerPos.x, playerPos.y);
      }
//     if (!movedChunks(playerPos)) {return;}
//     TerrainComponent.loadChunks(playerPos);
//     handleItems(TerrainComponent.newChunks, TerrainComponent.oldChunks);
//     handleFriendlies(TerrainComponent.newChunks, TerrainComponent.oldChunks);
//     handleEnemies(TerrainComponent.newChunks, TerrainComponent.oldChunks);
//     handleMisc(TerrainComponent.newChunks, TerrainComponent.oldChunks);
   }

  /**
   Chunk Terminology:
   Create - actually generating a new chunk using convolution etc.
   Loaded - has already been generated and is in the hash map of chunks
   Active - loaded chunks within a certain radius of the player on which entities are being actively spawned and maintained.
   Inactive - loaded chunks outside of a certain radius of the player where entities will no longer be maintained.

   Entity (Item/NPC/Bosses) Terminology:
   Static - entity that is kept loaded in the list of entities and is never disposed off due to player movements.
   Dynamic - entity that is only loaded if it is on an active chunk (and we will only update this when the player moves chunks).4

   Player Movement Terminology:
   New Chunk - a chunk which has become active after a player moved chunks.
   Old Chunk - a chunk which become inactive after a player moved chunks.

   GAME CONFIG CONSTANT:
   RADIUS - the radius which differentiates active and inactive chunks.
   */
  // private void handleItems(List<TerrainChunk> newChunks, List<TerrainChunk> oldChunks) {
  // }

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
    TerrainComponent.loadChunks(PLAYER_SPAWN);

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
      Entity kangarooBoss = NPCFactory.createKangaBossEntity(player);
      spawnEntityOnMap(kangarooBoss);
      kangarooBossSpawned = true;
    }
  }

  private void spawnEntityOnMap(Entity entity) {
    GridPoint2 minPos = new GridPoint2(PLAYER_SPAWN.x - 10, PLAYER_SPAWN.y - 10);
    GridPoint2 maxPos = new GridPoint2(PLAYER_SPAWN.x + 10, PLAYER_SPAWN.y + 10);
    GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
    spawnEntityAt(entity, randomPos, true, true);
  }

  private void spawnItems() {
    Supplier<Entity> generator;

    // Health Potions
    generator = () -> ItemFactory.createHealthPotion(player);
    spawnRandomItem(generator, config.spawns.NUM_HEALTH_POTIONS);

    // Apples
    generator = () -> ItemFactory.createApple(player);
    spawnRandomItem(generator, config.spawns.NUM_APPLES);
  }

  private void spawnEnemies() {
    Supplier<Entity> generator;

    // Chicken
    generator = () -> EnemyFactory.createChicken(player);
    spawnRandomEnemy(generator, config.spawns.NUM_CHICKENS, 0.05);

    // Monkey
    generator = () -> EnemyFactory.createMonkey(player);
    spawnRandomEnemy(generator, config.spawns.NUM_MONKEYS, 0.04);

    // Frog
    generator = () -> EnemyFactory.createFrog(player);
    spawnRandomEnemy(generator, config.spawns.NUM_FROGS, 0.06);
  }

  private void spawnNPCs() {
    Supplier<Entity> generator;

    // Cow
    generator = () -> NPCFactory.createCow(player, this.enemies);
    spawnRandomNPC(generator, config.spawns.NUM_COWS);

    // Lion
    generator = () -> NPCFactory.createLion(player, this.enemies);
    spawnRandomNPC(generator, config.spawns.NUM_LIONS);

    // Turtle
    generator = () -> NPCFactory.createTurtle(player, this.enemies);
    spawnRandomNPC(generator, config.spawns.NUM_TURTLES);

    // Eagle
    generator = () -> NPCFactory.createEagle(player, this.enemies);
    spawnRandomNPC(generator, config.spawns.NUM_EAGLES);

    // Snake
    generator = () -> NPCFactory.createSnake(player, this.enemies);
    spawnRandomNPC(generator, config.spawns.NUM_SNAKES);
  }

  private void spawnRandomItem(Supplier<Entity> creator, int numItems) {
    GridPoint2 minPos = new GridPoint2(PLAYER_SPAWN.x - 20, PLAYER_SPAWN.y - 20);
    GridPoint2 maxPos = new GridPoint2(PLAYER_SPAWN.x + 20, PLAYER_SPAWN.y + 20);

    for (int i = 0; i < numItems; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      Entity item = creator.get();
      spawnEntityAt(item, randomPos, true, false);
    }
  }

  private void spawnRandomNPC(Supplier<Entity> creator, int numNPCs) {
    GridPoint2 minPos = new GridPoint2(PLAYER_SPAWN.x - 10, PLAYER_SPAWN.y - 10);
    GridPoint2 maxPos = new GridPoint2(PLAYER_SPAWN.x + 10, PLAYER_SPAWN.y + 10);

    for (int i = 0; i < numNPCs; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      Entity npc = creator.get();
      spawnEntityAt(npc, randomPos, true, false);
    }
  }

  private void spawnRandomEnemy(Supplier<Entity> creator, int numItems, double proximityRange) {
    GridPoint2 minPos = new GridPoint2(PLAYER_SPAWN.x - 20, PLAYER_SPAWN.y - 20);
    GridPoint2 maxPos = new GridPoint2(PLAYER_SPAWN.x + 20, PLAYER_SPAWN.y + 20);

    for (int i = 0; i < numItems; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      Entity enemy = creator.get();
      spawnEntityAt(enemy, randomPos, true, false);
      enemies.add(enemy);
      enemy.addComponent(new ProximityComponent(player, proximityRange)); // Add ProximityComponent
    }
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