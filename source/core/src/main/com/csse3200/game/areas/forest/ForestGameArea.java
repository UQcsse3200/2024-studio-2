package com.csse3200.game.areas.forest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import com.csse3200.game.areas.GameArea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.MapHandler.MapType;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.areas.terrain.TerrainFactory.TerrainType;
import com.csse3200.game.areas.terrain.TerrainLoader;
import com.csse3200.game.components.ProximityComponent;
import com.csse3200.game.components.gamearea.GameAreaDisplay;
import com.csse3200.game.components.inventory.InventoryComponent;
import com.csse3200.game.components.inventory.PlayerInventoryDisplay;
import com.csse3200.game.components.quests.QuestManager;
import com.csse3200.game.components.quests.QuestPopup;
import com.csse3200.game.components.settingsmenu.UserSettings;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.BossFactory;
import com.csse3200.game.entities.factories.EnemyFactory;
import com.csse3200.game.entities.factories.ItemFactory;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.entities.factories.ObstacleFactory;
import com.csse3200.game.entities.factories.PlayerFactory;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.services.AudioManager;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.utils.math.GridPoint2Utils;
import com.csse3200.game.utils.math.RandomUtils;

import static com.badlogic.gdx.math.MathUtils.random;

/** Forest area for the demo game with trees, a player, and some enemies. */
public class ForestGameArea extends GameArea {
    private static final Logger logger = LoggerFactory.getLogger(ForestGameArea.class);

    // INFO: The Map is equally divided into three areas. Each area is 160x48 tiles wide.
    private static final GridPoint2 AREA_SIZE = new GridPoint2(10, 3); // modify this to change the dimension of the number of chunk in the area
    public static final GridPoint2 MAP_SIZE = new GridPoint2(16 * AREA_SIZE.x, 16 * AREA_SIZE.y * 3);
    private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(MAP_SIZE.x / 2, 10);
    private static final float WALL_LENGTH = 0.1f;
    private static final String UNLOCK_AREA_EVENT = "unlockArea";
    private final TerrainFactory terrainFactory;
    private final ArrayList<Entity> area1To2 = new ArrayList<>();
    private final ArrayList<Entity> area2To3 = new ArrayList<>();

    private final List<Entity> enemies;

    private final List<Entity> bosses;

    private final List<Entity> friendlyNPCs;

    private final List<Entity> minigameNPCs;
    private final Map<Integer, Entity> dynamicItems = new HashMap<>();
    private int totalForestItems = 0;
    private int totalOceanItems = 0;
    private int totalAirItems = 0;
    private Entity player;

    private final GdxGame game;

    // Boolean to ensure that only a single boss entity is spawned when a trigger happens
    private boolean kangarooBossSpawned = false;
    private boolean waterBossSpawned = false;
    private boolean airBossSpawned = false;

    /**
     * Initialise this ForestGameArea to use the provided TerrainFactory.
     * Precondition: terrainFactory is not null
     *
     * @param terrainFactory TerrainFactory used to create the terrain for the GameArea.
     * @param game           GdxGame needed for creating the player
     */
    public ForestGameArea(TerrainFactory terrainFactory, GdxGame game) {
        super();
        this.enemies = new ArrayList<>();
        this.bosses = new ArrayList<>();
        this.friendlyNPCs = new ArrayList<>();
        this.minigameNPCs = new ArrayList<>();
        this.terrainFactory = terrainFactory;
        this.game = game;
    }

    /**
     * Create the game area, including terrain, static entities (trees),
     * dynamic entities (player and NPCs), and ui
     */
    @Override
    public void create() {
        loadAssets();

        displayUI();

        // Terrain
        spawnTerrain();

        // Player
        player = spawnPlayer();
        logger.debug("Player is at ({}, {})", player.getPosition().x, player.getPosition().y);
        TerrainLoader.setInitials(player.getPosition(), terrain);

        // Obstacles
        spawnTrees();
        spawnClouds();
        spawnSeaweed();
        spawnStarfish();

        // spawn area barriers
        spawnWorldBarrier();
        spawnFirstBarrier();
        spawnSecondBarrier();

        //Enemies
        spawnEnemies();

        // items
        handleItems("Water", "Air");

        //Friendlies
        spawnFriendlyNPCs();

        playMusic();
        player.getEvents().addListener("setPosition", this::handleNewChunks);
        player.getEvents().addListener("spawnLandBoss", this::spawnKangarooBoss);
        player.getEvents().addListener("spawnWaterBoss", this::spawnWaterBoss);
        player.getEvents().addListener("spawnAirBoss", this::spawnAirBoss);
        player.getEvents().addListener("unlockArea", this::unlockArea);
        kangarooBossSpawned = false;
        waterBossSpawned = false;
        airBossSpawned = false;

        player.getEvents().addListener("dropItems", this::spawnEntityNearPlayer);
        player.getEvents().addListener(UNLOCK_AREA_EVENT, this::unlockArea);

        //Initialise inventory and quests with loaded data
        player.getComponent(InventoryComponent.class).loadInventoryFromSave();
        player.getComponent(PlayerInventoryDisplay.class).regenerateDisplay();
        player.getComponent(QuestManager.class).loadQuests();
    }

