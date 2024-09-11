package com.csse3200.game;

public class GdxGameManager {
    private static GdxGame instance;

    public static GdxGame getInstance() {
        return instance;
    }

    public static void setInstance(GdxGame gdxGame) {
        instance = gdxGame;
    }
}
