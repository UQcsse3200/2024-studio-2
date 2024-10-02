package com.csse3200.game.components.audio;

import com.badlogic.gdx.audio.Sound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * WaterAnimalSoundPlayer class responsible for managing the playback of water-animal-related sounds.
 * This class focuses on handling the swimming sounds.
 */
public class WaterAnimalSoundPlayer {
    private static final Logger logger = LoggerFactory.getLogger(WaterAnimalSoundPlayer.class);
    private final Sound swimmingSound;
    private long swimmingSoundId = -1;

    /**
     * Constructor for WaterAnimalSoundPlayer.
     *
     * @param swimmingSound The sound effect for swimming.
     */
    public WaterAnimalSoundPlayer(Sound swimmingSound) {
        this.swimmingSound = swimmingSound;
    }

    /**
     * Plays the swimming sound in a loop.
     *
     * @param volume the volume at which to play the sound (0.0f to 1.0f)
     */
    public void playSwimmingSound(float volume) {
        if (swimmingSoundId == -1) {
            swimmingSoundId = swimmingSound.loop(volume);
            logger.info("Swimming sound started looping with volume: " + volume);
        }
    }

    /**
     * Stops the swimming sound if it is playing.
     */
    public void stopSwimmingSound() {
        if (swimmingSoundId != -1) {
            swimmingSound.stop(swimmingSoundId);
            swimmingSoundId = -1;
            logger.info("Swimming sound stopped.");
        }
    }

    /**
     * Updates the playing state of the swimming sound.
     *
     * @param isSwimming whether the water animal is swimming and should play the swimming sound
     * @param volume     the volume at which to play the sound (0.0f to 1.0f)
     */
    public void updateSwimmingSound(boolean isSwimming, float volume) {
        if (isSwimming) {
            playSwimmingSound(volume);
        } else {
            stopSwimmingSound();
        }
    }
}
