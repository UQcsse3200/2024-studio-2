package com.csse3200.game.areas.forest;

public class ForestSoundsConfig {
    public static final String[] GAME_SOUNDS = {
            "sounds/aus-magpie.wav",
            "sounds/mooing-cow.wav",
            "sounds/tiger-roar.wav",
            "sounds/turtle-hiss.wav",
            "sounds/snake-hiss.wav",
            "sounds/FishBubble.wav",
            "sounds/eagle-scream.wav",
            "sounds/chicken.wav",
            "sounds/frog.wav",
            "sounds/monkey.wav",
            "sounds/QuestComplete.wav",
            "sounds/achievement-sound.mp3",
            "sounds/Impact4.ogg"
    };

    public static final String BACKGROUND_MUSIC = "sounds/BGM_03_mp3.mp3";

    public static final String[] GAME_MUSIC = {
            BACKGROUND_MUSIC,
            "sounds/heartbeat.mp3"
    };

    public static final String [] CHARACTER_SOUNDS = {
            "sounds/animal/panting.mp3",
            "sounds/animal/bark.mp3",
            "sounds/animal/catgrowl.mp3",
            "sounds/animal/birdscreech.mp3",
            "sounds/animal/flap.mp3",
            "sounds/animal/waterwhoosh.mp3"
    };

    private ForestSoundsConfig() {
        throw new IllegalArgumentException("Do not instantiate util class!");
    }
}
