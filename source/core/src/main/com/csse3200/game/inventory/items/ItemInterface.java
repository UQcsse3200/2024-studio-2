package main.com.csse3200.game.inventory.items;

import com.csse3200.game.inventory.items.ItemUsageContext;

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
     * @return the quantity of the item
     */
    int getQuantity();

    /**
     * Uses the item with the provided context or argument and returns a result.
     *
     * @param inputs the inputs required to use the item
     */
    void useItem(ItemUsageContext inputs);

    /**
     * @return whether the quantity of this item can be increased by n
     */
    boolean canAdd(int n);

    /**
     * @param n - number of item to add
     */
    void add(int n);

    /**
     * Checks whether an item has been consumed.
     * @return whether an item has been consumed.
     */
    boolean isConsumed();
}