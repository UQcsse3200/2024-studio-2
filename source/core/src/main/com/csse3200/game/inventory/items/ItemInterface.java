package com.csse3200.game.inventory.items;

/**
 * Represents an item that can be used by a player and stored within the inventory.
 */
public interface ItemInterface {
    /**
     * Gets the name of the item.
     *
     * @return the name of the item
     */
    String getName();

    /**
     * Gets the unique item code (up to item name) for the item
     */
    int getItemCode();

    /**
     * Gets the maximum number of this item that can be held in a stack.
     *
     * @return the limit of the item
     */
    int getLimit();

    /**
     * Uses the item with the provided context or argument and returns a result.
     *
     * @param inputs the inputs required to use the item
     */
    void useItem(ItemUsageContext inputs);

    /**
     * Checks whether an item has been consumed.
     * @return whether an item has been consumed.
     */
    boolean isConsumed();
}
