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
    private static boolean isMuted = false;  // Global mute state
    private static Music currentMusic;  // The currently playing background music
    private static final Map<Sound, Long> soundInstances = new HashMap<>(); // To manage sound instances

    // Set the desired audio scale values even if the game is muted.
    private static float desiredMusicVolume = 1f;  // User-desired music volume
    private static float desiredSoundVolume = 1f;  // User-desired sound volume

    private static Map<String, Music> musicTracks = new HashMap<>();

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
     * Mute both music and sound. This will set the volume to 0 but retain the previous volume.
     */
    public static void muteAudio() {
        if (!isMuted) {
            // Set volumes to 0 without changing desired values
            musicVolume = 0f;
            soundVolume = 0f;
            if (currentMusic != null) {
                currentMusic.setVolume(musicVolume);
            }
            for (Map.Entry<Sound, Long> entry : soundInstances.entrySet()) {
                entry.getKey().setVolume(entry.getValue(), soundVolume);
            }

            isMuted = true;
        }
    }

    /**
     * Unmute both music and sound. This will restore the volume to its previous levels.
     */
    public static void unmuteAudio() {
        if (isMuted) {
            // Restore desired volumes when unmuted
            musicVolume = desiredMusicVolume;
            soundVolume = desiredSoundVolume;

            if (currentMusic != null) {
                currentMusic.setVolume(musicVolume);
            }
            for (Map.Entry<Sound, Long> entry : soundInstances.entrySet()) {
                entry.getKey().setVolume(entry.getValue(), soundVolume);
            }

            isMuted = false;
        }
    }

    private static float scaleVolume(float input) {
        // Apply quadratic scaling to make the volume change more gradual
        return input * input;
    }


    /**
     * Check if the audio is muted.
     * @return True if audio is muted, false otherwise.
     */
    public static boolean isMuted() {
        return isMuted;
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
     * If the audio is muted, it will override this and set the volume to 0.
     * @param volume Volume level (0.0 to 1.0)
     */
    public static void setMusicVolume(float volume) {
        desiredMusicVolume = volume;  // Always save the desired value
        if (!isMuted) {
            musicVolume = (float) scaleVolume(volume);
            if (currentMusic != null) {
                currentMusic.setVolume(musicVolume);
            }
        }
    }

    /**
     * Set the volume for all sounds. This will apply to currently playing sounds as well.
     * If the audio is muted, it will override this and set the volume to 0.
     * @param volume Volume level (0.0 to 1.0)
     */
    public static void setSoundVolume(float volume) {
        desiredSoundVolume = volume;  // Always save the desired value
        if (!isMuted) {
            soundVolume = (float)scaleVolume(volume);
            for (Map.Entry<Sound, Long> entry : soundInstances.entrySet()) {
                entry.getKey().setVolume(entry.getValue(), soundVolume);
            }
        }
    }

    /**
     * Get the current music volume level.
     * @return The current music volume (0.0 to 1.0)
     */
    public static float getMusicVolume() {
        return isMuted ? 0f : musicVolume;
    }

    /**
     * Get the current sound volume level.
     * @return The current sound volume (0.0 to 1.0)
     */
    public static float getSoundVolume() {
        return isMuted ? 0f : soundVolume;
    }

    public static float getDesiredMusicVolume() {
        return desiredMusicVolume;  // Return the desired volume even if muted
    }

    public static float getDesiredSoundVolume() {
        return desiredSoundVolume;  // Return the desired volume even if muted
    }
}
