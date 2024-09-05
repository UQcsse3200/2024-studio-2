package com.csse3200.game.components.minigame;

/**
 * Enum to represent the medal scoring system
 */
public enum MiniGameMedals {
    BRONZE,
    SILVER,
    GOLD,
    FAIL;

    /**
     * Makes string representation of the enum.
     * @return the string representation
     */
    @Override
    public String toString() {
        return this.name();
    }
}
