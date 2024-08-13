package com.csse3200.game.inventory.items;

/**
 * Base class for consumable type items that can be used by a player.
 * <p>
 * It is items that have limited uses until it is depleted (removed) from the inventory
 * Defines the useItem function in {@link ItemUsageContext}
 * Should be input to {@code useItem}.
 * </p>
 */
public abstract class ConsumableItem extends AbstractItem {

    /**
     * Constructs a ConsumableItem
     *
     * @param name is of item inherited from class AbstractItem
     * @param itemCode is the itemCode of the item inherited from class AbstractItem
     * @param limit is the limit of the item inherited from class AbstractItem
     * @param  quantity is the no. of copies the item has left which is inherited from class AbstractItem
     */
    public ConsumableItem(String name, int itemCode, int limit, int quantity) {
        super(name, itemCode, limit, quantity);
    }

    /**
     *  Updates the no. of uses left for each usage by decreasing quantity count after each call
     *
     * @param inputs the inputs for this ite to be used
     * @throws IllegalStateException iff consumable item is attempted to be used with quantity equal to zero (isEmpty())
     */
    @Override
    public void useItem(ItemUsageContext inputs) {
        if (super.isEmpty()) {
            throw new IllegalStateException("This item has ran out of uses");
        }
        this.quantity--;
        super.isEmpty();
    }
}