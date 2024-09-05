package com.csse3200.game.components.settingsmenu;

import static org.mockito.Mockito.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.AudioManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.MockedStatic;

@ExtendWith(GameExtension.class)
class UserSettingsTest {

    @Test
    void shouldApplySettings() {
        // Mock the necessary components
        Gdx.graphics = mock(Graphics.class);
        DisplayMode displayMode = mock(DisplayMode.class);
        when(Gdx.graphics.getDisplayMode()).thenReturn(displayMode);

        // Mock the static AudioManager class
        try (MockedStatic<AudioManager> audioManagerMock = mockStatic(AudioManager.class)) {

            // Set up test settings
            UserSettings.Settings settings = new UserSettings.Settings();
            settings.fullscreen = true;
            settings.fps = 40;
            settings.audioScale = 75; // 75% audio
            settings.soundScale = 80; // 80% sound

            // Ensure the audio is not muted
            UserSettings.unmuteAudio();

            // Apply the settings
            UserSettings.applySettings(settings);

            // Verify the correct order of method calls
            InOrder inOrder = inOrder(Gdx.graphics);
            inOrder.verify(Gdx.graphics).setForegroundFPS(settings.fps);
            inOrder.verify(Gdx.graphics).setFullscreenMode(displayMode);

            // Verify that audio settings were applied correctly
            audioManagerMock.verify(() -> AudioManager.setMusicVolume(0.75f)); // 75% volume
            audioManagerMock.verify(() -> AudioManager.setSoundVolume(0.80f)); // 80% volume
        }
    }

    @Test
    void shouldApplyWindowedSettings() {
        // Mock the necessary components
        Gdx.graphics = mock(Graphics.class);

        // Mock the static AudioManager class
        try (MockedStatic<AudioManager> audioManagerMock = mockStatic(AudioManager.class)) {

            // Set up test settings with windowed mode
            UserSettings.Settings settings = new UserSettings.Settings();
            settings.fullscreen = false;
            settings.fps = 60;
            settings.audioScale = 50;
            settings.soundScale = 60;

            // Ensure the audio is not muted
            UserSettings.unmuteAudio();  // To ensure it's not muted

            // Apply the settings
            UserSettings.applySettings(settings);

            // Verify that graphics settings were applied correctly for windowed mode
            InOrder inOrder = inOrder(Gdx.graphics);
            inOrder.verify(Gdx.graphics).setForegroundFPS(settings.fps);
            inOrder.verify(Gdx.graphics).setWindowedMode(1280, 800); // Default windowed resolution

            // Verify that audio settings were applied correctly (50% music, 60% sound)
            audioManagerMock.verify(() -> AudioManager.setMusicVolume(0.50f)); // 50% volume
            audioManagerMock.verify(() -> AudioManager.setSoundVolume(0.60f)); // 60% volume
        }
    }

    @Test
    void shouldMuteAudio() {
        // Mock the static AudioManager class
        try (MockedStatic<AudioManager> audioManagerMock = mockStatic(AudioManager.class)) {

            // Mute audio
            UserSettings.muteAudio();

            // Verify that music and sound volumes are set to 0 when muted
            audioManagerMock.verify(() -> AudioManager.setMusicVolume(0f), times(1));
            audioManagerMock.verify(() -> AudioManager.setSoundVolume(0f), times(1));
        }
    }

    @Test
    void shouldUnmuteAudio() {
        // Mock the static AudioManager class
        try (MockedStatic<AudioManager> audioManagerMock = mockStatic(AudioManager.class)) {

            // Mute audio first
            UserSettings.muteAudio();

            // Unmute audio
            UserSettings.unmuteAudio();

            // Verify that music and sound volumes are restored to their previous values
            audioManagerMock.verify(() -> AudioManager.setMusicVolume(anyFloat()), times(2)); // Once on mute and once on unmute
            audioManagerMock.verify(() -> AudioManager.setSoundVolume(anyFloat()), times(2)); // Once on mute and once on unmute
        }
    }

    @Test
    void shouldFindMatchingDisplayMode() {
        // Mock the necessary components
        Gdx.graphics = mock(Graphics.class);
        DisplayMode correctMode = new CustomDisplayMode(1920, 1080, 60, 0);
        DisplayMode[] displayModes = {
                new CustomDisplayMode(1280, 720, 30, 0),
                correctMode
        };
        when(Gdx.graphics.getDisplayModes()).thenReturn(displayModes);

        // Set up test settings
        UserSettings.Settings settings = new UserSettings.Settings();
        settings.displayMode = new UserSettings.DisplaySettings();
        settings.displayMode.width = 1920;
        settings.displayMode.height = 1080;
        settings.displayMode.refreshRate = 60;
        settings.fullscreen = true;

        // Apply the settings
        UserSettings.applySettings(settings);

        // Verify that the display mode was retrieved and fullscreen mode was set
        InOrder inOrder = inOrder(Gdx.graphics);
        inOrder.verify(Gdx.graphics).getDisplayModes();  // Ensure display modes are retrieved first
        inOrder.verify(Gdx.graphics).setFullscreenMode(correctMode);  // Then verify fullscreen mode is set with correct mode
    }

    /**
     * Custom DisplayMode class for testing purposes.
     */
    static class CustomDisplayMode extends DisplayMode {
        public CustomDisplayMode(int width, int height, int refreshRate, int bitsPerPixel) {
            super(width, height, refreshRate, bitsPerPixel);
        }
    }
}
