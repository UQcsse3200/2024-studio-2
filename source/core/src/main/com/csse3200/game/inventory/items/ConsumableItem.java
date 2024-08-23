package com.csse3200.game.inventory.items;

import com.csse3200.game.inventory.items.exceptions.ConsumedException;

/**
 * Base class for consumable type items that can be used by a player.
 * <p>
 * This includes items that have limited uses until it is depleted (removed) from the inventory,
 * e.g. weapons with a durability or single use items such as food or potion.
 * </p>
 */
public abstract class ConsumableItem extends AbstractItem {

    /**
     * Constructs a ConsumableItem with given initial starting quantity and stack limit.
     * <p><b>Note - a consumable item cannot be created without a limit/quantity specified.</b></p>
     *
     * @param name the name of the item
     * @param itemCode the item code
     * @param limit the stack limit of the item
     * @param quantity the initial quantity for this item
     */
    public ConsumableItem(String name, int itemCode, int limit, int quantity, String description) {
        super(name, itemCode, limit, quantity, description);
    }

    /**
     *  Reduces quantity of item by 1.
     *  <p><b>Sub-classes MUST override this function and call super.useItem()</b></p>
     *
     * @param context the relevant {@link ItemUsageContext} for this item to be used
     * @throws ConsumedException if the item has already been consumed and cannot be used.
     */
    @Override
    public void useItem(ItemUsageContext context) {
        if (super.isEmpty()) {
            throw new ConsumedException();
        }
        this.quantity--;
    }
}