package com.csse3200.game.components.audio;

import com.badlogic.gdx.audio.Sound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CatSoundPlayer {
    private static final Logger logger = LoggerFactory.getLogger(CatSoundPlayer.class);
    private final Sound meowSound;

    public CatSoundPlayer(Sound meowSound) {
        this.meowSound = meowSound;
    }

    public void playMeowSound(float volume) {
        if (meowSound != null) {
            meowSound.play(volume);
        }
        logger.info(String.format("Meow sound played with volume: %.2f", volume));
    }
}