    /**
     * Unlock an area of the map
     */
    @Override
    public void unlockArea(String area) {
        terrain.getMap().getLayers().get(area).setVisible(false);
    }

    /**
     * Spawn the world barrier
     */
    private void spawnWorldBarrier() {
        float tileSize = terrain.getTileSize();
        GridPoint2 tileBounds = terrain.getMapBounds(0);
        Vector2 worldBounds = new Vector2(tileBounds.x * tileSize, tileBounds.y * tileSize);

        // Left
        spawnEntityAt(
                ObstacleFactory.createWall(WALL_LENGTH, worldBounds.y),
                GridPoint2Utils.ZERO,
                false,
                false);
        // Right
        spawnEntityAt(
                ObstacleFactory.createWall(WALL_LENGTH, worldBounds.y),
                new GridPoint2(tileBounds.x, 0),
                false,
                false);
        // Top
        spawnEntityAt(
                ObstacleFactory.createWall(worldBounds.x, WALL_LENGTH),
                new GridPoint2(0, tileBounds.y),
                false,
                false);
        // Bottom
        spawnEntityAt(
                ObstacleFactory.createWall(worldBounds.x, WALL_LENGTH),
                GridPoint2Utils.ZERO,
                false,
                false);
    }

    /**
     * Spawns the first barrier
     */
    private void spawnFirstBarrier() {
        float tileSize = terrain.getTileSize();
        GridPoint2 tileBounds = terrain.getMapBounds(0);
        Vector2 worldBounds = new Vector2(tileBounds.x * tileSize, tileBounds.y * tileSize);

        Entity leftWall = ObstacleFactory.createVisibleWall(worldBounds.x / 2 - 2, WALL_LENGTH);
        Entity rightWall = ObstacleFactory.createVisibleWall(worldBounds.x / 2, WALL_LENGTH);
        spawnEntityAt(
                leftWall,
                new GridPoint2(0, MAP_SIZE.y / 3),
                false,
                false);

        spawnEntityAt(
                rightWall,
                new GridPoint2((int) (worldBounds.x / 2), MAP_SIZE.y / 3),
                false,
                false);
        area1To2.add(leftWall);
        area1To2.add(rightWall);
    }

    /**
     * Spawns the second barrier
     */
    private void spawnSecondBarrier() {
        float tileSize = terrain.getTileSize();
        GridPoint2 tileBounds = terrain.getMapBounds(0);
        Vector2 worldBounds = new Vector2(tileBounds.x * tileSize, tileBounds.y * tileSize);

        Entity leftWall = ObstacleFactory.createVisibleWall(worldBounds.x / 2 - 2, WALL_LENGTH);
        Entity rightWall = ObstacleFactory.createVisibleWall(worldBounds.x / 2, WALL_LENGTH);
        spawnEntityAt(
                leftWall,
                new GridPoint2(0, MAP_SIZE.y / 3 * 2),
                false,
                false);

        spawnEntityAt(
                rightWall,
                new GridPoint2((int) (worldBounds.x / 2), MAP_SIZE.y / 3 * 2),
                false,
                false);
    }

    private void handleNewChunks(Vector2 playerPos) {
        if (TerrainLoader.movedChunk(playerPos)) {
            logger.debug("Player position is: ({}, {})", playerPos.x, playerPos.y);
            handleItems("Water", "Air");
        }
    }

    private void handleItems(String ocean, String air) {
        // Spawn items on new chunks
        for (GridPoint2 pos : terrain.getNewChunks()) {
            spawnForestItems();
            if (terrain.getMap().getLayers().get(ocean).isVisible()){
                spawnOceanItems();
            }
            if (terrain.getMap().getLayers().get(air).isVisible()){
                spawnAirItems();
            }
        }


        // TODO: De-spawn items on old chunks:
        List<Integer> removals = new ArrayList<>();
        for (int key : dynamicItems.keySet()) {
            GridPoint2 chunkPos = TerrainLoader.posToChunk(dynamicItems.get(key).getPosition());
            if (!terrain.getActiveChunks().contains(chunkPos)) {
                removals.add(key);
            }
        }

        for (int i : removals) {
            dynamicItems.remove(i);
        }
    }

    /**
     * gets the player
     *
     * @return player entity
     */
    @Override
    public Entity getPlayer() {
        return player;
    }

    public void displayUI() {
        Entity ui = new Entity();

        // Create the necessary objects to pass to GameAreaDisplay
        SpriteBatch batch = new SpriteBatch(); // Ensure you have a valid SpriteBatch
        OrthographicCamera mainCamera = new OrthographicCamera(); // Initialize your main camera appropriately
        // Make sure to get the correct CameraComponent type required by GameAreaDisplay

        // Pass the required parameters to the GameAreaDisplay constructor
        ui.addComponent(new GameAreaDisplay("Box Forest"));
        ui.addComponent(new QuestPopup());
        spawnEntity(ui);
    }

