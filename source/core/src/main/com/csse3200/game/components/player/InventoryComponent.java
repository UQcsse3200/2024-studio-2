package com.csse3200.game.components.player;

import com.csse3200.game.components.Component;
import com.csse3200.game.inventory.Inventory;

/**
 * Data class to store the player's inventory separately from the display components
 */
public class InventoryComponent extends Component {
    private final Inventory inventory;

    public InventoryComponent(int capacity) {
        this.inventory = new Inventory(capacity);
    }

    /**
     * Returns the player's current inventory from this attached InventoryComponent
     * @return The player's current inventory
     */
    public Inventory getInventory() {return this.inventory;}

    /**
     * Loads the inventory attached to the player from a save.
     */
    public void loadInventoryFromSave() {
        inventory.loadInventoryFromSave();
    }
}
