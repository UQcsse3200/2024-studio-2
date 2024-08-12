package com.csse3200.game.inventory.items;

/**
 * Represents the context containing inputs for using an item.
 */
public class ItemUsageContext {
    private final int numInputs;

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
}
