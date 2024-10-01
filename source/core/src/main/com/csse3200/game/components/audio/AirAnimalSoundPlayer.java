package com.csse3200.game.components.audio;

import com.badlogic.gdx.audio.Sound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AirAnimalSoundPlayer class responsible for managing the playback of air-animal-related sounds.
 * This class focuses on handling the wings flapping and screech sounds for birds or other air animals.
 */
public class AirAnimalSoundPlayer {
    private static final Logger logger = LoggerFactory.getLogger(AirAnimalSoundPlayer.class);
    private final Sound flappingSound;
    private final Sound screechSound;
    private long flappingSoundId = -1;

    /**
     * Constructor for AirAnimalSoundPlayer.
     *
     * @param flappingSound The sound effect for wings flapping.
     * @param screechSound  The sound effect for bird screeching.
     */
    public AirAnimalSoundPlayer(Sound flappingSound, Sound screechSound) {
        this.flappingSound = flappingSound;
        this.screechSound = screechSound;
    }

    /**
     * Plays the wings flapping sound in a loop.
     *
     * @param volume the volume at which to play the sound (0.0f to 1.0f)
     */
    public void playFlappingSound(float volume) {
        if (flappingSoundId == -1) {
            flappingSoundId = flappingSound.loop(volume);
            logger.info("Wings flapping sound started looping with volume: " + volume);
        }
    }

    /**
     * Stops the wings flapping sound if it is playing.
     */
    public void stopFlappingSound() {
        if (flappingSoundId != -1) {
            flappingSound.stop(flappingSoundId);
            flappingSoundId = -1;
            logger.info("Wings flapping sound stopped.");
        }
    }

    /**
     * Updates the playing state of the wings flapping sound.
     *
     * @param isFlying whether the air animal is flying and should have flapping sound
     * @param volume   the volume at which to play the sound (0.0f to 1.0f)
     */
    public void updateFlappingSound(boolean isFlying, float volume) {
        if (isFlying) {
            playFlappingSound(volume);
        } else {
            stopFlappingSound();
        }
    }

    /**
     * Plays the bird screech sound.
     *
     * @param volume the volume at which to play the sound (0.0f to 1.0f)
     */
    public void playScreechSound(float volume) {
        if (screechSound != null) {
            screechSound.play(volume);
            logger.info("Bird screech sound played with volume: " + volume);
        }
    }
}
