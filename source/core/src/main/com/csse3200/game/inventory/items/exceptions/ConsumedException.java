package com.csse3200.game.inventory.items.exceptions;

public class ConsumedException extends RuntimeException {
    // Constructor with no arguments
    public ConsumedException() {
        super("Attempting to use an empty consumable item!");
    }
}
