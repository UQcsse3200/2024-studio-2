package com.csse3200.game.minigames;

/** Enum representing the 4 cardinal directions or no direction */
public enum Direction {
    RIGHT(1,0),
    LEFT(-1,0),
    UP(0,1),
    DOWN(0,-1),
    ZERO(0,0);

    public final int dx;
    public final int dy;

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }
}
