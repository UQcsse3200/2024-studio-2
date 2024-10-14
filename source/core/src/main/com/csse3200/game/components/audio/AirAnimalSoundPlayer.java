package com.csse3200.game.components.audio;

import com.badlogic.gdx.audio.Sound;
import com.csse3200.game.services.AudioManager;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for managing the playback of air-animal-related sounds,
 * focusing on sounds such as wings flapping and bird screeching. It uses the AudioManager
 * to control the volume levels.
 */
public class AirAnimalSoundPlayer {
    private static final Logger logger = LoggerFactory.getLogger(AirAnimalSoundPlayer.class);
    private final String flappingSoundPath;
    private final String screechSoundPath;
    private long flappingSoundId = -1;

    /**
     * Constructs a new AirAnimalSoundPlayer with specific paths to the flapping and screeching sound assets.
     * @param flappingSoundPath Path to the flapping sound file.
     * @param screechSoundPath Path to the screeching sound file.
     */
    public AirAnimalSoundPlayer(String flappingSoundPath, String screechSoundPath) {
        this.flappingSoundPath = flappingSoundPath;
        this.screechSoundPath = screechSoundPath;
    }

    /**
     * Plays the wings flapping sound in a loop at the volume specified by the AudioManager.
     * If the sound is already playing, this method does nothing.
     */
    public void playFlappingSound() {
        if (flappingSoundId == -1) {
            Sound flappingSound = ServiceLocator.getResourceService().getAsset(flappingSoundPath, Sound.class);
            if (flappingSound != null) {
                float volume = AudioManager.getSoundVolume();
                flappingSoundId = flappingSound.loop(volume);
                logger.info("Wings flapping sound started looping with volume: {}", volume);
            }
        }
    }

    /**
     * Stops the wings flapping sound if it is currently playing.
     * Resets the identifier to ensure it can be restarted.
     */
    public void stopFlappingSound() {
        if (flappingSoundId != -1) {
            Sound flappingSound = ServiceLocator.getResourceService().getAsset(flappingSoundPath, Sound.class);
            if (flappingSound != null) {
                flappingSound.stop(flappingSoundId);
                flappingSoundId = -1;
                logger.info("Wings flapping sound stopped.");
            }
        }
    }

    /**
     * Updates the playing state of the wings flapping sound based on the animal's flying state.
     * Plays or stops the wings flapping sound based on whether the air animal is flying.
     *
     * @param isFlying True if the air animal is flying and the sound should be played; false otherwise.
     */
    public void updateFlappingSound(boolean isFlying) {
        if (isFlying) {
            playFlappingSound();
        } else {
            stopFlappingSound();
        }
    }

    /**
     * Plays the bird screech sound once at the volume specified by the AudioManager.
     */
    public void playScreechSound() {
        Sound screechSound = ServiceLocator.getResourceService().getAsset(screechSoundPath, Sound.class);
        if (screechSound != null) {
            float volume = AudioManager.getSoundVolume();
            screechSound.play(volume);
            logger.info("Bird screech sound played with volume: {}", volume);
        }
    }
}
