package com.csse3200.game.components.audio;

import com.badlogic.gdx.audio.Sound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

class DogSoundPlayerTest {

    private Sound mockPantingSound;
    private Sound mockBarkingSound;
    private DogSoundPlayer dogSoundPlayer;

    @BeforeEach
    public void setUp() {
        // Mock the sound objects
        mockPantingSound = Mockito.mock(Sound.class);
        mockBarkingSound = Mockito.mock(Sound.class);

        // Instantiate DogSoundPlayer with the mocked sounds
        dogSoundPlayer = new DogSoundPlayer(mockPantingSound, mockBarkingSound);
    }

    @Test
    void testPlayPantingSound() {
        // Arrange
        float volume = 0.5f;
        long mockPantingSoundId = 1L;
        when(mockPantingSound.loop(volume)).thenReturn(mockPantingSoundId);

        // Act
        dogSoundPlayer.playPantingSound(volume);

        // Assert
        verify(mockPantingSound, times(1)).loop(volume); // Ensure panting sound is played
    }

    @Test
    void testStopPantingSound() {
        // Arrange
        float volume = 0.5f;
        long mockPantingSoundId = 1L;
        when(mockPantingSound.loop(volume)).thenReturn(mockPantingSoundId);
        dogSoundPlayer.playPantingSound(volume);  // Ensure panting sound is playing

        // Act
        dogSoundPlayer.stopPantingSound();

        // Assert
        verify(mockPantingSound, times(1)).stop(mockPantingSoundId); // Ensure panting sound is stopped
    }

    @Test
    void testUpdatePantingSound_PlaySoundWhenMoving() {
        // Arrange
        float volume = 0.5f;
        long mockPantingSoundId = 1L;
        when(mockPantingSound.loop(volume)).thenReturn(mockPantingSoundId);

        // Act
        dogSoundPlayer.updatePantingSound(true, volume);

        // Assert
        verify(mockPantingSound, times(1)).loop(volume); // Ensure sound is played when moving
    }

    @Test
    void testUpdatePantingSound_StopSoundWhenNotMoving() {
        // Arrange
        float volume = 0.5f;
        long mockPantingSoundId = 1L;
        when(mockPantingSound.loop(volume)).thenReturn(mockPantingSoundId);
        dogSoundPlayer.playPantingSound(volume);  // Ensure panting sound is playing

        // Act
        dogSoundPlayer.updatePantingSound(false, volume);

        // Assert
        verify(mockPantingSound, times(1)).stop(mockPantingSoundId); // Ensure sound is stopped when not moving
    }

    @Test
    void testPlayBarkingSound() {
        // Arrange
        float volume = 0.7f;

        // Act
        dogSoundPlayer.playBarkingSound(volume);

        // Assert
        verify(mockBarkingSound, times(1)).play(volume); // Ensure barking sound is played
    }

    @Test
    void testPlayPantingSound_AlreadyPlaying() {
        // Arrange
        float volume = 0.5f;
        long mockPantingSoundId = 1L;
        when(mockPantingSound.loop(volume)).thenReturn(mockPantingSoundId);
        dogSoundPlayer.playPantingSound(volume);

        // Act
        dogSoundPlayer.playPantingSound(volume); // Try to play again

        // Assert
        verify(mockPantingSound, times(1)).loop(volume); // Ensure sound is only played once
    }

    @Test
    void testStopPantingSound_NotPlaying() {
        // Act
        dogSoundPlayer.stopPantingSound(); // Try to stop when not playing

        // Assert
        verify(mockPantingSound, times(0)).stop(ArgumentMatchers.anyLong()); // Ensure stop is not called
    }
}