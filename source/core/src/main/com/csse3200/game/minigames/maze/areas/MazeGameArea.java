package com.csse3200.game.minigames.maze.areas;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.areas.GameArea;
import com.csse3200.game.minigames.maze.areas.terrain.MazeTerrainFactory;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.minigames.maze.Maze;
import com.csse3200.game.minigames.maze.components.gamearea.MazeGameAreaDisplay;
import com.csse3200.game.minigames.maze.components.tasks.MazeHuntTask;
import com.csse3200.game.minigames.maze.components.tasks.PatrolTask;
import com.csse3200.game.minigames.maze.entities.factories.MazeNPCFactory;
import com.csse3200.game.minigames.maze.entities.factories.MazeObstacleFactory;
import com.csse3200.game.minigames.maze.entities.factories.MazePlayerFactory;
import com.csse3200.game.minigames.maze.entities.mazenpc.AnglerFish;
import com.csse3200.game.minigames.maze.entities.mazenpc.ElectricEel;
import com.csse3200.game.minigames.maze.entities.mazenpc.Jellyfish;
import com.csse3200.game.services.AudioManager;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.csse3200.game.utils.math.GridPoint2Utils.GRID_DIRECTIONS;

/**
 * Forest area for the demo game with trees, a player, and some enemies.
 */
public class MazeGameArea extends GameArea {
    private static final Logger logger = LoggerFactory.getLogger(MazeGameArea.class);
    public static final float WALL_THICKNESS = 0.1f;

    // Number of entities spawned onto the maze
    public static final int NUM_WALL_BREAKS = 18;
    public static final int NUM_ANGLERS = 1;
    public static final int NUM_EELS = 6;
    public static final int NUM_JELLYFISH = 20;
    public static final int NUM_EGGS = 16;

    Map<Entity.EnemyType, List<Entity>> enemies;

    // entities textures and music
    private static final String[] mazeEnvironmentTextures = {
            "images/minigames/water.png",
            "images/minigames/wall.png",
            "images/minigames/fishegg.png",
            "images/PauseOverlay/TitleBG.png",
            "images/PauseOverlay/Button.png",
            "images/QuestsOverlay/Quest_BG.png",
            "images/QuestsOverlay/Quest_SBG.png",
    };
    private static final String[] mazeTextureAtlases = {
            "images/minigames/Angler.atlas", "images/minigames/fish.atlas",
            "images/minigames/Jellyfish.atlas", "images/minigames/eels.atlas",
            "images/minigames/GreenJellyfish.atlas"
    };
    private static final String[] mazeSounds = {
            "sounds/minigames/angler-chomp.mp3",
            "sounds/minigames/eel-zap.mp3",
            "sounds/minigames/eel-electricity.mp3",
            "sounds/minigames/maze-hit.mp3",
            "sounds/minigames/collect-fishegg.mp3"
    };
    private static final String mazeBackgroundMusic = "sounds/minigames/maze-bg.mp3";
    private static final String[] mazeMusic = {mazeBackgroundMusic};
    private final MazeTerrainFactory terrainFactory;  // Generates the maze tiles
    private Maze maze;  // The maze instance
    private Entity player;  // THe player instance

    /**
     * Initialise this ForestGameArea to use the provided TerrainFactory.
     *
     * @param terrainFactory TerrainFactory used to create the terrain for the GameArea.
     */
    public MazeGameArea(MazeTerrainFactory terrainFactory) {
        super();
        this.terrainFactory = terrainFactory;
        this.enemies = new HashMap<>();
    }

    /**
     * Create the game area, including terrain, static entities (trees), dynamic entities (player)
     */
    @Override
    public void create() {
        loadAssets();

        displayUI();

        maze = new Maze(MazeTerrainFactory.MAP_SIZE);
        maze.breakWalls(NUM_WALL_BREAKS);

        spawnTerrain();
        spawnWalls();
        player = spawnPlayer();
        spawnAngler();
        spawnJellyfish(MazeGameArea.NUM_JELLYFISH, 1f);
        spawnGreenJellyfish(MazeGameArea.NUM_JELLYFISH, 1f);
        spawnEels();
        spawnFishEggs();

        playMusic();
    }

