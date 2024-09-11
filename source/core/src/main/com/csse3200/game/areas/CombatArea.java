package com.csse3200.game.areas;

import com.badlogic.gdx.audio.Music;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.combat.CombatExitDisplay;
import com.csse3200.game.components.gamearea.GameAreaDisplay;
import com.csse3200.game.components.settingsmenu.UserSettings;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.AudioManager;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Objects;

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

  public static void playMusic() {
//    Music music = ServiceLocator.getResourceService().getAsset(BACKGROUND_MUSIC, Music.class);
//    music.setLooping(true);
//    music.setVolume(0.5f);
//    music.play();
    // Get the selected music track from the user settings
    UserSettings.Settings settings = UserSettings.get();
    String selectedTrack = settings.selectedMusicTrack;  // This will be "Track 1" or "Track 2"

    if (Objects.equals(selectedTrack, "Track 1")) {
      AudioManager.playMusic("sounds/BGM_03_mp3.mp3", true);
    } else if (Objects.equals(selectedTrack, "Track 2")) {
        AudioManager.playMusic("sounds/track_2.mp3", true);
    }
  }
  public static void pauseMusic() {
//    Music music = ServiceLocator.getResourceService().getAsset(BACKGROUND_MUSIC, Music.class);
//    music.pause();
    AudioManager.stopMusic();  // Stop the music
  }

    @Override
    public Entity getPlayer() {
        return player;
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
