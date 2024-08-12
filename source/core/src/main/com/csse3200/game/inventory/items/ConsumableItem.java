package main.com.csse3200.game.inventory.items;

import com.csse3200.game.inventory.items.ItemUsageContext;

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
public abstract class ConsumableItem extends AbstractItem {
    protected boolean isEmpty;

    /**
     * Constructs a ConsumableItems with the specified uses
     */
    public ConsumableItem(String name, int quantity, int limit, boolean isEmpty) {
        super(name, quantity);
        this.isEmpty = isEmpty; // Flag iff there is no consumables uses left (true if not added or empty)
    }

    /**
     *  Updates the no. of uses left for each usage
     *
     * @param inputs the inputs for this ite to be used
     */
    @Override
    public void useItem(ItemUsageContext inputs) {
        if (super.quantity > 0) {
            super.quantity--;
            this.isEmpty = super.isEmpty();
            //Write functionality to how it would be used
        }
        else {
            this.isEmpty = isEmpty();
        }
    }
}