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
class AirAnimalSoundPlayerTest {

    private ResourceService mockResourceService;
    private Sound mockFlappingSound;
    private Sound mockScreechSound;
    private AirAnimalSoundPlayer airAnimalSoundPlayer;

    private final String flappingSoundPath = "sounds/air/flapping.mp3";
    private final String screechSoundPath = "sounds/air/screech.mp3";

    @BeforeEach
    public void setUp() {
        // Mock the sound objects
        mockFlappingSound = Mockito.mock(Sound.class);
        mockScreechSound = Mockito.mock(Sound.class);

        // Mock the ResourceService
        mockResourceService = Mockito.mock(ResourceService.class);

        // Set up the ResourceService to return the mocked sounds
        when(mockResourceService.getAsset(flappingSoundPath, Sound.class)).thenReturn(mockFlappingSound);
        when(mockResourceService.getAsset(screechSoundPath, Sound.class)).thenReturn(mockScreechSound);

        // Register the mocked ResourceService with ServiceLocator
        ServiceLocator.registerResourceService(mockResourceService);

        // Set AudioManager sound volume to a known value
        AudioManager.setSoundVolume(0.5f);

        // Instantiate AirAnimalSoundPlayer with the sound paths
        airAnimalSoundPlayer = new AirAnimalSoundPlayer(flappingSoundPath, screechSoundPath);
    }

    @Test
    void testPlayFlappingSound() {
        // Arrange
        float expectedVolume = AudioManager.getSoundVolume();
        long mockFlappingSoundId = 3L;
        when(mockFlappingSound.loop(expectedVolume)).thenReturn(mockFlappingSoundId);

        // Act
        airAnimalSoundPlayer.playFlappingSound();

        // Assert
        verify(mockFlappingSound, times(1)).loop(expectedVolume); // Ensure flapping sound is played with correct volume
    }

    @Test
    void testStopFlappingSound() {
        // Arrange
        float expectedVolume = AudioManager.getSoundVolume();
        long mockFlappingSoundId = 3L;
        when(mockFlappingSound.loop(expectedVolume)).thenReturn(mockFlappingSoundId);
        airAnimalSoundPlayer.playFlappingSound();  // Ensure flapping sound is playing

        // Act
        airAnimalSoundPlayer.stopFlappingSound();

        // Assert
        verify(mockFlappingSound, times(1)).stop(mockFlappingSoundId); // Ensure flapping sound is stopped
    }

    @Test
    void testUpdateFlappingSound_PlaySoundWhenFlying() {
        // Arrange
        float expectedVolume = AudioManager.getSoundVolume();
        long mockFlappingSoundId = 3L;
        when(mockFlappingSound.loop(expectedVolume)).thenReturn(mockFlappingSoundId);

        // Act
        airAnimalSoundPlayer.updateFlappingSound(true);

        // Assert
        verify(mockFlappingSound, times(1)).loop(expectedVolume); // Ensure sound is played when flying
    }

    @Test
    void testUpdateFlappingSound_StopSoundWhenNotFlying() {
        // Arrange
        float expectedVolume = AudioManager.getSoundVolume();
        long mockFlappingSoundId = 3L;
        when(mockFlappingSound.loop(expectedVolume)).thenReturn(mockFlappingSoundId);
        airAnimalSoundPlayer.playFlappingSound();  // Ensure flapping sound is playing

        // Act
        airAnimalSoundPlayer.updateFlappingSound(false);

        // Assert
        verify(mockFlappingSound, times(1)).stop(mockFlappingSoundId); // Ensure sound is stopped when not flying
    }

    @Test
    void testPlayScreechSound() {
        // Arrange
        float expectedVolume = AudioManager.getSoundVolume();

        // Act
        airAnimalSoundPlayer.playScreechSound();

        // Assert
        verify(mockScreechSound, times(1)).play(expectedVolume); // Ensure screech sound is played with correct volume
    }

    @Test
    void testPlayFlappingSound_AlreadyPlaying() {
        // Arrange
        float expectedVolume = AudioManager.getSoundVolume();
        long mockFlappingSoundId = 3L;
        when(mockFlappingSound.loop(expectedVolume)).thenReturn(mockFlappingSoundId);
        airAnimalSoundPlayer.playFlappingSound(); // Play sound first time

        // Act
        airAnimalSoundPlayer.playFlappingSound(); // Try to play again

        // Assert
        verify(mockFlappingSound, times(1)).loop(expectedVolume); // Ensure sound is only started once
    }

    @Test
    void testStopFlappingSound_NotPlaying() {
        // Act
        airAnimalSoundPlayer.stopFlappingSound(); // Try to stop when not playing

        // Assert
        verify(mockFlappingSound, never()).stop(anyLong()); // Ensure stop is not called
    }
}
