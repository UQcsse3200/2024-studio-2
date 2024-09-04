package com.csse3200.game.services;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;
import java.util.Map;

/**
 * AudioManager is a service for managing all game audio, including music and sound effects.
 * It interacts with the ServiceLocator to retrieve the ResourceService for sound management.
 */
public class AudioManager {

    private static float musicVolume = 1f;  // Default music volume (0.0 to 1.0)
    private static float soundVolume = 1f;  // Default sound volume (0.0 to 1.0)
    private static Music currentMusic;  // The currently playing background music
    private static final Map<Sound, Long> soundInstances = new HashMap<>();  // To manage sound instances

    /**
     * Play the specified sound at the current sound volume level, retrieved via ResourceService.
     * @param soundPath The path of the sound file to be played.
     */
    public static void playSound(String soundPath) {
        Sound sound = ServiceLocator.getResourceService().getAsset(soundPath, Sound.class);
        if (sound != null) {
            long id = sound.play(soundVolume);
            soundInstances.put(sound, id);
        }
    }

    /**
     * Stop the specified sound if it's currently playing.
     * @param soundPath The path of the sound file to be stopped.
     */
    public static void stopSound(String soundPath) {
        Sound sound = ServiceLocator.getResourceService().getAsset(soundPath, Sound.class);
        if (sound != null && soundInstances.containsKey(sound)) {
            sound.stop(soundInstances.get(sound));
        }
    }

    /**
     * Play the specified music with the current music volume level, retrieved via ResourceService.
     * @param musicPath The path of the music file to be played.
     * @param looping True if the music should loop, false otherwise.
     */
    public static void playMusic(String musicPath, boolean looping) {
        Music music = ServiceLocator.getResourceService().getAsset(musicPath, Music.class);
        if (music != null) {
            music.setVolume(musicVolume);
            music.setLooping(looping);
            music.play();
            currentMusic = music;
        }
    }

    /**
     * Stop the currently playing music.
     */
    public static void stopMusic() {
        if (currentMusic != null) {
            currentMusic.stop();
        }
    }

    /**
     * Set the volume for all music. This will apply to the currently playing music if any.
     * @param volume Volume level (0.0 to 1.0)
     */
    public static void setMusicVolume(float volume) {
        musicVolume = volume;
        if (currentMusic != null) {
            currentMusic.setVolume(musicVolume);  // Update volume for currently playing music
        }
    }

    /**
     * Set the volume for all sounds. This will apply to currently playing sounds as well.
     * @param volume Volume level (0.0 to 1.0)
     */
    public static void setSoundVolume(float volume) {
        soundVolume = volume;
        for (Map.Entry<Sound, Long> entry : soundInstances.entrySet()) {
            entry.getKey().setVolume(entry.getValue(), soundVolume);  // Update volume for all playing sounds
        }
    }

    /**
     * Get the current music volume level.
     * @return The current music volume (0.0 to 1.0)
     */
    public static float getMusicVolume() {
        return musicVolume;
    }

    /**
     * Get the current sound volume level.
     * @return The current sound volume (0.0 to 1.0)
     */
    public static float getSoundVolume() {
        return soundVolume;
    }
}