    /**
     * Picks a random cell in the maze that is at least minDistToPlayer (euclidean distance) away
     * from the player to spawn an entity.
     * @param minDistToPlayer the minimum distance from the player to spawn an entity
     * @return Grid cell to spawn entity at
     */
    private GridPoint2 getSimpleStartLocation(float minDistToPlayer) {
        while (true) {
            GridPoint2 start = maze.getRandomCell();
            if (new Vector2(start.x, start.y).dst(player.getCenterPosition()) > minDistToPlayer) {
                return start;
            }
        }
    }

    /**
     * Displays the maze game area
     */
    public void displayUI() {
        Entity ui = new Entity();
        ui.addComponent(new MazeGameAreaDisplay("Underwater Maze"));
        spawnEntity(ui);
    }

    /**
     * Spawns walls on the maze mini-game area
     */
    private void spawnWalls() {
        for (int x = 0; x < maze.getWidth(); x++) {
            for (int y = 0; y < maze.getHeight(); y++) {
                for (GridPoint2 direction : GRID_DIRECTIONS) {
                    if (maze.isWall(x, y, direction)) {
                        float width = 1 + WALL_THICKNESS - Math.abs(direction.x);
                        float height = 1 + WALL_THICKNESS - Math.abs(direction.y);
                        float xPos = x + .5f * (1 + direction.x);
                        float yPos = y + .5f * (1 + direction.y);
                        spawnEntityCenteredAt(MazeObstacleFactory.createMazeWall(width, height), new Vector2(xPos, yPos));
                    }
                }
            }
        }
    }

    /**
     * Spawns the background (water) terrain
     */
    private void spawnTerrain() {
        // Background terrain
        terrain = terrainFactory.createTerrain();
        spawnEntity(new Entity().addComponent(terrain));
    }

    /**
     * Spawns and makes the player entity
     *
     * @return the player entity
     */
    private Entity spawnPlayer() {
        Entity newPlayer = MazePlayerFactory.createPlayer(this);
        newPlayer.addComponent(terrainFactory.getCameraComponent());
        spawnEntityAt(newPlayer, maze.getNextStartLocation(), true, true);
        newPlayer.getEvents().trigger("wanderStart");
        return newPlayer;
    }

    /**
     * Spawns in the angler npc.
     */
    private void spawnAngler() {
        for (int i = 0; i < MazeGameArea.NUM_ANGLERS; i++) {
            Entity angler = MazeNPCFactory.createAngler(player);
            spawnEntityAt(angler, maze.getNextStartLocation(), true, true);
            angler.getComponent(AITaskComponent.class).addTask(
                    new MazeHuntTask(player, maze, 2));
            getEnemies(Entity.EnemyType.MAZE_ANGLER).add(angler);
        }
    }

    /**
     * Spawns in the jellyfish npc. Jellyfish wander around, and do not actively seek
     * the player.
     */
    public void spawnJellyfish(int number, float minDistToPlayer) {
        for (int i = 0; i < number; i++) {
            Entity jellyfish = MazeNPCFactory.createJellyfish();
            spawnEntityAt(jellyfish, getSimpleStartLocation(minDistToPlayer), true, true);
            getEnemies(Entity.EnemyType.MAZE_JELLYFISH).add(jellyfish);
        }
    }

    private float randomRange(float min, float max) {
        return (float) Math.random() * (max - min) + min;
    }

