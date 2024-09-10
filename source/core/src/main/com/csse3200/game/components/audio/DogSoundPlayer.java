package com.csse3200.game.components.audio;

import com.badlogic.gdx.audio.Sound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DogSoundPlayer class responsible for managing the playback of dog-related sounds.
 * This class focuses on handling the panting and other dog-specific sounds.
 */
public class DogSoundPlayer {
    private static final Logger logger = LoggerFactory.getLogger(DogSoundPlayer.class);
    private final Sound pantingSound;
    private final Sound barkingSound;
    private long pantingSoundId = -1;


    public DogSoundPlayer(Sound pantingSound, Sound barkingSound) {
        this.pantingSound = pantingSound;
        this.barkingSound = barkingSound;
    }

    /**
     * Plays the panting sound in a loop.
     *
     * @param volume the volume at which to play the sound (0.0f to 1.0f)
     */
    public void playPantingSound(float volume) {
        if (pantingSoundId == -1) {
            pantingSoundId = pantingSound.loop(volume);
            logger.info("Panting sound started looping with volume: " + volume);
        }
    }

    /**
     * Stops the panting sound if it is playing.
     */
    public void stopPantingSound() {
        if (pantingSoundId != -1) {
            pantingSound.stop(pantingSoundId);
            pantingSoundId = -1;
            logger.info("Panting sound stopped.");
        }
    }

    /**
     * Updates the playing state of the panting sound.
     *
     * @param isMoving whether the dog is moving and should be panting
     * @param volume the volume at which to play the sound (0.0f to 1.0f)
     */
    public void updatePantingSound(boolean isMoving, float volume) {
        if (isMoving) {
            playPantingSound(volume);
        } else {
            stopPantingSound();
        }
    }

    public void playBarkingSound(float volume) {
        if (barkingSound != null) {
            barkingSound.play(volume);
            logger.info("Barking sound played with volume: " + volume);
        }
    }
}
