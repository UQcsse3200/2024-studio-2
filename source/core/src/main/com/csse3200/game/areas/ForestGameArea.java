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
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.entities.factories.*;
import com.csse3200.game.areas.terrain.TerrainLoader;
import com.csse3200.game.utils.math.RandomUtils;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.components.gamearea.GameAreaDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;
import java.util.function.Supplier;


/** Forest area for the demo game with trees, a player, and some enemies. */
public class ForestGameArea extends GameArea {
  private static final Logger logger = LoggerFactory.getLogger(ForestGameArea.class);
  private static final ForestGameAreaConfig config = new ForestGameAreaConfig();
  private static final GridPoint2 MAP_SIZE = new GridPoint2(5000, 5000);
  private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(2500, 2500);
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
          "images/terrain_iso_grass.atlas", "images/chicken.atlas", "images/frog.atlas",
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
  private final Map<Integer, Entity> dynamicItems = new HashMap<>();
  private int totalItems = 0;
  private Entity player;
  private final GdxGame game;
  private boolean kangarooBossSpawned = false;

  public ForestGameArea(TerrainFactory terrainFactory, GdxGame game) {
    super();
    this.enemies = new ArrayList<>();
    this.terrainFactory = terrainFactory;
    this.game = game;
  }

  @Override
  public void create() {
    loadAssets();
    displayUI();


    // Terrain
    spawnTerrain();

    // Player
    player = spawnPlayer();
    logger.debug("Player is at ({}, {})", player.getPosition().x, player.getPosition().y);
    TerrainLoader.setChunk(player.getPosition());

    // Obstacles
    spawnTrees();



    // Friendlies
    spawnCow();
    spawnLion();
    spawnTurtle();
    spawnEagle();
    spawnSnake();

    //Enemies
    spawnEnemies();

    // items
    handleItems();

    //Friendlies
    spawnFriendlyNPCs();


    playMusic();
    player.getEvents().addListener("setPosition", this::handleNewChunks);
    player.getEvents().addListener("spawnKangaBoss", this::spawnKangarooBoss);
    kangarooBossSpawned = false;
  }


  private void handleNewChunks(Vector2 playerPos) {
    if (TerrainLoader.movedChunk(playerPos)) {
      logger.info("Player position is: ({}, {})", playerPos.x, playerPos.y);
      handleItems();
    }
  }