    private void spawnTerrain() {
        // Background terrain
        this.terrain = terrainFactory.createTerrain(TerrainType.FOREST_DEMO, PLAYER_SPAWN, MAP_SIZE, MapType.FOREST);
        Entity terrain = new Entity().addComponent(this.terrain);

        spawnEntity(terrain);
    }

    private void spawnTrees() {
        GridPoint2 minPos = new GridPoint2(PLAYER_SPAWN.x - 10, PLAYER_SPAWN.y - 10);
        GridPoint2 maxPos = new GridPoint2(PLAYER_SPAWN.x + 10, PLAYER_SPAWN.y + 10);

        for (int i = 0; i < ForestSpawnConfig.NUM_TREES; i++) {
            GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
            Entity tree = ObstacleFactory.createTree();
            spawnEntityAt(tree, randomPos, true, false);
        }
    }

    // Spawn Cloud Obstacles
    private void spawnClouds() {
        GridPoint2 minPos = new GridPoint2(PLAYER_SPAWN.x - 10, PLAYER_SPAWN.y - 10);
        GridPoint2 maxPos = new GridPoint2(PLAYER_SPAWN.x + 10, PLAYER_SPAWN.y + 10);

        for (int i = 0; i < ForestSpawnConfig.NUM_CLOUDS; i++) {
            GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
            Entity cloud = ObstacleFactory.createCloud();
            spawnEntityAt(cloud, randomPos, true, false);
        }
    }

    // Spawn Seaweed Obstacles
    private void spawnSeaweed() {
        GridPoint2 minPos = new GridPoint2(PLAYER_SPAWN.x - 10, PLAYER_SPAWN.y - 10);
        GridPoint2 maxPos = new GridPoint2(PLAYER_SPAWN.x + 10, PLAYER_SPAWN.y + 10);

        for (int i = 0; i < ForestSpawnConfig.NUM_SEAWEED; i++) {
            GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
            Entity seaweed = ObstacleFactory.createSeaweed();
            spawnEntityAt(seaweed, randomPos, true, false);
        }
    }

    //Spawn Starfish Obstacle
    private void spawnStarfish() {
        GridPoint2 minPos = new GridPoint2(PLAYER_SPAWN.x - 10, PLAYER_SPAWN.y - 10);
        GridPoint2 maxPos = new GridPoint2(PLAYER_SPAWN.x + 10, PLAYER_SPAWN.y + 10);

        for (int i = 0; i < ForestSpawnConfig.NUM_STARFISH; i++) {
            GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
            Entity starfish = ObstacleFactory.createStarfish();
            spawnEntityAt(starfish, randomPos, true, false);
        }
    }

    /**
     * Creates the player entity for this screen
     *
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
            Entity kangarooBoss = BossFactory.createKangaBossEntity(player);
            kangarooBoss.getEvents().addListener("spawnJoey", this::spawnJoeyEnemy);
            spawnEntityOnMap(kangarooBoss);
            bosses.add(kangarooBoss);
            kangarooBossSpawned = true;
        }
    }

    private void spawnWaterBoss() {
        if (!waterBossSpawned) {
            Entity waterBoss = BossFactory.createWaterBossEntity(player);
            waterBoss.getEvents().addListener("spawnWaterSpiral", this::spawnWaterSpiral);
            spawnEntityOnMap(waterBoss);
            bosses.add(waterBoss);
            waterBossSpawned = true;
        }
    }

    private void spawnAirBoss() {
        if (!airBossSpawned) {
            Entity airBoss = BossFactory.createAirBossEntity(player);
            airBoss.getEvents().addListener("spawnWindGust", this::spawnWindGust);
            spawnEntityOnMap(airBoss);
            bosses.add(airBoss);
            airBossSpawned = true;
        }
    }

    private void spawnEntityOnMap(Entity entity) {
        GridPoint2 minPos = new GridPoint2(PLAYER_SPAWN.x - 15, PLAYER_SPAWN.y - 15);
        GridPoint2 maxPos = new GridPoint2(PLAYER_SPAWN.x + 15, PLAYER_SPAWN.y + 15);

        GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
        // Prevent spawn camping
        while (Math.abs(randomPos.x - PLAYER_SPAWN.x) <= 5 && Math.abs(randomPos.y - PLAYER_SPAWN.y) <= 5) {
            randomPos = RandomUtils.random(minPos, maxPos);
        }

        spawnEntityAt(entity, randomPos, true, true);
    }

    /**
     * Spawns an entity near the player within a specified radius, ensuring the entity
     * is placed within the correct chunk boundaries and loaded areas of the game map.
     * This function calculates a valid spawn position near the player's current location,
     * considering the player's world position and current chunk. It ensures that the entity
     * is spawned within the boundaries of the current chunk to avoid positioning the entity
     * in an unloaded or inaccessible area.
     *
     * @param entity The entity to be spawned near the player.
     * @param radius The radius around the player's position within which the entity will be spawned.
     *               The spawn position is randomly selected within this radius but is constrained
     *               to be within the current chunk boundaries.
     */
    private void spawnEntityNearPlayer(Entity entity, int radius) {
        // Get the player's current position in the world
        Vector2 playerWorldPos = player.getPosition();

        // Convert player's position to chunk coordinates
        GridPoint2 playerChunk = TerrainLoader.posToChunk(playerWorldPos);

        // Calculate potential spawn positions within the specified radius
        GridPoint2 minPos = new GridPoint2(
                Math.max(playerChunk.x * TerrainFactory.CHUNK_SIZE, (int) playerWorldPos.x - radius),
                Math.max(playerChunk.y * TerrainFactory.CHUNK_SIZE, (int) playerWorldPos.y - radius)
        );

        GridPoint2 maxPos = new GridPoint2(
                Math.min((playerChunk.x + 1) * TerrainFactory.CHUNK_SIZE - 1, (int) playerWorldPos.x + radius),
                Math.min((playerChunk.y + 1) * TerrainFactory.CHUNK_SIZE - 1, (int) playerWorldPos.y + radius)
        );

        // Randomly select a position within the radius
        GridPoint2 spawnPos = RandomUtils.random(minPos, maxPos);

        // Spawn the entity at the calculated position
        spawnEntityAt(entity, spawnPos, true, true);
        logger.debug("Spawned entity {} near player at chunk ({}, {}) at world position ({}, {})",
                entity, playerChunk.x, playerChunk.y, spawnPos.x, spawnPos.y);
    }

