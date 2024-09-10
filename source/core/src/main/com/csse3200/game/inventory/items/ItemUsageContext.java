package com.csse3200.game.inventory.items;

import com.csse3200.game.entities.Entity;

/**
 * Represents the context containing inputs for using an item.
 * Note - when creating a new context, the number of inputs <strong>must</strong> be specified.
 */
public class ItemUsageContext {
    private final int numInputs;
    public Entity player;

    /**
     * Find number of inputs of a context (used for testing)
     */
    public int getNumInputs() {
        return numInputs;
    }

    /**
     * Empty context (used for items which require no inputs).
     */
    public ItemUsageContext() {
        this.numInputs = 0;
    }

    /**
     * Context used for using an item on a player
     * @param player the player to use the item on.
     */
    public ItemUsageContext(Entity player) {
        if (player == null) {
            throw new IllegalArgumentException("Cannot create ItemUsageContext with null Player!");
        }
        this.numInputs = 1;
        this.player = player;
    }
}