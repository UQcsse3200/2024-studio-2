package com.csse3200.game.components.audio;

import com.badlogic.gdx.audio.Sound;
import com.csse3200.game.services.AudioManager;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DogSoundPlayer class responsible for managing the playback of dog-related sounds.
 * This class focuses on handling the panting and other dog-specific sounds.
 */
public class DogSoundPlayer {
    private static final Logger logger = LoggerFactory.getLogger(DogSoundPlayer.class);
    private final String pantingSoundPath;
    private final String barkingSoundPath;
    private long pantingSoundId = -1;

    public DogSoundPlayer(String pantingSoundPath, String barkingSoundPath) {
        this.pantingSoundPath = pantingSoundPath;
        this.barkingSoundPath = barkingSoundPath;
    }

    /**
     * Plays the panting sound in a loop, using the volume from AudioManager.
     */
    public void playPantingSound() {
        if (pantingSoundId == -1) {
            Sound pantingSound = ServiceLocator.getResourceService().getAsset(pantingSoundPath, Sound.class);
            if (pantingSound != null) {
                float volume = AudioManager.getSoundVolume();
                pantingSoundId = pantingSound.loop(volume);
                logger.info("Panting sound started looping with volume: {}", volume);
            }
        }
    }

    /**
     * Stops the panting sound if it is playing.
     */
    public void stopPantingSound() {
        if (pantingSoundId != -1) {
            Sound pantingSound = ServiceLocator.getResourceService().getAsset(pantingSoundPath, Sound.class);
            if (pantingSound != null) {
                pantingSound.stop(pantingSoundId);
                pantingSoundId = -1;
                logger.info("Panting sound stopped.");
            }
        }
    }

    /**
     * Updates the playing state of the panting sound based on movement.
     *
     * @param isMoving whether the dog is moving and should be panting
     */
    public void updatePantingSound(boolean isMoving) {
        if (isMoving) {
            playPantingSound();
        } else {
            stopPantingSound();
        }
    }

    /**
     * Plays the barking sound once, using the volume from AudioManager.
     */
    public void playBarkingSound() {
        Sound barkingSound = ServiceLocator.getResourceService().getAsset(barkingSoundPath, Sound.class);
        if (barkingSound != null) {
            float volume = AudioManager.getSoundVolume();
            barkingSound.play(volume);
            logger.info("Barking sound played with volume: {}", volume);
        }
    }
}