    /**
     * Spawns the air items on the map. Each item will have different spawning rate depending on
     * how useful their effects are.
     */
    private void spawnAirItems() {
        Supplier<Entity> generator;
        // Health Potions
        if (random.nextFloat() <= 0.40) { // Spawn rate is 40%
            generator = () -> ItemFactory.createHealthPotion(player);
            spawnFixedItems(generator, ForestSpawnConfig.NUM_HEALTH_POTIONS, 3);
        }

        // Defense Potions
        if (random.nextFloat() <= 0.15) {
            generator = () -> ItemFactory.createDefensePotion(player);
            spawnFixedItems(generator, ForestSpawnConfig.NUM_DEFENSE_POTIONS, 3);
        }

        // Attack Potions
        if (random.nextFloat() <= 0.15) {
            generator = () -> ItemFactory.createAttackPotion(player);
            spawnFixedItems(generator, ForestSpawnConfig.NUM_ATTACK_POTIONS, 3);
        }

        // Speed Potions
        if (random.nextFloat() <= 0.20) {
            generator = () -> ItemFactory.createSpeedPotion(player);
            spawnFixedItems(generator, ForestSpawnConfig.NUM_SPEED_POTIONS, 3);
        }

        // Apples
        if (random.nextFloat() <= 0.50) {
            generator = () -> ItemFactory.createApple(player);
            spawnFixedItems(generator, ForestSpawnConfig.NUM_APPLES, 3);
        }

        // Cloud Cookies
        if (random.nextFloat() <= 0.50) {
            generator = () -> ItemFactory.createCloudCookie(player);
            spawnFixedItems(generator, ForestSpawnConfig.NUM_CLOUD_COOKIES, 3);
        }

        // Cloud Cupcake
        if (random.nextFloat() <= 0.35) {
            generator = () -> ItemFactory.createCloudCupcakes(player);
            spawnFixedItems(generator, ForestSpawnConfig.NUM_CLOUD_CUPCAKES, 3);
        }

        // Cotton Cloud
        if (random.nextFloat() <= 0.25) {
            generator = () -> ItemFactory.createCottonCLoud(player);
            spawnFixedItems(generator, ForestSpawnConfig.NUM_COTTON_CLOUD, 3);
        }
    }


    /**
     * Spawns the ocean items on the map. Each item will have different spawning rate depending on
     * how useful their effects are.
     */
    private void spawnOceanItems() {
        Supplier<Entity> generator;

        // Health Potions
        if (random.nextFloat() <= 0.40) { // Spawn rate is 40%
            generator = () -> ItemFactory.createHealthPotion(player);
            spawnFixedItems(generator, ForestSpawnConfig.NUM_HEALTH_POTIONS, 2);
        }

        // Defense Potions
        if (random.nextFloat() <= 0.15) {
            generator = () -> ItemFactory.createDefensePotion(player);
            spawnFixedItems(generator, ForestSpawnConfig.NUM_DEFENSE_POTIONS, 2);
        }

        // Attack Potions
        if (random.nextFloat() <= 0.15) {
            generator = () -> ItemFactory.createAttackPotion(player);
            spawnFixedItems(generator, ForestSpawnConfig.NUM_ATTACK_POTIONS, 2);
        }

        // Speed Potions
        if (random.nextFloat() <= 0.20) {
            generator = () -> ItemFactory.createSpeedPotion(player);
            spawnFixedItems(generator, ForestSpawnConfig.NUM_SPEED_POTIONS, 2);
        }

        // Apples
        if (random.nextFloat() <= 0.50) {
            generator = () -> ItemFactory.createApple(player);
            spawnFixedItems(generator, ForestSpawnConfig.NUM_APPLES, 2);
        }

        // Fried Fish
        if (random.nextFloat() <= 0.40) {
            generator = () -> ItemFactory.createFriedFish(player);
            spawnFixedItems(generator, ForestSpawnConfig.NUM_FRIED_FISH, 2);
        }

        // Shrimp
        if (random.nextFloat() <= 0.40) {
            generator = () -> ItemFactory.createShrimp(player);
            spawnFixedItems(generator, ForestSpawnConfig.NUM_SHRIMP, 2);
        }
    }

