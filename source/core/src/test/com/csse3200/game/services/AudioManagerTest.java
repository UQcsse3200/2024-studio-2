package com.csse3200.game.services;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;


@ExtendWith(GameExtension.class)
class AudioManagerTest {

    private ResourceService resourceService;

    @BeforeEach
    void setUp() {
        // Mock the ResourceService to return mock Sound and Music assets
        resourceService = mock(ResourceService.class);
        ServiceLocator.registerResourceService(resourceService);  // ServiceLocator is a singleton
    }

    @Test
    void shouldPlaySound() {
        // Mock sound
        Sound sound = mock(Sound.class);
        when(resourceService.getAsset("soundPath", Sound.class)).thenReturn(sound);

        // Call playSound method
        AudioManager.playSound("soundPath");

        // Verify sound is played with the current sound volume
        verify(sound).play(anyFloat());
    }
    @Test
    void shouldStopSound() {
        // Mock sound and set up a sound instance map
        Sound sound = mock(Sound.class);
        when(resourceService.getAsset("soundPath", Sound.class)).thenReturn(sound);
        when(sound.play(anyFloat())).thenReturn(1L);  // Mock sound ID

        // Simulate playing and stopping the sound
        AudioManager.playSound("soundPath");
        AudioManager.stopSound("soundPath");

        // Verify that the sound was stopped
        verify(sound).stop(1L);
    }

    @Test
    void shouldPlayMusic() {
        // Mock music
        Music music = mock(Music.class);
        when(resourceService.getAsset("musicPath", Music.class)).thenReturn(music);

        // Call playMusic method
        AudioManager.playMusic("musicPath", true);  // Playing music with looping

        // Verify music was played with the correct volume and looping
        verify(music).setVolume(anyFloat());
        verify(music).setLooping(true);
        verify(music).play();
    }

    @Test
    void shouldStopMusic() {
        // Mock music
        Music music = mock(Music.class);
        when(resourceService.getAsset("musicPath", Music.class)).thenReturn(music);

        // Simulate playing and stopping the music
        AudioManager.playMusic("musicPath", true);
        AudioManager.stopMusic();

        // Verify that the music was stopped
        verify(music).stop();
    }

    @Test
    void shouldSetSoundVolume() {
        // Mock sound
        Sound sound = mock(Sound.class);
        when(resourceService.getAsset("soundPath", Sound.class)).thenReturn(sound);
        when(sound.play(anyFloat())).thenReturn(1L);

        // Simulate playing a sound
        AudioManager.playSound("soundPath");

        // Set sound volume
        AudioManager.setSoundVolume(0.7f);  // Set volume to 70%

        // Verify that the sound volume was scaled and set correctly
        // Allow small delta Quadratic scaling: 0.7 * 0.7 = 0.49
        verify(sound).setVolume(eq(1L), floatThat(volume -> Math.abs(volume - 0.49f) < 0.001f));
    }

    @Test
    void shouldMuteAndUnmuteAudio() {
        // Mock music and sound
        Music music = mock(Music.class);
        Sound sound = mock(Sound.class);
        when(resourceService.getAsset("musicPath", Music.class)).thenReturn(music);
        when(resourceService.getAsset("soundPath", Sound.class)).thenReturn(sound);
        when(sound.play(anyFloat())).thenReturn(1L);

        // Play music and sound
        AudioManager.playMusic("musicPath", false);
        AudioManager.playSound("soundPath");

        // Mute audio
        AudioManager.muteAudio();

        // Check that the volume is reported as 0 when muted
        assert AudioManager.getMusicVolume() == 0f;
        assert AudioManager.getSoundVolume() == 0f;

        // Unmute audio
        AudioManager.unmuteAudio();

        // Verify that the music volume was set 2 times (once for mute, once for unmute)
        verify(music, times(3)).setVolume(anyFloat());

        // Verify that the sound volume was set 2 times (once for mute, once for unmute)
        verify(sound, times(2)).setVolume(eq(1L), anyFloat());
    }


    @Test
    void shouldScaleVolume() {
        // Set the music and sound volumes to check the quadratic scaling effect

        // Define an epsilon for floating-point comparison
        float epsilon = 0.0001f;

        AudioManager.setMusicVolume(0.6f);
        assert AudioManager.getMusicVolume() - 0.36f  <= epsilon; // 0.6 * 0.6 = 0.36

        AudioManager.setSoundVolume(0.8f);
        assert AudioManager.getSoundVolume() - 0.64f  <= epsilon ; // 0.8 * 0.8 = 0.64
    }

