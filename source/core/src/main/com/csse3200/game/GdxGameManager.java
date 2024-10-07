package com.csse3200.game;

public class GdxGameManager {
    private static GdxGame instance;

    private GdxGameManager() {
        throw new IllegalArgumentException("Do not instantiate util class!");
    }

    public static GdxGame getInstance() {
        return instance;
    }

    public static void setInstance(GdxGame gdxGame) {
        instance = gdxGame;
    }
}
