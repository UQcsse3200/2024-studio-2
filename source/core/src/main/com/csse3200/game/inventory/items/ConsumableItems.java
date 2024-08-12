package com.csse3200.game.inventory.items;

/**
 * Base class for consumable type items that can be used by a player.
 * <p>
 * This class provides a common implementation for the {@link ItemInterface} interface,
 * including default behavior for common properties like isConsumed and quantity. Subclasses
 * must provide their own implementation for the {@code useItem} method, which
 * defines how the item can be used as well how many uses is left and must define what {@link ItemUsageContext}
 * should be input to {@code useItem}.
 * </p>
 */
public class ConsumableItems extends AbstractItem {
    protected boolean isConsumed;
    protected boolean isEmpty;

    /**
     * Constructs a ConsumableItems with the specified uses
     */
    public ConsumableItems(int quantity) {
        super("Consumable", 10);
        this.quantity = quantity;
        this.isEmpty = true; // Flag iff there is no consumables uses left (true if not added or empty)
    }

    /**
     *  Updates the no. of uses left for each usage
     *
     * @param inputs the inputs for this ite to be used
     */
    @Override
    public void useItem(ItemUsageContext inputs) {
        if (this.quantity > 0) {
            this.quantity--;
            //Write functionality to how it would be used
        }
        else {
            this.isEmpty = isConsumed();
        }
    }

    public boolean isEmpty() {
        return this.isEmpty;
    }

    public double getUses() {
        return this.quantity;
    }
}
