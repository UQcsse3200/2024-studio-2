package com.csse3200.game.components.minigame;

public final class MiniGameConstants {

    private MiniGameConstants() {
        throw new AssertionError("Cannot instantiate MiniGameConstants class");

    }
    public static final int SNAKE_FAIL_THRESHOLD = 5;
    public static final int SNAKE_BRONZE_THRESHOLD = 15;
    public static final int SNAKE_SILVER_THRESHOLD = 30;
    public static final int SNAKE_GOLD_THRESHOLD = (int) Double.POSITIVE_INFINITY;;
}
