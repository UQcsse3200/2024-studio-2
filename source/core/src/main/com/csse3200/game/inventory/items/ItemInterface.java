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
     *
     * @return the item code
     */
    int getItemCode();

    /**
     * Gets the maximum number of this item that can be held in a `stack`.
     *
     * @return the stack limit of the item
     */
    int getLimit();

    /**
     * The current quantity of the item (used for display in inventory)
     *
     * @return the quantity of the item
     */
    int getQuantity();

    /**
     * Uses the item with the provided context.
     * See {@link ItemUsageContext} for details.
     *
     * @param context the context required to use the item
     */
    void useItem(ItemUsageContext context);

    /**
     * Checks the number of this item that can be further stacked.
     *
     * @return how much the quantity of this item can increase by
     */
    int canAdd();

    /**
     * Increases quantity of item by n.
     *
     * @param n - number of item to add
     */
    void add(int n);

    /**
     * Checks whether an item is an empty stack (0 quantity).
     * @return whether an item has been consumed.
     */
    boolean isEmpty();
}