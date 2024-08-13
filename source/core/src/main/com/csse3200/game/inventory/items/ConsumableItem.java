package com.csse3200.game.inventory.items;

/**
 * Base class for consumable type items that can be used by a player.
 * <p>
 * Defines the useItemm function in {@link ItemUsageContext}
 * should be input to {@code useItem}.
 * </p>
 */
public abstract class ConsumableItem extends AbstractItem {

    /**
     * Constructs a ConsumableItem
     */
    public ConsumableItem(String name, int itemCode, int limit, int quantity) {
        super(name, itemCode, limit, quantity);
    }

    /**
     *  Updates the no. of uses left for each usage
     *
     * @param inputs the inputs for this ite to be used
     */
    @Override
    public void useItem(ItemUsageContext inputs) {
        if (super.isEmpty()) {
            throw new ArrayIndexOutOfBoundsException("This item has ran out of uses");
        }
        else {
            this.quantity--;
            super.isEmpty();
        }
    }
}