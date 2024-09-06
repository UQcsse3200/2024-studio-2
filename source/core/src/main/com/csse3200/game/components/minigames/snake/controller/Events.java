package com.csse3200.game.components.minigames.snake.controller;

/**
 * Enum representing possible events in the Snake mini-game, such as restarting the game or
 * exiting to the menu.
 */
public enum Events {
    NONE,
    RESTART,
    EXIT_TO_MENU;

    @Override
    public String toString() {
        return this.name();
    }
}
