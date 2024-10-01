package com.csse3200.game.components.inventory;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.csse3200.game.inventory.Inventory;

/**
 * CombatInventoryDisplay class extends InventoryDisplay.
 * This represents a combat-specific inventory display.
 * It inherits all methods and functionality from the abstract InventoryDisplay class.
 */
public class CombatInventoryDisplay2 extends InventoryDisplay {

    public CombatInventoryDisplay2(Inventory inventory, int numCols) {
        super(inventory, numCols, inventory.getCapacity());
    }

    @Override
    protected void draw(SpriteBatch batch) {

    }
}