    /**
     * Spawns in the jellyfish npc. Jellyfish wander around, and do not actively seek
     * the player.
     */
    public void spawnGreenJellyfish(int number, float minDistToPlayer) {
        for (int i = 0; i < number; i++) {
            Entity jellyfish = MazeNPCFactory.createGreenJellyfish();
            spawnEntityAt(jellyfish, getSimpleStartLocation(minDistToPlayer), true, true);
            GridPoint2 cell = MazeTerrainFactory.worldPosToGridPos(jellyfish.getCenterPosition());
            GridPoint2 otherCell = maze.getMazeAdjacent(cell).getFirst();
            jellyfish.getComponent(AITaskComponent.class).addTask(new PatrolTask(3, new Vector2[]{
                    new Vector2(cell.x + randomRange(WALL_THICKNESS, 1-WALL_THICKNESS-jellyfish.getScale().x),
                            cell.y + randomRange(WALL_THICKNESS, 1-WALL_THICKNESS-jellyfish.getScale().y)),
                    new Vector2(otherCell.x + randomRange(WALL_THICKNESS, 1-WALL_THICKNESS-jellyfish.getScale().x),
                            otherCell.y + randomRange(WALL_THICKNESS, 1-WALL_THICKNESS-jellyfish.getScale().y))
            }));
            getEnemies(Entity.EnemyType.MAZE_JELLYFISH).add(jellyfish);
        }
    }

    /**
     * Spawns the eels entities
     */
    private void spawnEels() {
        for (int i = 0; i < MazeGameArea.NUM_EELS; i++) {
            Entity eel = MazeNPCFactory.createEel(player);
            spawnEntityAt(eel, getSimpleStartLocation(3f), true, true);
            getEnemies(Entity.EnemyType.MAZE_EEL).add(eel);
        }
    }

    /**
     * Spawns in the fish egg npc.
     */
    private void spawnFishEggs() {
        for (int i = 0; i < MazeGameArea.NUM_EGGS; i++) {
            Entity fishEgg = MazeNPCFactory.createFishEgg();
            spawnEntityAt(fishEgg, maze.getNextStartLocation(), true, true);
        }
    }

    /**
     * Playes backgroun music for the maze mini-game
     */
    @Override
    public void playMusic() {
        AudioManager.playMusic("sounds/minigames/maze-bg.mp3", true);
    }

    /**
     * Stops the music (TODO: See if properly implemented)
     */
    public void pauseMusic() {
        AudioManager.stopMusic();  // Stop the music
    }

    /**
     * Loads assests onto the screen
     */
    private void loadAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(mazeEnvironmentTextures);
        resourceService.loadTextureAtlases(mazeTextureAtlases);
        resourceService.loadSounds(mazeSounds);
        resourceService.loadMusic(mazeMusic);

        while (!resourceService.loadForMillis(10)) {
            // This could be upgraded to a loading screen
            logger.info("Loading... {}%", resourceService.getProgress());
        }
    }

    /**
     * Unloads assets from the screen
     */
    @Override
    public void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(mazeEnvironmentTextures);
        resourceService.unloadAssets(mazeTextureAtlases);
        resourceService.unloadAssets(mazeSounds);
        resourceService.unloadAssets(mazeMusic);
    }

    /**
     * Disposes of assets used by this component, including the maze background music
     */
    @Override
    public void dispose() {
        super.dispose();
        ServiceLocator.getResourceService().getAsset(mazeBackgroundMusic, Music.class).stop();
        this.unloadAssets();
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

    /**
     * Gets the list of enemies
     *
     * @return the list of enemies
     */
    @Override
    public List<Entity> getEnemies() {
        List<Entity> list = new ArrayList<>();
        for (List<Entity> enemiesOfType : enemies.values()) {
            list.addAll(enemiesOfType);
        }
        return list;
    }

    public List<Entity> getEnemies(Entity.EnemyType enemyType) {
        if (!enemies.containsKey(enemyType)) {
            enemies.put(enemyType, new ArrayList<>());
        }
        return enemies.get(enemyType);
    }
}
