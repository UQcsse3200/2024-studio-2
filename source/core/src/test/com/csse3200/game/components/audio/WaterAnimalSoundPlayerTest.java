package com.csse3200.game.components.audio;

import com.badlogic.gdx.audio.Sound;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.AudioManager;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class WaterAnimalSoundPlayerTest {

    private ResourceService mockResourceService;
    private Sound mockSwimmingSound;
    private WaterAnimalSoundPlayer waterAnimalSoundPlayer;

    private final String swimmingSoundPath = "sounds/water/swimming.mp3";

    @BeforeEach
    public void setUp() {
        // Mock the sound object
        mockSwimmingSound = Mockito.mock(Sound.class);

        // Mock the ResourceService
        mockResourceService = Mockito.mock(ResourceService.class);

        // Set up the ResourceService to return the mocked sound
        when(mockResourceService.getAsset(swimmingSoundPath, Sound.class)).thenReturn(mockSwimmingSound);

        // Register the mocked ResourceService with ServiceLocator
        ServiceLocator.registerResourceService(mockResourceService);

        // Set AudioManager sound volume to a known value
        AudioManager.setSoundVolume(0.5f);

        // Instantiate WaterAnimalSoundPlayer with the sound path
        waterAnimalSoundPlayer = new WaterAnimalSoundPlayer(swimmingSoundPath);
    }

    @Test
    void testPlaySwimmingSound() {
        // Arrange
        float expectedVolume = AudioManager.getSoundVolume();
        long mockSwimmingSoundId = 2L;
        when(mockSwimmingSound.loop(expectedVolume)).thenReturn(mockSwimmingSoundId);

        // Act
        waterAnimalSoundPlayer.playSwimmingSound();

        // Assert
        verify(mockSwimmingSound, times(1)).loop(expectedVolume); // Ensure swimming sound is played with correct volume
    }

    @Test
    void testStopSwimmingSound() {
        // Arrange
        float expectedVolume = AudioManager.getSoundVolume();
        long mockSwimmingSoundId = 2L;
        when(mockSwimmingSound.loop(expectedVolume)).thenReturn(mockSwimmingSoundId);
        waterAnimalSoundPlayer.playSwimmingSound();  // Ensure swimming sound is playing

        // Act
        waterAnimalSoundPlayer.stopSwimmingSound();

        // Assert
        verify(mockSwimmingSound, times(1)).stop(mockSwimmingSoundId); // Ensure swimming sound is stopped
    }

    @Test
    void testUpdateSwimmingSound_PlaySoundWhenSwimming() {
        // Arrange
        float expectedVolume = AudioManager.getSoundVolume();
        long mockSwimmingSoundId = 2L;
        when(mockSwimmingSound.loop(expectedVolume)).thenReturn(mockSwimmingSoundId);

        // Act
        waterAnimalSoundPlayer.updateSwimmingSound(true);

        // Assert
        verify(mockSwimmingSound, times(1)).loop(expectedVolume); // Ensure sound is played when swimming
    }

    @Test
    void testUpdateSwimmingSound_StopSoundWhenNotSwimming() {
        // Arrange
        float expectedVolume = AudioManager.getSoundVolume();
        long mockSwimmingSoundId = 2L;
        when(mockSwimmingSound.loop(expectedVolume)).thenReturn(mockSwimmingSoundId);
        waterAnimalSoundPlayer.playSwimmingSound();  // Ensure swimming sound is playing

        // Act
        waterAnimalSoundPlayer.updateSwimmingSound(false);

        // Assert
        verify(mockSwimmingSound, times(1)).stop(mockSwimmingSoundId); // Ensure sound is stopped when not swimming
    }

    @Test
    void testPlaySwimmingSound_AlreadyPlaying() {
        // Arrange
        float expectedVolume = AudioManager.getSoundVolume();
        long mockSwimmingSoundId = 2L;
        when(mockSwimmingSound.loop(expectedVolume)).thenReturn(mockSwimmingSoundId);
        waterAnimalSoundPlayer.playSwimmingSound(); // Play sound first time

        // Act
        waterAnimalSoundPlayer.playSwimmingSound(); // Try to play again

        // Assert
        verify(mockSwimmingSound, times(1)).loop(expectedVolume); // Ensure sound is only started once
    }

    @Test
    void testStopSwimmingSound_NotPlaying() {
        // Act
        waterAnimalSoundPlayer.stopSwimmingSound(); // Try to stop when not playing

        // Assert
        verify(mockSwimmingSound, never()).stop(anyLong()); // Ensure stop is not called
    }
}