    private void spawnForestItems() {
        Supplier<Entity> generator;

    // Health Potions
      if (random.nextFloat() <= 0.30) {
          generator = () -> ItemFactory.createHealthPotion(player);
          spawnFixedItems(generator, ForestSpawnConfig.NUM_HEALTH_POTIONS, 1);
      }

      // Defense Potions
      if (random.nextFloat() <= 0.10) {
          generator = () -> ItemFactory.createDefensePotion(player);
          spawnFixedItems(generator, ForestSpawnConfig.NUM_DEFENSE_POTIONS, 1);
      }

      // Attack Potions
      if (random.nextFloat() <= 0.10) {
          generator = () -> ItemFactory.createAttackPotion(player);
          spawnFixedItems(generator, ForestSpawnConfig.NUM_ATTACK_POTIONS, 1);
      }

      // Speed Potions
      if (random.nextFloat() <= 0.20) {
          generator = () -> ItemFactory.createSpeedPotion(player);
          spawnFixedItems(generator, ForestSpawnConfig.NUM_SPEED_POTIONS, 1);
      }

      // Apples
      if (random.nextFloat() <= 0.40) {
          generator = () -> ItemFactory.createApple(player);
          spawnFixedItems(generator, ForestSpawnConfig.NUM_APPLES, 1);
      }

      // Carrots
      if (random.nextFloat() <= 0.25) {
          generator = () -> ItemFactory.createCarrot(player);
          spawnFixedItems(generator, ForestSpawnConfig.NUM_CARROTS, 1);
      }

      // Meat
      if (random.nextFloat() <= 0.20) {
          generator = () -> ItemFactory.createMeat(player);
          spawnFixedItems(generator, ForestSpawnConfig.NUM_MEAT, 1);
      }

      // Chicken Legs
      if (random.nextFloat() <= 0.20) {
          generator = () -> ItemFactory.createChickenLeg(player);
          spawnFixedItems(generator, ForestSpawnConfig.NUM_CHICKEN_LEGS, 1);
      }

      // Candy
      if (random.nextFloat() <= 0.10) {
          generator = () -> ItemFactory.createCandy(player);
          spawnFixedItems(generator, ForestSpawnConfig.NUM_CANDY, 1);
      }
    }

    private void spawnEnemies() {
        Supplier<Entity> generator;

        // Chicken
        generator = () -> EnemyFactory.createChicken(player);
        spawnRandomEnemy(generator, ForestSpawnConfig.NUM_CHICKENS, 0.05, 1);

        // Monkey
        generator = () -> EnemyFactory.createMonkey(player);
        spawnShooterEnemy(generator, ForestSpawnConfig.NUM_MONKEYS, 0.04, 1);

        // Pigeon
        generator = () -> EnemyFactory.createPigeon(player);
        spawnRandomEnemy(generator, ForestSpawnConfig.NUM_PIGEONS, 0.06, 3);

        // Frog
        generator = () -> EnemyFactory.createFrog(player);
        spawnRandomEnemy(generator, ForestSpawnConfig.NUM_FROGS, 0.06, 2);

        //Bear
        generator = () -> EnemyFactory.createBear(player);
        spawnRandomEnemy(generator, ForestSpawnConfig.NUM_BEARS, 0.1, 1);

        //Bee
        generator = () -> EnemyFactory.createBee(player);
        spawnRandomEnemy(generator, ForestSpawnConfig.NUM_BEES, 0.1, 3);

        //Eel
        generator = () -> EnemyFactory.createEel(player);
        spawnShooterEnemy(generator, ForestSpawnConfig.NUM_EELS, 0.1, 2);

        //Octopus
        generator = () -> EnemyFactory.createOctopus(player);
        spawnRandomEnemy(generator, ForestSpawnConfig.NUM_OCTOPUS, 0.06, 2);

        //Big saw fish
        generator = () -> EnemyFactory.createBigsawfish(player);
        spawnShooterEnemy(generator, ForestSpawnConfig.NUM_BIGSAWFISH, 0.1, 2);

        //Macaw
        generator = () -> EnemyFactory.createMacaw(player);
        spawnShooterEnemy(generator, ForestSpawnConfig.NUM_MACAW, 0.1, 3);

        //Hive
        generator = () -> ProjectileFactory.createHive(player);
        spawnHive(generator, 5, 0.1, 1);
    }

