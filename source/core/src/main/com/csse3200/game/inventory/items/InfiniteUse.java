package com.csse3200.game.inventory.items;

public class InfiniteUse extends AbstractItem {
    /**
     * Constructs an AbstractItem with the specified name and limit.
     *
     * @param name: name of item
     * @param itemCode: unique identifier for item
     */
    public InfiniteUse(String name, int itemCode) {
        super(name, itemCode);
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
     * Inventory can never be empty of this item
     *
     * @return false, since item is infinite use
     */
    @Override
    public boolean isEmpty() {
        return false;
    }
}
