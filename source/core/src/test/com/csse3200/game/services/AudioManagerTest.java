package com.csse3200.game.services;

import static org.mockito.Mockito.*;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


@ExtendWith(GameExtension.class)
class AudioManagerTest {

    private ResourceService resourceService;

    @BeforeEach
    void setUp() {
        // Mock the ResourceService to return mock Sound and Music assets
        resourceService = mock(ResourceService.class);
        ServiceLocator.registerResourceService(resourceService);  // Assuming ServiceLocator is a singleton
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
    void shouldSetMusicVolume() {
        // Mock music
        Music music = mock(Music.class);
        when(resourceService.getAsset("musicPath", Music.class)).thenReturn(music);

        // Play music first
        AudioManager.playMusic("musicPath", false);

        // Set music volume
        AudioManager.setMusicVolume(0.5f);  // Set volume to 50%

        // Verify that the volume was scaled and set on the currently playing music
        verify(music).setVolume(0.25f);  // Quadratic scaling: 0.5 * 0.5 = 0.25
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
}