    /**
     * Spawns the friendly NPCs onto the map
     */
    private void spawnFriendlyNPCs() {
        Supplier<Entity> generator;

        // Cow
        generator = () -> NPCFactory.createCow(player, this.enemies);
        spawnRandomNPC(generator, ForestSpawnConfig.NUM_COWS);

        // Fish
        generator = () -> NPCFactory.createFish(player, this.enemies);
        spawnMinigameNPC(generator, ForestSpawnConfig.NUM_FISH);

        // Lion
        generator = () -> NPCFactory.createLion(player, this.enemies);
        spawnRandomNPC(generator, ForestSpawnConfig.NUM_LIONS);

        // Turtle
        generator = () -> NPCFactory.createTurtle(player, this.enemies);
        spawnRandomNPC(generator, ForestSpawnConfig.NUM_TURTLES);

        // Eagle
        generator = () -> NPCFactory.createEagle(player, this.enemies);
        spawnRandomNPC(generator, ForestSpawnConfig.NUM_EAGLES);

        // Snake
        generator = () -> NPCFactory.createSnake(player, this.enemies);
        spawnMinigameNPC(generator, ForestSpawnConfig.NUM_SNAKES);

        // Magpie
        generator = () -> NPCFactory.createMagpie(player, this.enemies);
        spawnMinigameNPC(generator, ForestSpawnConfig.NUM_MAGPIES);
    }

    /**
     * Spawns defeated enemy NPCs are friendly NPCs in the same/similar location
     *
     * @param defeatedEnemy the entity that has been defeated in combat
     */
    @Override
    public void spawnConvertedNPCs(Entity defeatedEnemy) {
        loadAssets();
        if (defeatedEnemy == null || defeatedEnemy.getEnemyType() == null) {
            logger.warn("Attempted to convert null entity or entity with null enemy type");
            return;
        }

        Vector2 pos = calculateSpawnPosition(defeatedEnemy);
        Entity convertedNPC; // defaults to null

        switch (defeatedEnemy.getEnemyType()) {
            case CHICKEN:
                convertedNPC = NPCFactory.createChicken(player, this.enemies);
                break;
            case FROG:
                convertedNPC = NPCFactory.createFrog(player, this.enemies);
                break;
            case MONKEY:
                convertedNPC = NPCFactory.createMonkey(player, this.enemies);
                break;
            case BEAR:
                convertedNPC = NPCFactory.createBear(player, this.enemies);
                break;
            // Add other enemy types as needed
            default:
                return;
        }

        spawnEntityAtVector(convertedNPC, pos);
        convertedNPC.getEvents().trigger("wanderStart");
    }

    /**
     * Calculates a spawn position for an entity relative to the player's position.
     * The position is adjusted based on the entity's current position in comparison to the player's
     * and is clamped to ensure it stays within the map boundaries.
     *
     * @param entity the entity for which the spawn position is being calculated.
     * @return a representing the new spawn position within the map bounds.
     */
    private Vector2 calculateSpawnPosition(Entity entity) {
        // Use the same logic as in spawnBanana method
        float spawnX = (entity.getPosition().x - player.getPosition().x) > 0 ? -1 : 1;
        float spawnY = (entity.getPosition().y - player.getPosition().y) > 0 ? 1 : -1;

        // Calculate the new position using Vector2
        Vector2 pos = new Vector2(entity.getPosition().x + spawnX, entity.getPosition().y + spawnY);

        // Ensure the position is within map bounds
        pos.x = Math.clamp(pos.x, 0, MAP_SIZE.x);
        pos.y = Math.clamp(pos.y, 0, MAP_SIZE.y);

		return pos;
	}

    /**
     * Spawns the items randomly at a fixed position across the map
     */
    private void spawnFixedItems(Supplier<Entity> creator, int numItems, int zone) {
        if (zone == 1 && totalForestItems < 50) {
            int forestItemsToSpawn = Math.min(numItems, 50 - totalForestItems);
            GridPoint2 minPos = new GridPoint2(0, AREA_SIZE.y * 16 * (zone - 1));
            GridPoint2 maxPos = new GridPoint2(AREA_SIZE.x * 16, AREA_SIZE.y * 16 * zone);

            for (int i = 0; i < forestItemsToSpawn; i++) {
                GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
                Entity item = creator.get();
                spawnEntityAt(item, randomPos, true, false);
                dynamicItems.put(totalForestItems, item);
                totalForestItems++;
            }
        } else if (zone == 2 && totalForestItems < 50) {
            int oceamItemsToSpawn = Math.min(numItems, 50 - totalForestItems);
            GridPoint2 minPos = new GridPoint2(0, AREA_SIZE.y * 16 * (zone - 1));
            GridPoint2 maxPos = new GridPoint2(AREA_SIZE.x * 16, AREA_SIZE.y * 16 * zone);

            for (int i = 0; i < oceamItemsToSpawn; i++) {
                GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
                Entity item = creator.get();
                spawnEntityAt(item, randomPos, true, false);
                dynamicItems.put(totalOceanItems, item);
                totalOceanItems++;
            }
        } else if (zone == 3 && totalAirItems < 50) {
            int airItemsToSpawn = Math.min(numItems, 50 - totalAirItems);
            GridPoint2 minPos = new GridPoint2(0, AREA_SIZE.y * 16 * (zone - 1));
            GridPoint2 maxPos = new GridPoint2(AREA_SIZE.x * 16, AREA_SIZE.y * 16 * zone);

            for (int i = 0; i < airItemsToSpawn; i++) {
                GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
                Entity item = creator.get();
                spawnEntityAt(item, randomPos, true, false);
                dynamicItems.put(totalAirItems, item);
                totalAirItems++;
            }
        }
    }

