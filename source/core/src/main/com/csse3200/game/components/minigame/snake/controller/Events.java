package com.csse3200.game.components.minigame.snake.controller;

public enum Events {
    NONE,
    RESTART,
    EXIT_TO_MENU;

    @Override
    public String toString() {
        return this.name();
    }
}
