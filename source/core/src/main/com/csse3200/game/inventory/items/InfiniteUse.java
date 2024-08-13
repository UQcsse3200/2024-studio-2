package com.csse3200.game.inventory.items;

public class InfiniteUse extends AbstractItem {
    /**
     * Constructs an AbstractItem with the specified name and limit.
     *
     * @param name: name of item
     * @param limit: limit on number of this item, limit <= quanity
     * @param quantity: quantity of item in inventory
     */
    public InfiniteUse(String name, int limit, int quantity) {
        super(name, limit, quantity);
    }

    /**
     * Uses the item with the provided {@link ItemUsageContext} and returns a result.
     * <p>
     * Usage is dependent on subclasses of InfiniteUse. The quantity does not change since
     * item cannot deplete.
     * </p>
     *
     * @param inputs the inputs for this item to be used
     */
    @Override
    public void useItem(ItemUsageContext inputs) {
        // will be implemented by subclasses
    }

    /**
     * An infinite use item can never be consumed
     *
     * @return false, since item can't be consumed
     */
    @Override
    public boolean isConsumed() {
        return false;
    }
}
