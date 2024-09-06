package com.csse3200.game.areas;

import com.badlogic.gdx.audio.Music;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.gamearea.GameAreaDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Combat area used in combat screen. */
public class CombatArea extends GameArea {
    private static final Logger logger = LoggerFactory.getLogger(CombatArea.class);
    private static final String[] combatTextures = {
            "images/ghost_1.png",
    };
    private static final String backgroundMusic = "sounds/BGM_03_mp3.mp3";
    private Entity player;
    private final GdxGame game;

    public CombatArea(GdxGame game) {
        super();
        this.game = game;
    }

    /** Create the game area, including player and enemy.*/
    @Override
    public void create() {
        loadAssets();

        displayUI();

        playMusic();
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

        while (!resourceService.loadForMillis(10)) {
            // This could be upgraded to a loading screen
            logger.info("Loading... {}%", resourceService.getProgress());
        }
    }

    public void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(combatTextures);
    }

    @Override
    public void dispose() {
        super.dispose();
        ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class).stop();
        this.unloadAssets();
    }
}