    private void spawnRandomNPC(Supplier<Entity> creator, int numNPCs) {
        GridPoint2 minPos = new GridPoint2(PLAYER_SPAWN.x - 10, PLAYER_SPAWN.y - 10);
        GridPoint2 maxPos = new GridPoint2(PLAYER_SPAWN.x + 10, PLAYER_SPAWN.y + 10);

        for (int i = 0; i < numNPCs; i++) {
            GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
            Entity npc = creator.get();
            friendlyNPCs.add(npc);
            spawnEntityAt(npc, randomPos, true, false);
        }
    }


    public void spawnRandomObject(){

    }



    private void spawnMinigameNPC(Supplier<Entity> creator, int numNPCs) {
        GridPoint2 minPos = new GridPoint2(PLAYER_SPAWN.x - 10, PLAYER_SPAWN.y - 10);
        GridPoint2 maxPos = new GridPoint2(PLAYER_SPAWN.x + 10, PLAYER_SPAWN.y + 10);

        for (int i = 0; i < numNPCs; i++) {
            GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
            Entity npc = creator.get();
            minigameNPCs.add(npc);
            spawnEntityAt(npc, randomPos, true, false);
        }
    }

    public void playMusic() {
        // Get the selected music track from the user settings
        UserSettings.Settings settings = UserSettings.get();
        String selectedTrack = settings.selectedMusicTrack;  // This will be "Track 1" or "Track 2"

        if (Objects.equals(selectedTrack, "Track 1")) {
            loadAssets(); // Music was not loading after resuming combat
            AudioManager.playMusic("sounds/BGM_03_mp3.mp3", true);
        } else if (Objects.equals(selectedTrack, "Track 2")) {
            AudioManager.playMusic("sounds/track_2.mp3", true);
        }
    }

    public void pauseMusic() {
        AudioManager.stopMusic();  // Stop the music
    }

    private void spawnRandomEnemy(Supplier<Entity> creator, int numItems, double proximityRange, int zone) {
        GridPoint2 minPos = new GridPoint2(0, AREA_SIZE.y * 16 * (zone - 1));
        GridPoint2 maxPos = new GridPoint2(AREA_SIZE.x * 16, AREA_SIZE.y * 16 * zone);

        for (int i = 0; i < numItems; i++) {
            GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
            Entity enemy = creator.get();
            spawnEntityAt(enemy, randomPos, true, false);
            enemies.add(enemy);
            enemy.addComponent(new ProximityComponent(player, proximityRange)); // Add ProximityComponent
        }
    }

    private void spawnShooterEnemy(Supplier<Entity> creator, int numItems, double proximityRange, int zone) {
        GridPoint2 minPos = new GridPoint2(0, AREA_SIZE.y * 16 * (zone - 1));
        GridPoint2 maxPos = new GridPoint2(AREA_SIZE.x * 16, AREA_SIZE.y * 16 * zone);

        for (int i = 0; i < numItems; i++) {
            GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
            Entity enemy = creator.get();
            spawnEntityAt(enemy, randomPos, true, false);
            enemies.add(enemy);
            enemy.addComponent(new ProximityComponent(player, proximityRange)); // Add ProximityComponent
            enemy.getEvents().addListener("Shoot", this::spawnProjectile);
        }
    }

    private void spawnProjectile(Entity enemy, Entity.EnemyType name) {
        Entity projectile = switch (name) {
            case MONKEY -> ProjectileFactory.createBanana(player);
            case EEL -> ProjectileFactory.createElectricOrb(player);
            case MACAW -> ProjectileFactory.createWorm(player);
            default -> {
                logger.warn("Attempting to add a projectile to an Entity that may not support it");
                yield ProjectileFactory.createBanana(player);
            }
        };

        // Calculate bananaX and bananaY based on target's relative position
        float bananaX = (enemy.getPosition().x - player.getPosition().x) > 0 ? -1 : 1;
        float bananaY = (enemy.getPosition().y - player.getPosition().y) > 0 ? 1 : -1;

        // Calculate the new position using Vector2
        Vector2 pos = new Vector2(enemy.getPosition().x + bananaX, enemy.getPosition().y + bananaY);

        //spawns
        spawnEntityAtVector(projectile, pos);
    }

