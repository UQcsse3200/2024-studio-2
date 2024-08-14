package com.csse3200.game.team6;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.csse3200.game.files.FileLoader.Location;
import com.csse3200.game.files.FileLoader;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Reading, Writing, and applying user settings in the game.
 */
public class UserSettings {
    private static final String ROOT_DIR = "CSSE3200Game";
    private static final String SETTINGS_FILE = "settings.json";

    private static final int WINDOW_WIDTH = 1280;
    private static final int WINDOW_HEIGHT = 800;
    private static Map<Sound, Float> soundVolumes = new HashMap<>();
    private static Map<Music, Float> musicVolumes = new HashMap<>();

    /**
     * Get the stored user settings
     * @return Copy of the current settings
     */
    public static Settings get() {
        String path = ROOT_DIR + File.separator + SETTINGS_FILE;
        Settings fileSettings = FileLoader.readClass(Settings.class, path, Location.EXTERNAL);
        // Use default values if file doesn't exist
        return fileSettings != null ? fileSettings : new Settings();
    }

    /**
     * Set the stored user settings
     * @param settings New settings to store
     * @param applyImmediate true to immediately apply new settings.
     */
    public static void set(Settings settings, boolean applyImmediate) {
        String path = ROOT_DIR + File.separator + SETTINGS_FILE;
        FileLoader.writeClass(settings, path, Location.EXTERNAL);

        if (applyImmediate) {
            applySettings(settings);
        }
    }

    /**
     * Apply the given settings without storing them.
     * @param settings Settings to apply
     */
    public static void applySettings(Settings settings) {
        Gdx.graphics.setForegroundFPS(settings.fps);

        if (settings.fullscreen) {
            DisplayMode displayMode = findMatching(settings.displayMode);
            if (displayMode == null) {
                displayMode = Gdx.graphics.getDisplayMode();
            }
            Gdx.graphics.setFullscreenMode(displayMode);
        } else {
            Gdx.graphics.setWindowedMode(WINDOW_WIDTH, WINDOW_HEIGHT);
        }
        //applyAudioSettings(settings.audioScale, settings.soundScale);
    }
    /**
    private static void applyAudioSettings(float audioScale, float soundScale) {
        float volume = audioScale / 100f; // Scale to 0.0 - 1.0

        // Apply volume settings to sounds
        for (Map.Entry<Sound, Float> entry : soundVolumes.entrySet()) {
            Sound sound = entry.getKey();
            float originalVolume = entry.getValue();
            sound.setVolume(0, volume * originalVolume); // Adjust volume
        }

        // Appply volume settings to music
        for (Map.Entry<Music, Float> entry : musicVolumes.entrySet()) {
            Music music = entry.getKey();
            float originalVolume = entry.getValue();
            music.setVolume(volume * originalVolume); // adjust volume
        }
    } **/

    private static DisplayMode findMatching(DisplaySettings desiredSettings) {
        if (desiredSettings == null) {
            return null;
        }
        for (DisplayMode displayMode : Gdx.graphics.getDisplayModes()) {
            if (displayMode.refreshRate == desiredSettings.refreshRate
                    && displayMode.height == desiredSettings.height
                    && displayMode.width == desiredSettings.width) {
                return displayMode;
            }
        }

        return null;
    }

    /**
     * Stores game settings, can be serialised/deserialised.
     */
    public static class Settings {
        /**
         * FPS cap of the game. Independant of screen FPS.
         */
        public int fps = 60;
        public boolean fullscreen = true;
        /**
         * ui Scale. Currently unused, but can be implemented.
         */
        public float audioScale = 100;
        public float soundScale = 100;
        public DisplaySettings displayMode = null;
    }

    /**
     * Stores chosen display settings. Can be serialised/deserialised.
     */
    public static class DisplaySettings {
        public int width;
        public int height;
        public int refreshRate;

        public DisplaySettings() {}

        public DisplaySettings(DisplayMode displayMode) {
            this.width = displayMode.width;
            this.height = displayMode.height;
            this.refreshRate = displayMode.refreshRate;
        }
    }

    private UserSettings() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
