package com.csse3200.game.components.audio;

import com.badlogic.gdx.audio.Sound;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.AudioManager;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class DogSoundPlayerTest {

    private ResourceService mockResourceService;
    private Sound mockPantingSound;
    private Sound mockBarkingSound;
    private DogSoundPlayer dogSoundPlayer;

    private final String pantingSoundPath = "sounds/animal/panting.mp3";
    private final String barkingSoundPath = "sounds/animal/bark.mp3";

    @BeforeEach
    public void setUp() {
        // Mock the sound objects
        mockPantingSound = Mockito.mock(Sound.class);
        mockBarkingSound = Mockito.mock(Sound.class);

        // Mock the ResourceService
        mockResourceService = Mockito.mock(ResourceService.class);

        // Set up the ResourceService to return the mocked sounds
        when(mockResourceService.getAsset(pantingSoundPath, Sound.class)).thenReturn(mockPantingSound);
        when(mockResourceService.getAsset(barkingSoundPath, Sound.class)).thenReturn(mockBarkingSound);

        // Register the mocked ResourceService with ServiceLocator
        ServiceLocator.registerResourceService(mockResourceService);

        // Set AudioManager sound volume to a known value
        AudioManager.setSoundVolume(0.5f);

        // Instantiate DogSoundPlayer with the sound paths
        dogSoundPlayer = new DogSoundPlayer(pantingSoundPath, barkingSoundPath);
    }

    @Test
    void testPlayPantingSound() {
        // Arrange
        float expectedVolume = AudioManager.getSoundVolume();
        long mockPantingSoundId = 1L;
        when(mockPantingSound.loop(expectedVolume)).thenReturn(mockPantingSoundId);

        // Act
        dogSoundPlayer.playPantingSound();

        // Assert
        verify(mockPantingSound, times(1)).loop(expectedVolume); // Ensure panting sound is played with correct volume
    }

    @Test
    void testStopPantingSound() {
        // Arrange
        float expectedVolume = AudioManager.getSoundVolume();
        long mockPantingSoundId = 1L;
        when(mockPantingSound.loop(expectedVolume)).thenReturn(mockPantingSoundId);
        dogSoundPlayer.playPantingSound();  // Ensure panting sound is playing

        // Act
        dogSoundPlayer.stopPantingSound();

        // Assert
        verify(mockPantingSound, times(1)).stop(mockPantingSoundId); // Ensure panting sound is stopped
    }

    @Test
    void testUpdatePantingSound_PlaySoundWhenMoving() {
        // Arrange
        float expectedVolume = AudioManager.getSoundVolume();
        long mockPantingSoundId = 1L;
        when(mockPantingSound.loop(expectedVolume)).thenReturn(mockPantingSoundId);

        // Act
        dogSoundPlayer.updatePantingSound(true);

        // Assert
        verify(mockPantingSound, times(1)).loop(expectedVolume); // Ensure sound is played when moving
    }

    @Test
    void testUpdatePantingSound_StopSoundWhenNotMoving() {
        // Arrange
        float expectedVolume = AudioManager.getSoundVolume();
        long mockPantingSoundId = 1L;
        when(mockPantingSound.loop(expectedVolume)).thenReturn(mockPantingSoundId);
        dogSoundPlayer.playPantingSound();  // Ensure panting sound is playing

        // Act
        dogSoundPlayer.updatePantingSound(false);

        // Assert
        verify(mockPantingSound, times(1)).stop(mockPantingSoundId); // Ensure sound is stopped when not moving
    }

    @Test
    void testPlayBarkingSound() {
        // Arrange
        float expectedVolume = AudioManager.getSoundVolume();

        // Act
        dogSoundPlayer.playBarkingSound();

        // Assert
        verify(mockBarkingSound, times(1)).play(expectedVolume); // Ensure barking sound is played with correct volume
    }

    @Test
    void testPlayPantingSound_AlreadyPlaying() {
        // Arrange
        float expectedVolume = AudioManager.getSoundVolume();
        long mockPantingSoundId = 1L;
        when(mockPantingSound.loop(expectedVolume)).thenReturn(mockPantingSoundId);
        dogSoundPlayer.playPantingSound();

        // Act
        dogSoundPlayer.playPantingSound(); // Try to play again

        // Assert
        verify(mockPantingSound, times(1)).loop(expectedVolume); // Ensure sound is only played once
    }

    @Test
    void testStopPantingSound_NotPlaying() {
        // Act
        dogSoundPlayer.stopPantingSound(); // Try to stop when not playing

        // Assert
        verify(mockPantingSound, never()).stop(ArgumentMatchers.anyLong()); // Ensure stop is not called
    }
}