    private void spawnHive(Supplier<Entity> creator, int numHives, double proximityRange, int zone) {
        GridPoint2 minPos = new GridPoint2(0, AREA_SIZE.y * 16 * (zone - 1));
        GridPoint2 maxPos = new GridPoint2(AREA_SIZE.x * 16, AREA_SIZE.y * 16 * zone);

        for (int i = 0; i < numHives; i++) {
            GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
            Entity enemy = creator.get();
            spawnEntityAt(enemy, randomPos, true, false);
            enemies.add(enemy);
            enemy.addComponent(new ProximityComponent(player, proximityRange));
            enemy.getEvents().addListener("spawnBee", this::spawnBee);
        }
    }

    private void spawnBee(Entity bee, Vector2 pos) {
        spawnEntityAtVector(bee, pos);
        enemies.add(bee);
    }

    private void spawnJoeyEnemy(Entity kangaroo) {
        if (kangaroo != null) {
            Entity joey = EnemyFactory.createJoey(player);

            Vector2 kangarooBossPos = kangaroo.getPosition();

            // Define the area around the Kangaroo boss where the Joey can be spawned
            GridPoint2 minPos = new GridPoint2((int) kangarooBossPos.x - 2, (int) kangarooBossPos.y - 2);
            GridPoint2 maxPos = new GridPoint2((int) kangarooBossPos.x + 2, (int) kangarooBossPos.y + 2);

            GridPoint2 spawnPos = RandomUtils.random(minPos, maxPos);

            spawnEntityAt(joey, spawnPos, true, false);
            enemies.add(joey);
        }
    }

    private void spawnWaterSpiral(Entity boss) {
        if (boss != null) {
            Entity waterSpiral = ProjectileFactory.createWaterSpiral(player);

            float posX = (boss.getPosition().x - player.getPosition().x) > 0 ? -1 : 1;
            float posY = (boss.getPosition().y - player.getPosition().y) > 0 ? 1 : -1;

            Vector2 pos = new Vector2(boss.getPosition().x + posX, boss.getPosition().y + posY);

            spawnEntityAtVector(waterSpiral, pos);
        }
    }

    private void spawnWindGust(Entity boss) {
        if (boss != null) {
            Entity windGust = ProjectileFactory.createWindGust(player);

            float posX = (boss.getPosition().x - player.getPosition().x) > 0 ? -1 : 1;
            float posY = (boss.getPosition().y - player.getPosition().y) > 0 ? 1 : -1;

            Vector2 pos = new Vector2(boss.getPosition().x + posX, boss.getPosition().y + posY);

            spawnEntityAtVector(windGust, pos);
        }
    }

    /**
     * Static method to play the background music
     */
    public static void pMusic() {
        Music music = ServiceLocator.getResourceService().getAsset(ForestSoundsConfig.BACKGROUND_MUSIC,
                Music.class);
        music.setLooping(true);
        music.setVolume(0.5f);
        music.play();
    }

    /**
     * Static method to pause the background music
     */
    public static void puMusic() {
        Music music = ServiceLocator.getResourceService().getAsset(ForestSoundsConfig.BACKGROUND_MUSIC, Music.class);
        music.pause();
    }

    public void loadAssets() {
        logger.debug("LOADING ASSETS");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadMusic(new String[]{"sounds/BGM_03_mp3.mp3", "sounds/track_2.mp3"});

        resourceService.loadTextures(ForestTexturesConfig.FOREST_TEXTURES);
        resourceService.loadTextureAtlases(ForestTexturesConfig.FOREST_TEXTURE_ATLASES);
        resourceService.loadSounds(ForestSoundsConfig.GAME_SOUNDS);
        resourceService.loadMusic(ForestSoundsConfig.GAME_MUSIC);
        resourceService.loadSounds(ForestSoundsConfig.CHARACTER_SOUNDS);
        while (!resourceService.loadForMillis(10)) {
            // This could be upgraded to a loading screen
            logger.debug("Loading... {}%", resourceService.getProgress());
        }
    }

    @Override
    public void unloadAssets() {
        logger.debug("UNLOADING ASSETS");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(ForestTexturesConfig.FOREST_TEXTURES);
        resourceService.unloadAssets(ForestTexturesConfig.FOREST_TEXTURE_ATLASES);
        resourceService.unloadAssets(ForestSoundsConfig.GAME_SOUNDS);
        resourceService.unloadAssets(ForestSoundsConfig.GAME_MUSIC);
    }

    @Override
    public void dispose() {
        super.dispose();
        ServiceLocator.getResourceService().getAsset(ForestSoundsConfig.BACKGROUND_MUSIC, Music.class).stop();
        this.unloadAssets();
    }

    public List<Entity> getEnemies() {
        return enemies;
    }

    public List<Entity> getBosses() {
        return bosses;
    }

    public List<Entity> getFriendlyNPCs() {
        return friendlyNPCs;
    }

    public List<Entity> getMinigameNPCs() {
        return minigameNPCs;
    }

    public Map<Integer, Entity> getDynamicItems() {
        return dynamicItems;
    }

}
