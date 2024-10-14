package com.csse3200.game.components.audio;

import com.badlogic.gdx.audio.Sound;
import com.csse3200.game.services.AudioManager;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for managing the playback of water-animal-related sounds,
 * specifically focusing on swimming sounds. It utilizes the AudioManager to handle
 * sound volume levels.
 */
public class WaterAnimalSoundPlayer {
    private static final Logger logger = LoggerFactory.getLogger(WaterAnimalSoundPlayer.class);
    private final String swimmingSoundPath;
    private long swimmingSoundId = -1;

    /**
     * Constructs a new WaterAnimalSoundPlayer with a specific path to the swimming sound asset.
     * @param swimmingSoundPath Path to the swimming sound file.
     */
    public WaterAnimalSoundPlayer(String swimmingSoundPath) {
        this.swimmingSoundPath = swimmingSoundPath;
    }

    /**
     * Plays the swimming sound in a loop at the volume specified by the AudioManager.
     * If the sound is already playing, this method does nothing.
     */
    public void playSwimmingSound() {
        if (swimmingSoundId == -1) {
            Sound swimmingSound = ServiceLocator.getResourceService().getAsset(swimmingSoundPath, Sound.class);
            if (swimmingSound != null) {
                float volume = AudioManager.getSoundVolume();
                swimmingSoundId = swimmingSound.loop(volume);
                logger.info("Swimming sound started looping with volume: {}", volume);
            }
        }
    }

    /**
     * Stops the swimming sound if it is currently playing.
     * Resets the identifier to ensure it can be restarted.
     */
    public void stopSwimmingSound() {
        if (swimmingSoundId != -1) {
            Sound swimmingSound = ServiceLocator.getResourceService().getAsset(swimmingSoundPath, Sound.class);
            if (swimmingSound != null) {
                swimmingSound.stop(swimmingSoundId);
                swimmingSoundId = -1;
                logger.info("Swimming sound stopped.");
            }
        }
    }

    /**
     * Updates the playing state of the swimming sound based on the animal's movement.
     * Plays or stops the swimming sound based on whether the animal is swimming.
     *
     * @param isSwimming True if the animal is swimming and the sound should be played; false otherwise.
     */
    public void updateSwimmingSound(boolean isSwimming) {
        if (isSwimming) {
            playSwimmingSound();
        } else {
            stopSwimmingSound();
        }
    }
}
