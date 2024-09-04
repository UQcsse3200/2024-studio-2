package com.csse3200.game.areas;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.components.combat.CombatExitDisplay;
import com.csse3200.game.components.gamearea.GameAreaDisplay;
import com.csse3200.game.areas.terrain.TerrainFactory.TerrainType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Combat area used in combat screen. */
public class CombatArea extends GameArea {
    private static final Logger logger = LoggerFactory.getLogger(CombatArea.class);
    private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(25, 25);
    private static final GridPoint2 MAP_SIZE = new GridPoint2(50, 50);
    private static final String[] combatTextures = {
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
    private static final String backgroundMusic = "sounds/BGM_03_mp3.mp3";
    private final TerrainFactory terrainFactory;
    private final Entity player;
    private final Entity enemy;
    private final GdxGame game;

    private static final String[] combatTextureAtlases = {
            "images/terrain_iso_grass.atlas", "images/chicken.atlas", "images/frog.atlas",
            "images/monkey.atlas", "images/Cow.atlas", "images/snake.atlas", "images/lion.atlas",
            "images/eagle.atlas", "images/turtle.atlas", "images/final_boss_kangaroo.atlas"
    };

    public CombatArea(GdxGame game, TerrainFactory terrainFactory, Entity player, Entity enemy) {
        super();
        this.game = game;
        this.enemy = enemy;
        this.player = player;
        this.terrainFactory = terrainFactory;
    }



    /** Create the game area, including player and enemy.*/
    @Override
    public void create() {
        loadAssets();

        displayUI();

        spawnTerrain();

        spawnPlayer();

        spawnEnemy();

        playMusic();
    }

    private void spawnTerrain() {
        // Background terrain
        terrain = terrainFactory.createTerrain(TerrainType.FOREST_DEMO, PLAYER_SPAWN, MAP_SIZE);
        spawnEntity(new Entity().addComponent(terrain));
    }

    private void spawnPlayer() {
        logger.info("Spawning player into combat screen from CombatArea");
        spawnEntityAt(player, PLAYER_SPAWN, true, true);
    }

    private void spawnEnemy() {
        logger.info("Spawning enemy into combat screen from CombatArea");
        spawnEntityAt(enemy, PLAYER_SPAWN, true, true);
    }

    public void displayUI() {
        Entity ui = new Entity();
        ui.addComponent(new GameAreaDisplay("Combat"));
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
        resourceService.loadTextureAtlases(combatTextureAtlases);

        while (!resourceService.loadForMillis(10)) {
            // This could be upgraded to a loading screen
            logger.info("Loading... {}%", resourceService.getProgress());
        }
    }

    public void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(combatTextures);
        resourceService.unloadAssets(combatTextureAtlases);
    }

    @Override
    public void dispose() {
        super.dispose();
        ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class).stop();
        this.unloadAssets();
    }
}