    @Test
    void shouldReturnCorrectVolumeWhenMuted() {
        // Define an epsilon for floating-point comparison
        float epsilon = 0.0001f;
        // Set music and sound volumes
        AudioManager.setMusicVolume(0.7f);
        AudioManager.setSoundVolume(0.8f);

        // Mute audio
        AudioManager.muteAudio();

        // Check that the volume is reported as 0 when muted
        assert AudioManager.getMusicVolume() == 0f;
        assert AudioManager.getSoundVolume() == 0f;

        // Unmute audio
        AudioManager.unmuteAudio();
        // Check that the volume returns to the previously set value
        assert AudioManager.getMusicVolume() - 0.49f <= epsilon; // Stays same after muted
        assert AudioManager.getSoundVolume() - 0.64f <= epsilon; // Stays same since music was empty
    }
    @Test
    void shouldHandleMissingSoundAsset() {
        // No sound is returned for the given path
        when(resourceService.getAsset("missingSoundPath", Sound.class)).thenReturn(null);

        // Attempt to play missing sound
        AudioManager.playSound("missingSoundPath");

        // Verify that getAsset was called but no interaction beyond that
        verify(resourceService).getAsset("missingSoundPath", Sound.class);
        verifyNoMoreInteractions(resourceService);
    }

    @Test
    void shouldHandleMissingMusicAsset() {
        // No music is returned for the given path
        when(resourceService.getAsset("missingMusicPath", Music.class)).thenReturn(null);

        // Attempt to play missing music
        AudioManager.playMusic("missingMusicPath", true);

        // Verify that getAsset was called but no interaction beyond that
        verify(resourceService).getAsset("missingMusicPath", Music.class);
        verifyNoMoreInteractions(resourceService);
    }
    @Test
    void shouldNotCrashOnStopMissingSound() {
        // No sound for the given path, expect no crash
        when(resourceService.getAsset("missingSoundPath", Sound.class)).thenReturn(null);

        // Attempt to stop missing sound
        AudioManager.stopSound("missingSoundPath");

        // Verify that getAsset was called but no interaction beyond that
        verify(resourceService).getAsset("missingSoundPath", Sound.class);
        verifyNoMoreInteractions(resourceService);
    }

    @Test
    void shouldNotCrashOnStopMissingMusic() {
        // No music for the given path, expect no crash
        when(resourceService.getAsset("missingMusicPath", Music.class)).thenReturn(null);

        // Attempt to stop missing music
        AudioManager.stopMusic();

        // Nothing should be stopped, verify no interaction
        verifyNoMoreInteractions(resourceService);
    }
    @Test
    void shouldScaleMusicVolumeCorrectlyAtLowVolume() {
        // Mock music
        Music music = mock(Music.class);
        when(resourceService.getAsset("musicPath", Music.class)).thenReturn(music);

        // Play music
        AudioManager.playMusic("musicPath", true);

        // Set a low music volume (0.1 -> squared to 0.01)
        AudioManager.setMusicVolume(0.1f);

        // Use ArgumentCaptor to capture the volume
        ArgumentCaptor<Float> volumeCaptor = ArgumentCaptor.forClass(Float.class);
        verify(music, atLeastOnce()).setVolume(volumeCaptor.capture());

        // Assert the volume with a small tolerance
        Assertions.assertEquals(0.01f, volumeCaptor.getValue(), 0.0001f);
    }


    @Test
    void shouldScaleSoundVolumeCorrectlyAtHighVolume() {
        // Mock sound
        Sound sound = mock(Sound.class);
        when(resourceService.getAsset("soundPath", Sound.class)).thenReturn(sound);
        when(sound.play(anyFloat())).thenReturn(1L);

        // Simulate playing the sound
        AudioManager.playSound("soundPath");

        // Set a high sound volume (0.9 -> squared to 0.81)
        AudioManager.setSoundVolume(0.9f);

        // Use ArgumentCaptor to capture the volume
        ArgumentCaptor<Float> volumeCaptor = ArgumentCaptor.forClass(Float.class);
        verify(sound).setVolume(eq(1L), volumeCaptor.capture());

        // Assert the volume with a small tolerance
        Assertions.assertEquals(0.81f, volumeCaptor.getValue(), 0.0001f);  // Use a tolerance for floating-point values
    }


    @Test
    void shouldHandleMaxVolumeScaling() {
        // Mock music and sound
        Music music = mock(Music.class);
        Sound sound = mock(Sound.class);
        when(resourceService.getAsset("musicPath", Music.class)).thenReturn(music);
        when(resourceService.getAsset("soundPath", Sound.class)).thenReturn(sound);
        when(sound.play(anyFloat())).thenReturn(1L);

        // Play music and sound
        AudioManager.playMusic("musicPath", true);
        AudioManager.playSound("soundPath");

        // Set volume to max (1.0)
        AudioManager.setMusicVolume(1f);
        AudioManager.setSoundVolume(1f);

        // Verify that the volume is 1 after scaling
        verify(music).setVolume(1f);
        verify(sound).setVolume(eq(1L), eq(1f));
    }

