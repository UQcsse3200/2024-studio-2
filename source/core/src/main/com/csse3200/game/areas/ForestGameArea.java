package com.csse3200.game.areas;

import com.badlogic.gdx.audio.Music;

import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.areas.terrain.TerrainFactory.TerrainType;
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
  private static final GridPoint2 MAP_SIZE = new GridPoint2(5000, 5000);
  private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(2500, 2500);
  private static final int NUM_TREES = 7;
  private  static final int NUM_APPLES = 5;
  private  static final int NUM_HEALTH_POTIONS = 3;
  private static final int NUM_CHICKENS = 2;
  private static final int NUM_FROGS = 5;
  private static final int NUM_MONKEYS = 2;
  private static final GridPoint2 KANGAROO_BOSS_SPAWN = new GridPoint2(25, 10);
  private static final float WALL_WIDTH = 0.1f;
  private static final String[] forestTextures = {
          "images/box_boy_leaf.png",
          "images/tree.png",
          "images/ghost_king.png",
          "images/final_boss_kangaroo.png",
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
          "images/gt.png",
          "images/top_left_grass.png",
          "images/top_middle_grass.png",
          "images/top_right_grass.png",
          "images/middle_left_grass.png",
          "images/middle_grass.png",
          "images/middle_right_grass.png",
          "images/lower_left_grass.png",
          "images/lower_middle_grass.png",
          "images/lower_right_grass.png",
          "images/full_sand_tile.png",
          "images/dog.png",
          "images/croc.png",
          "images/bird.png",
          "images/Healthpotion.png",
          "images/foodtextures/apple.png",
  };
  private static final String[] forestTextureAtlases = {
    "images/terrain_iso_grass.atlas", "images/chicken.atlas", "images/enemy-chicken.atlas", "images/frog.atlas",
          "images/monkey.atlas", "images/Cow.atlas", "images/snake.atlas", "images/lion.atlas",
          "images/eagle.atlas", "images/turtle.atlas", "images/final_boss_kangaroo.atlas"
  };
  private static final String[] questSounds = {"sounds/QuestComplete.wav"};
  private static final String[] forestSounds = {"sounds/Impact4.ogg"};
    private static final String heartbeat = "sounds/heartbeat.mp3";
    private static final String[] heartbeatSound = {heartbeat};

  private static final List<String[]> soundArrays = List.of(
          new String[] {"sounds/mooing-cow.mp3"},
          new String[] {"sounds/tiger-roar.mp3"},
          new String[] {"sounds/turtle-hiss.mp3"},
          new String[] {"sounds/snake-hiss.mp3"},
          new String[] {"sounds/eagle-scream.mp3"}
  );

  private static final String BACKGROUND_MUSIC = "sounds/BGM_03_mp3.mp3";
  private static final String[] forestMusic = {BACKGROUND_MUSIC};
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
//      for (int i = 0;i < NUM_CHICKENS; i++) {
//        spawnChicken();
//      }
//      for (int i = 0; i< NUM_FROGS; i++) {
//        spawnFrog();
//
//      }
//      for (int i = 0; i< NUM_FROGS; i++) {
//        spawnMonkey();
//      }

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

    for (int i = 0; i < NUM_TREES; i++) {
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
    spawnRandomItem(healthPotionGenerator, NUM_HEALTH_POTIONS);
  }

  private void spawnApples() {
    Supplier<Entity> appleGenerator = () -> ItemFactory.createApple(player);
    spawnRandomItem(appleGenerator, NUM_APPLES);
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
    Music music = ServiceLocator.getResourceService().getAsset(BACKGROUND_MUSIC, Music.class);
    music.setLooping(true);
    music.setVolume(0.5f);
    music.play();
  }
  public static void pauseMusic() {
    Music music = ServiceLocator.getResourceService().getAsset(BACKGROUND_MUSIC, Music.class);
    music.pause();
  }

  public void loadAssets() {
    logger.debug("Loading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.loadTextures(forestTextures);
    resourceService.loadTextureAtlases(forestTextureAtlases);
    resourceService.loadSounds(questSounds);
    resourceService.loadSounds(forestSounds);
    for (String[] sounds : soundArrays) {
      resourceService.loadSounds(sounds);
    }
    resourceService.loadMusic(forestMusic);
    resourceService.loadMusic(heartbeatSound);

    while (!resourceService.loadForMillis(10)) {
      // This could be upgraded to a loading screen
      logger.info("Loading... {}%", resourceService.getProgress());
    }
  }

  public void unloadAssets() {
    logger.debug("Unloading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.unloadAssets(forestTextures);
    resourceService.unloadAssets(forestTextureAtlases);
    resourceService.unloadAssets(forestSounds);
    for (String[] sounds : soundArrays) {
      resourceService.unloadAssets(sounds);
    }
    resourceService.unloadAssets(forestMusic);
    resourceService.unloadAssets(heartbeatSound);
  }

  @Override
  public void dispose() {
    super.dispose();
    ServiceLocator.getResourceService().getAsset(BACKGROUND_MUSIC, Music.class).stop();
    this.unloadAssets();
  }
}