  private void handleItems() {
    // Spawn items on new chunks: TODO: ADD THIS TO A LIST OF DYNAMIC ENTITIES IN SPAWNER!
    for (GridPoint2 pos : TerrainComponent.getNewChunks()) {
      spawnItems(TerrainLoader.chunktoWorldPos(pos));
    }

    // Despawn items on old chunks:
    // TODO: WE CAN DO THIS EFFICIENTLY BY STORING THE SET OF ITEMS IN AN AVL TREE ORDERED BY
    //  POSITION, AND THEN CAN JUST CHECK FOR ANYTHING SPAWNED OUTSIDE THE PLAYER RADIUS (AND
    //  PROVIDED THE RADIUS IS BIG ENOUGH IT ALSO WON'T MATTER FOR DYNAMIC NPC's IF THEY WANDER
    //  ONTO THE CHUNK)
    List<Integer> removals = new ArrayList<>();
    for (int key : dynamicItems.keySet()) {
      GridPoint2 chunkPos = TerrainLoader.posToChunk(dynamicItems.get(key).getPosition());
      if (!TerrainComponent.getActiveChunks().contains(chunkPos)) {
        removals.add(key);
      }
    }
    for (int i : removals) {
      dynamicItems.remove(i);
    }
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
    terrain = terrainFactory.createTerrain(TerrainType.FOREST_DEMO, PLAYER_SPAWN, MAP_SIZE);
    spawnEntity(new Entity().addComponent(terrain));
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

  private void spawnChicken() {
    GridPoint2 minPos = new GridPoint2(PLAYER_SPAWN.x - 10, PLAYER_SPAWN.y - 10);
    GridPoint2 maxPos = new GridPoint2(PLAYER_SPAWN.x + 10, PLAYER_SPAWN.y + 10);
    GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
    Entity chicken = EnemyFactory.createChicken(player);
    enemies.add(chicken);
    float proximityRange = 0.05f;
    chicken.addComponent(new ProximityComponent(player, proximityRange));
    spawnEntityAt(chicken, randomPos, true, true);
  }

  private void spawnFrog() {
    GridPoint2 minPos = new GridPoint2(PLAYER_SPAWN.x - 20, PLAYER_SPAWN.y - 10);
    GridPoint2 maxPos = new GridPoint2(PLAYER_SPAWN.x + 20, PLAYER_SPAWN.y + 10);
    GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
    Entity frog = EnemyFactory.createFrog(player);
    enemies.add(frog);
    float proximityRange = 0.05f;
    frog.addComponent(new ProximityComponent(player, proximityRange));
    spawnEntityAt(frog, randomPos, true, true);
  }

  private void spawnMonkey() {
    GridPoint2 minPos = new GridPoint2(PLAYER_SPAWN.x - 20, PLAYER_SPAWN.y - 10);
    GridPoint2 maxPos = new GridPoint2(PLAYER_SPAWN.x + 20, PLAYER_SPAWN.y + 10);
    GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
    Entity monkey = EnemyFactory.createMonkey(player);
    enemies.add(monkey);
    float proximityRange = 0.05f;
    monkey.addComponent(new ProximityComponent(player, proximityRange));
    spawnEntityAt(monkey, randomPos, true, true);
  }

  private void spawnRandomItem(GridPoint2 pos, Supplier<Entity> creator, int numItems) {
    GridPoint2 minPos = new GridPoint2(pos.x - 20, pos.y - 20);
    GridPoint2 maxPos = new GridPoint2(pos.x + 20, pos.y + 20);

    for (int i = 0; i < numItems; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      Entity item = creator.get();
      spawnEntityAt(item, randomPos, true, false);
      dynamicItems.put(totalItems, item);
      totalItems++;
    }
  }

  private void spawnItems(GridPoint2 pos) {
    Supplier<Entity> generator;

    // Health Potions
    generator = () -> ItemFactory.createHealthPotion(player);
    spawnRandomItem(pos, generator, config.spawns.NUM_HEALTH_POTIONS);

    // Defense Potions
    generator = () -> ItemFactory.createDefensePotion(player);
    spawnRandomItem(pos, generator, config.spawns.NUM_DEFENSE_POTIONS);

  // Attack potions
    generator = () -> ItemFactory.createAttackPotion(player);
    spawnRandomItem(pos, generator, config.spawns.NUM_ATTACK_POTIONS);

    // Speed potions
    generator = () -> ItemFactory.createSpeedPotion(player);
    spawnRandomItem(pos, generator, config.spawns.NUM_SPEED_POTIONS);

    // Apples
    generator = () -> ItemFactory.createApple(player);
    spawnRandomItem(pos, generator, config.spawns.NUM_APPLES);

    // Carrots
    generator = () -> ItemFactory.createCarrot(player);
    spawnRandomItem(pos, generator, config.spawns.NUM_CARROTS);

    // Meat
    generator = () -> ItemFactory.createMeat(player);
    spawnRandomItem(pos, generator, config.spawns.NUM_MEAT);

    // Chicken legs
    generator = () -> ItemFactory.createChickenLeg(player);
    spawnRandomItem(pos, generator, config.spawns.NUM_CHICKEN_LEGS);

    // Candy
    generator = () -> ItemFactory.createCandy(player);
    spawnRandomItem(pos, generator, config.spawns.NUM_CANDY);
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

  private void spawnFriendlyNPCs() {
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
      totalItems++;
      dynamicItems.put(totalItems, item);
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
    music.setVolume(0.0f);
    music.play();
  }

  public static void pauseMusic() {
    Music music = ServiceLocator.getResourceService().getAsset(config.sounds.backgroundMusic, Music.class);
    music.pause();
  }


  public void loadAssets() {
    logger.debug("LOADING ASSETS");
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
      logger.info("Loading... {}%", resourceService.getProgress());

      resourceService.loadTextures(config.textures.forestTextures);
      resourceService.loadTextureAtlases(config.textures.forestTextureAtlases);
      resourceService.loadSounds(config.sounds.gameSounds);
      resourceService.loadMusic(config.sounds.gameMusic);
      resourceService.loadSounds(config.sounds.characterSounds);

      while (!resourceService.loadForMillis(10)) {
        // This could be upgraded to a loading screen
        logger.debug("Loading... {}%", resourceService.getProgress());
      }
    }
  }

  private void unloadAssets() {
    logger.debug("Unloading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.unloadAssets(forestTextures);
    resourceService.unloadAssets(forestTextureAtlases);
    resourceService.unloadAssets(forestSounds);
    resourceService.unloadAssets(questSounds);
    resourceService.unloadAssets(forestMusic);
    resourceService.unloadAssets(heartbeatSound);

    for (String[] soundArray : soundArrays) {
      resourceService.unloadAssets(soundArray);
    }
  }


  public void dispose() {
      super.dispose();
      ServiceLocator.getResourceService().getAsset(config.sounds.backgroundMusic, Music.class).stop();
      this.unloadAssets();
    }
  }