    @Test
    void shouldReturnCorrectDesiredVolumeWhenMuted() {
        // Set music and sound volumes
        AudioManager.setMusicVolume(0.7f);
        AudioManager.setSoundVolume(0.8f);

        // Mute audio
        AudioManager.muteAudio();

        // Ensure the desired volumes remain unchanged even when muted
        assert AudioManager.getDesiredMusicVolume() == 0.7f;
        assert AudioManager.getDesiredSoundVolume() == 0.8f;
    }

    @Test
    void shouldCorrectlyPlayLoopingMusic() {
        // Mock music
        Music music = mock(Music.class);
        when(resourceService.getAsset("loopingMusicPath", Music.class)).thenReturn(music);

        // Play looping music
        AudioManager.playMusic("loopingMusicPath", true);

        // Verify music was set to loop and played
        verify(music).setLooping(true);
        verify(music).play();
    }

    @Test
    void shouldNotSetVolumeWhenMuted() {
        // Mock music and sound
        Music music = mock(Music.class);
        Sound sound = mock(Sound.class);
        when(resourceService.getAsset("musicPath", Music.class)).thenReturn(music);
        when(resourceService.getAsset("soundPath", Sound.class)).thenReturn(sound);
        when(sound.play(anyFloat())).thenReturn(1L);

        // Mute audio
        AudioManager.muteAudio();

        // Try setting volumes while muted
        AudioManager.setMusicVolume(0.5f);
        AudioManager.setSoundVolume(0.5f);

        // Verify that no volume is set on the music and sound while muted
        verify(music, never()).setVolume(anyFloat());
        verify(sound, never()).setVolume(eq(1L), anyFloat());
    }
    @Test
    void shouldReturnDesiredMusicVolume() {
        // Set the music volume to a specific value
        AudioManager.setMusicVolume(0.7f);

        // Verify the desired music volume is returned
        Assertions.assertEquals(0.7f, AudioManager.getDesiredMusicVolume(), 0.0001f);
    }

    @Test
    void shouldReturnDesiredSoundVolume() {
        // Set the sound volume to a specific value
        AudioManager.setSoundVolume(0.8f);

        // Verify the desired sound volume is returned
        Assertions.assertEquals(0.8f, AudioManager.getDesiredSoundVolume(), 0.0001f);
    }

    @Test
    void shouldReturnCorrectDesiredVolumesWhenMuted() {
        // Set the volumes
        AudioManager.setMusicVolume(0.6f);
        AudioManager.setSoundVolume(0.5f);

        // Mute the audio
        AudioManager.muteAudio();

        // Check that the desired volume is still the original, not affected by muting
        Assertions.assertEquals(0.6f, AudioManager.getDesiredMusicVolume(), 0.0001f);
        Assertions.assertEquals(0.5f, AudioManager.getDesiredSoundVolume(), 0.0001f);
    }

    @Test
    void shouldReturnMutedState() {
        // Initially, the audio should not be muted
        assertFalse(AudioManager.isMuted(), "Audio should not be muted initially");

        // Mute the audio and check the state
        AudioManager.muteAudio();
        assertTrue(AudioManager.isMuted(), "Audio should be muted after calling muteAudio()");

        // Unmute the audio and check the state
        AudioManager.unmuteAudio();
        assertFalse(AudioManager.isMuted(), "Audio should not be muted after calling unmuteAudio()");
    }

    @Test
    void shouldHandleNullSoundAsset() {
        // Simulate the ResourceService returning a null sound asset
        when(resourceService.getAsset("invalidSoundPath", Sound.class)).thenReturn(null);

        // Try to play a sound, expecting no exceptions or crashes
        AudioManager.playSound("invalidSoundPath");

        // Ensure no interactions occurred because the sound asset was null
        verify(resourceService).getAsset("invalidSoundPath", Sound.class); // Asset retrieval attempted
    }

    @Test
    void shouldHandleNullMusicAsset() {
        // Simulate the ResourceService returning a null music asset
        when(resourceService.getAsset("invalidMusicPath", Music.class)).thenReturn(null);

        // Try to play music, expecting no exceptions or crashes
        AudioManager.playMusic("invalidMusicPath", false);

        // Ensure no interactions occurred because the music asset was null
        verify(resourceService).getAsset("invalidMusicPath", Music.class); // Asset retrieval attempted
    }

    @Test
    void shouldNotCrashOnStopNullSound() {
        // Simulate the ResourceService returning a null sound asset
        when(resourceService.getAsset("invalidSoundPath", Sound.class)).thenReturn(null);

        // Try to stop a sound, expecting no exceptions or crashes
        AudioManager.stopSound("invalidSoundPath");

        // Ensure no interactions occurred because the sound asset was null
        verify(resourceService).getAsset("invalidSoundPath", Sound.class); // Asset retrieval attempted
    }

    @Test
    void shouldNotCrashOnStopNullMusic() {
        // Simulate the ResourceService returning a null music asset
        when(resourceService.getAsset("invalidMusicPath", Music.class)).thenReturn(null);

        // Try to stop music, expecting no exceptions or crashes
        AudioManager.stopMusic();

        // No interactions needed because the music asset was null
    }


}