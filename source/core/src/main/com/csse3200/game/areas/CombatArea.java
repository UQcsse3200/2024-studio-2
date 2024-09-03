package com.csse3200.game.areas;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.animal.AnimalSelectionActions;
import com.csse3200.game.components.combat.CombatExitDisplay;
import com.csse3200.game.components.gamearea.GameAreaDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.entities.factories.ObstacleFactory;
import com.csse3200.game.entities.factories.PlayerFactory;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.utils.math.GridPoint2Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.areas.terrain.TerrainFactory.TerrainType;


/** Combat area used in combat screen. */
public class CombatArea extends GameArea {
    private static final Logger logger = LoggerFactory.getLogger(CombatArea.class);

    // to include in combat textures being loaded
    private static String playerImagePath = AnimalSelectionActions.getSelectedAnimalImagePath();

    private static final String[] combatTextures = {
            "images/grass_3.png",
            playerImagePath,
            "images/final_boss_kangaroo_idle.png",
            // "sounds/QuestComplete.wav",
    };

    private static final String[] musicTextures = {"sounds/QuestComplete.wav"};

    private static final String[] combatAreaTextures = {
            "images/box_boy_leaf.png",
            "images/tree.png",
            "images/final_boss_kangaroo_idle.png",
            "images/ghost_king.png",
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
    private static final String[] combatAreaTextureAtlases = {
            "images/terrain_iso_grass.atlas", "images/ghost.atlas", "images/ghostKing.atlas", "final_boss_kangaroo.png",
    };
    private static final String backgroundMusic = "sounds/BGM_03_mp3.mp3";
    private Entity player;
    private final GdxGame game;
    private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(10, 12);
    private static final GridPoint2 ENEMY_COMBAT_SPAWN = new GridPoint2(22, 13);
    private static final float WALL_WIDTH = 0.1f;
    private static final GridPoint2 MAP_SIZE = new GridPoint2(5000, 5000);
    private final TerrainFactory terrainFactory;

    public CombatArea(TerrainFactory terrainFactory, GdxGame game) {
        super();
        this.terrainFactory = terrainFactory;
        this.game = game;

    }

    /** Create the game area, including player and enemy.*/
    @Override
    public void create() {
        loadAssets();

        displayUI();
        spawnTerrain();
        // player = spawnPlayer();
        spawnCombatEnemy();

        // playMusic();
    }

    //  /** Spawn a player for testing purposes. Currently, this player can be moved */
    private Entity spawnPlayer() {
        // System.out.println("Image path is: ");
        // System.out.println(AnimalSelectionActions.getSelectedAnimalImagePath());
        Entity newPlayer = PlayerFactory.createPlayer(game);
        spawnEntityAt(newPlayer, PLAYER_SPAWN, true, true);
        return newPlayer;
    }

    /** Spawn a combat enemy. Different to a regular enemy npc */
    private void spawnCombatEnemy() {
        // Create entity
        // for now, I have just manually initialised a boss Entity see CombatGameArea() for my
        // planned functionality -- callumR
        Entity combatEnemyNPC = NPCFactory.createKangaBossCombatEntity();
        // Create in the world
        spawnEntityAt(combatEnemyNPC, ENEMY_COMBAT_SPAWN, true, true);
    }

    public void displayUI() {
        Entity ui = new Entity();
        ui.addComponent(new GameAreaDisplay("Combat"));
        spawnEntity(ui); // added
    }

    public void playMusic() {
        Music music = ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class);
        music.setLooping(true);
        music.setVolume(0.3f);
        music.play();
    }
    public void pauseMusic() {
        Music music = ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class);
        music.pause();
    }

    public void loadAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(combatTextures);
        resourceService.loadTextures(combatAreaTextures);
        resourceService.loadTextureAtlases(combatAreaTextureAtlases);
        resourceService.loadSounds(musicTextures);

        while (!resourceService.loadForMillis(10)) {
            // This could be upgraded to a loading screen
            logger.info("Loading... {}%", resourceService.getProgress());
        }
    }

    public void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(combatTextures);
        resourceService.unloadAssets(combatAreaTextures);
        resourceService.unloadAssets(combatAreaTextureAtlases);
        resourceService.unloadAssets(musicTextures);
    }

    @Override
    public void dispose() {
        super.dispose();
        ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class).stop();
        this.unloadAssets();
    }

    private void spawnTerrain() {
        // Background terrain
        terrain = terrainFactory.createTerrain(TerrainType.FOREST_DEMO, PLAYER_SPAWN, MAP_SIZE);
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
}
