package com.csse3200.game.minigames.snake;

/**
 * Contains file paths for the assets used in the Snake mini-game,
 * including images for the apple, snake, and grid.
 */
public class AssetPaths {
    public static final String APPLE_IMAGE = "images/foodtextures/apple.png";
    public static final String SNAKE_HEAD_IMAGE = "images/minigames/snakehead.png";
    public static final String GRASS_IMAGE = "images/minigames/grass.jpg";
    public static final String GRASS_DARK_IMAGE = "images/minigames/grassDark.png";
    public static final String GRASS_LIGHT_IMAGE = "images/minigames/grassLight.png";

    public static final String SNAKE_BODY_HORIZONTAL_IMAGE = "images/minigames/snakebodyhorizontal.png";
    public static final String SNAKE_BODY_VERTICAL_IMAGE = "images/minigames/snakebodyvertical.png";
    public static final String SNAKE_BODY_BENT_IMAGE = "images/minigames/snakebodybent.png";
    public static final String SNAKE_TAIL_IMAGE = "images/minigames/snaketail.png";

    // Bird
    public static final String BIRD = "images/minigames/bird.png";
    public static final String SPIKE = "images/minigames/spikes.png";
    public static final String PIPE = "images/minigames/pipe.png";
    public static final String BACKGROUND = "images/BackgroundSplashBasic.png";

    public static final String[] IMAGES = {
            APPLE_IMAGE,
            SNAKE_HEAD_IMAGE,
            GRASS_IMAGE,
            GRASS_DARK_IMAGE,
            GRASS_LIGHT_IMAGE,
            SNAKE_BODY_HORIZONTAL_IMAGE,
            SNAKE_BODY_VERTICAL_IMAGE,
            SNAKE_BODY_BENT_IMAGE,
            SNAKE_TAIL_IMAGE,
            "images/PauseOverlay/TitleBG.png",
            "images/PauseOverlay/Button2.png",
            "images/QuestsOverlay/Quest_BG.png",
            "images/QuestsOverlay/Quest_SBG.png",
            BIRD
    };

    public static final String[] SOUNDS = {
            "sounds/minigames/snake-apple.mp3"
    };

    private AssetPaths() {
        throw new IllegalArgumentException("Do not instantiate util class!");
    }
}
