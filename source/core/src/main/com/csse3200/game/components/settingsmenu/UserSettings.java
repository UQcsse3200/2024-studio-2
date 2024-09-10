package com.csse3200.game.components.settingsmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.csse3200.game.files.FileLoader.Location;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.services.AudioManager;

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

    // Store the last unmuted volume levels for easy restoration after unmuting
    private static float lastMusicVolume = 1f;
    private static float lastSoundVolume = 1f;
    private static boolean isMuted = false;
    private static boolean isFullScreen = false;


    /**
     * Get the stored user settings
     * @return Copy of the current settings
     */
    public static com.csse3200.game.components.settingsmenu.UserSettings.Settings get() {
        String path = ROOT_DIR + File.separator + SETTINGS_FILE;
        com.csse3200.game.components.settingsmenu.UserSettings.Settings fileSettings = FileLoader.readClass(com.csse3200.game.components.settingsmenu.UserSettings.Settings.class, path, Location.EXTERNAL);
        // Use default values if file doesn't exist
        return fileSettings != null ? fileSettings : new com.csse3200.game.components.settingsmenu.UserSettings.Settings();
    }

    /**
     * Set the stored user settings
     * @param settings New settings to store
     * @param applyImmediate true to immediately apply new settings.
     */
    public static void set(com.csse3200.game.components.settingsmenu.UserSettings.Settings settings, boolean applyImmediate) {
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
    public static void applySettings(com.csse3200.game.components.settingsmenu.UserSettings.Settings settings) {
        Gdx.graphics.setForegroundFPS(settings.fps);

        if (isFullScreen) {
            DisplayMode displayMode = findMatching(settings.displayMode);
            if (displayMode == null) {
                displayMode = Gdx.graphics.getDisplayMode();
            }
            Gdx.graphics.setFullscreenMode(displayMode);
        } else {
            Gdx.graphics.setWindowedMode(WINDOW_WIDTH, WINDOW_HEIGHT);
        }
        applyAudioSettings(settings.audioScale, settings.soundScale);
    }
    /**
     * Applies the audio settings to the game, including handling mute/unmute.
     * @param audioScale The music volume scale (0-100).
     * @param soundScale The sound effects volume scale (0-100).
     */
    private static void applyAudioSettings(float audioScale, float soundScale) {
        if (isMuted) {
            AudioManager.setMusicVolume(0f);
            AudioManager.setSoundVolume(0f);
        } else {
            // Store the last known unmuted values for restoration after unmuting
            lastMusicVolume = audioScale / 100f;
            lastSoundVolume = soundScale / 100f;

            AudioManager.setMusicVolume(lastMusicVolume);
            AudioManager.setSoundVolume(lastSoundVolume);
        }
    }
    public static void setFullScreenMode(boolean status) {
        isFullScreen = status;
        applyDisplayMode();
    }

    public static void applyDisplayMode() {
        if (isFullScreen) {
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        } else {
            Gdx.graphics.setWindowedMode(1280, 800);
        }
    }

    /**
     * Mutes the audio globally, storing the current volume settings and setting them to 0.
     */
    public static void muteAudio() {
        if (!isMuted) {
            // Store current volumes before muting
            lastMusicVolume = AudioManager.getMusicVolume();
            lastSoundVolume = AudioManager.getSoundVolume();

            // Set volumes to 0
            AudioManager.setMusicVolume(0f);
            AudioManager.setSoundVolume(0f);

            isMuted = true;
        }
    }

    /**
     * Unmutes the audio globally, restoring the last saved volume levels.
     */
    public static void unmuteAudio() {
        if (isMuted) {
            // Restore the volumes
            AudioManager.setMusicVolume(lastMusicVolume);
            AudioManager.setSoundVolume(lastSoundVolume);

            isMuted = false;
        }
    }

    /**
     * Toggles between mute and unmute.
     */
    public static void toggleMute() {
        if (isMuted) {
            unmuteAudio();
        } else {
            muteAudio();
        }
    }


    private static DisplayMode findMatching(com.csse3200.game.components.settingsmenu.UserSettings.DisplaySettings desiredSettings) {
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
        public boolean fullscreen = false;
        /**
         * ui Scale. Currently unused, but can be implemented.
         */
        public float audioScale = 100;
        public float soundScale = 100;
        public com.csse3200.game.components.settingsmenu.UserSettings.DisplaySettings displayMode = null;
        public String selectedMusicTrack = "Track 1";
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

