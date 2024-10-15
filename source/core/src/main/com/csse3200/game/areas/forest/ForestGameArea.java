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
import com.csse3200.game.areas.terrain.enums.TileLocation;

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
    private int totalItems = 0;
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
        handleItems();

        //Friendlies
        spawnFriendlyNPCs();

        playMusic();
        player.getEvents().addListener("setPosition", this::handleNewChunks);
        player.getEvents().addListener("spawnLandBoss", this::spawnKangarooBoss);
        player.getEvents().addListener("spawnWaterBoss", this::spawnWaterBoss);
        player.getEvents().addListener("spawnAirBoss", this::spawnAirBoss);
        player.getEvents().addListener(UNLOCK_AREA_EVENT, this::unlockArea);
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
            handleItems();
        }
    }

    private void handleItems() {
        // Spawn items on new chunks
        for (GridPoint2 pos : terrain.getNewChunks()) {
            spawnItems(TerrainLoader.chunktoWorldPos(pos));
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
            spawnBossOnMap(kangarooBoss);
            bosses.add(kangarooBoss);
            kangarooBossSpawned = true;
        }
    }

    private void spawnWaterBoss() {
        if (!waterBossSpawned) {
            Entity waterBoss = BossFactory.createWaterBossEntity(player);
            waterBoss.getEvents().addListener("spawnWaterSpiral", this::spawnWaterSpiral);
            spawnBossOnMap(waterBoss);
            bosses.add(waterBoss);
            waterBossSpawned = true;
        }
    }

    private void spawnAirBoss() {
        if (!airBossSpawned) {
            Entity airBoss = BossFactory.createAirBossEntity(player);
            airBoss.getEvents().addListener("spawnWindGust", this::spawnWindGust);
            spawnBossOnMap(airBoss);
            bosses.add(airBoss);
            airBossSpawned = true;
        }
    }

    private void spawnBossOnMap(Entity entity) {
        GridPoint2 minPos = null;
        GridPoint2 maxPos = null;

        float tileSize = terrain.getTileSize();
        GridPoint2 tileBounds = terrain.getMapBounds(0);
        Vector2 worldBounds = new Vector2(tileBounds.x * tileSize, tileBounds.y * tileSize);

        int minGateX = (int) (worldBounds.x / 2) - 5;
        int maxGateX = (int) (worldBounds.x / 2) + 5;

        // Define spawn areas based on the boss type
        if (entity.getEnemyType() == Entity.EnemyType.KANGAROO) {
            // Spawn near the first barrier
            minPos = new GridPoint2(minGateX, (MAP_SIZE.y / 3 )- 10);
            maxPos = new GridPoint2(maxGateX, (MAP_SIZE.y / 3) - 5);
        } else if (entity.getEnemyType() == Entity.EnemyType.WATER_BOSS) {
            // Spawn near the second barrier
            minPos = new GridPoint2(minGateX, (MAP_SIZE.y / 3 * 2) - 10);
            maxPos = new GridPoint2(maxGateX, (MAP_SIZE.y / 3 * 2) - 5);
        } else if (entity.getEnemyType() == Entity.EnemyType.AIR_BOSS) {
            // Spawn at the top of the map
            minPos = new GridPoint2(minGateX, MAP_SIZE.y - 20);
            maxPos = new GridPoint2(maxGateX, MAP_SIZE.y - 15);
        }

        if (minPos != null) {
            // Generate a random position within the range
            GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
            // Spawn the entity at the calculated random position
            spawnEntityAt(entity, randomPos, true, true);
        }
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

    private void spawnItems(GridPoint2 pos) {
        Supplier<Entity> generator;

        // Health Potions
        generator = () -> ItemFactory.createHealthPotion(player);
        spawnRandomItem(pos, generator, ForestSpawnConfig.NUM_HEALTH_POTIONS);

        // Defense Potions
        generator = () -> ItemFactory.createDefensePotion(player);
        spawnRandomItem(pos, generator, ForestSpawnConfig.NUM_DEFENSE_POTIONS);

        // Attack potions
        generator = () -> ItemFactory.createAttackPotion(player);
        spawnRandomItem(pos, generator, ForestSpawnConfig.NUM_ATTACK_POTIONS);

        // Speed potions
        generator = () -> ItemFactory.createSpeedPotion(player);
        spawnRandomItem(pos, generator, ForestSpawnConfig.NUM_SPEED_POTIONS);

        // Apples
        generator = () -> ItemFactory.createApple(player);
        spawnRandomItem(pos, generator, ForestSpawnConfig.NUM_APPLES);

        // Carrots
        generator = () -> ItemFactory.createCarrot(player);
        spawnRandomItem(pos, generator, ForestSpawnConfig.NUM_CARROTS);

        // Meat
        generator = () -> ItemFactory.createMeat(player);
        spawnRandomItem(pos, generator, ForestSpawnConfig.NUM_MEAT);

        // Chicken legs
        generator = () -> ItemFactory.createChickenLeg(player);
        spawnRandomItem(pos, generator, ForestSpawnConfig.NUM_CHICKEN_LEGS);

        // Candy
        generator = () -> ItemFactory.createCandy(player);
        spawnRandomItem(pos, generator, ForestSpawnConfig.NUM_CANDY);
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
        spawnRandomNPC(generator, ForestSpawnConfig.NUM_COWS, TileLocation.FOREST);

        // Fish
        generator = () -> NPCFactory.createFish(player, this.enemies);
        spawnMinigameNPC(generator, ForestSpawnConfig.NUM_FISH,TileLocation.WATER);

        // Lion
        generator = () -> NPCFactory.createLion(player, this.enemies);
        spawnRandomNPC(generator, ForestSpawnConfig.NUM_LIONS, TileLocation.FOREST);

        // Turtle
        generator = () -> NPCFactory.createTurtle(player, this.enemies);
        spawnRandomNPC(generator, ForestSpawnConfig.NUM_TURTLES, TileLocation.WATER);

        // Eagle
        generator = () -> NPCFactory.createEagle(player, this.enemies);
        spawnRandomNPC(generator, ForestSpawnConfig.NUM_EAGLES, TileLocation.AIR);

        // Snake
        generator = () -> NPCFactory.createSnake(player, this.enemies);
        spawnMinigameNPC(generator, ForestSpawnConfig.NUM_SNAKES, TileLocation.FOREST);

        // Magpie
        generator = () -> NPCFactory.createMagpie(player, this.enemies);
        spawnMinigameNPC(generator, ForestSpawnConfig.NUM_MAGPIES, TileLocation.AIR);

        generator = NPCFactory::createFirefly;
        spawnRandomNPC(generator, ForestSpawnConfig.NUM_FIREFLIES, TileLocation.FOREST);
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

    /**
     * Spawns a specified number of NPCs at random positions within a designated zone.
     * The NPCs are created using the provided entity supplier and spawned at a random location
     * within the defined area for the zone.
     *
     * @param creator  A supplier function that creates new NPC entities.
     * @param numNPCs  The number of NPCs to spawn.
     * @param loc     The zone within which to spawn the NPCs. The zone enum determines the vertical position range.
     */
    private void spawnRandomNPC(Supplier<Entity> creator, int numNPCs, TileLocation loc) {
        int zone = 0;
        if (loc == TileLocation.FOREST) {
            zone = 1;
        } else if (loc == TileLocation.AIR) {
            zone = 3;
        } else {
            zone = 2;
        }

        GridPoint2 minPos = new GridPoint2(0, AREA_SIZE.y * 16 * (zone - 1));
        GridPoint2 maxPos = new GridPoint2(AREA_SIZE.x * 16, AREA_SIZE.y * 16 * zone);

        for (int i = 0; i < numNPCs; i++) {
            GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
            Entity npc = creator.get();
            friendlyNPCs.add(npc);
            spawnEntityAt(npc, randomPos, true, false);
        }
    }

    private void spawnMinigameNPC(Supplier<Entity> creator, int numNPCs, TileLocation loc) {
        int zone = 0;
        if (loc == TileLocation.FOREST) {
            zone = 1;
        } else if (loc == TileLocation.AIR) {
            zone = 3;
        } else {
            zone = 2;
        }

        GridPoint2 minPos = new GridPoint2(0, AREA_SIZE.y * 16 * (zone - 1));
        GridPoint2 maxPos = new GridPoint2(AREA_SIZE.x * 16, AREA_SIZE.y * 16 * zone);

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

            spawnEntityAt(joey, spawnPos, true, true);
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
