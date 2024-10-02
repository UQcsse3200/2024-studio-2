package com.csse3200.game.components.inventory;

import com.csse3200.game.inventory.Inventory;
import com.csse3200.game.inventory.items.AbstractItem;
import com.csse3200.game.inventory.items.ItemUsageContext;
import com.csse3200.game.services.ServiceLocator;

public class CombatInventoryDisplay extends InventoryDisplay {

    /**
     * Constructs a PlayerInventoryDisplay with the specified capacity and number of columns.
     * The capacity must be evenly divisible by the number of columns.
     *
     * @param inventory      The inventory from which to build the display
     * @param numCols        The number of columns in the inventory display.
     * @param hotBarCapacity The number of slots allocated to the hotBar
     * @throws IllegalArgumentException if numCols is less than 1 or if capacity is not divisible by numCols.
     */
    public CombatInventoryDisplay(Inventory inventory, int numCols, int hotBarCapacity) {
        super(inventory, numCols, hotBarCapacity, false);
    }

    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener("itemUsedInCombat", this::useItemInCombat);
    }

    @Override
    public String toggleMsg() {return "toggleCombatInventory";}

    @Override
    protected void enterSlot(AbstractItem item) {
        // Does nothing in combat mode
    }

    @Override
    protected void exitSlot(AbstractItem item) {
        // Does nothing in combat mode
    }

    @Override
    protected void useItem(AbstractItem item, int index) {
        ItemUsageContext context = new ItemUsageContext(entity);
        if (!item.onlyMapItem()) {
            entity.getEvents().trigger("itemClicked", item, index, context);
        } else {
            String[][] itemText = {{"This item can only be used on the map!"}};
            ServiceLocator.getDialogueBoxService().updateText(itemText);
        }
    }

    /**
     * Uses the item in combat.
     * @param item   The item to use.
     * @param context The context in which the item is used.
     * @param index The index of the item in the inventory.
     */
    private void useItemInCombat(AbstractItem item, ItemUsageContext context, int index) {
        super.consumeItem(item, context, index);
    }
}