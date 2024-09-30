package com.csse3200.game.components.combat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.components.inventory.InventoryUtils;
import com.csse3200.game.components.inventory.PlayerInventoryDisplay;
import com.csse3200.game.inventory.Inventory;
import com.csse3200.game.inventory.items.AbstractItem;
import com.csse3200.game.inventory.items.ItemUsageContext;
import com.csse3200.game.inventory.items.TimedUseItem;
import com.csse3200.game.inventory.items.potions.AttackPotion;
import com.csse3200.game.inventory.items.potions.DefensePotion;
import com.csse3200.game.inventory.items.potions.SpeedPotion;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CombatInventoryDisplay is a UI component that displays the player's inventory in a grid format.
 * It creates a window with a table of inventory slots that can display items, handle item usage,
 * and update dynamically when the inventory changes.
 */
public class CombatInventoryDisplay extends PlayerInventoryDisplay {
    private static final Logger logger = LoggerFactory.getLogger(CombatInventoryDisplay.class);
    private static final float Z_INDEX = 3f;
    private Window inventoryDisplay;
    private Table table;
    private boolean toggle = false; // Whether inventory is toggled on;

    /**
     * Constructs a CombatInventoryDisplay with the specified capacity and number of columns.
     * The capacity must be evenly divisible by the number of columns.
     * Inventory used as the underlying data structure should be the SAME as the one used in PlayerInventoryDisplay.
     * This means that, while used in a different display, any changes made during combat will carry over to the main
     * game.
     *
     * @param inventory The inventory from which to build the display.
     * @param numCols  The number of columns in the inventory display.
     * @throws IllegalArgumentException if numCols is less than 1 or if capacity is not divisible by numCols.
     */
    public CombatInventoryDisplay(Inventory inventory, int numCols, int hotBarCapacity) {
        super(inventory, numCols, hotBarCapacity);
    }

    /**
     * Initializes the component by setting up event listeners for toggling the inventory display
     * and adding items.
     */
    @Override
    public void create() {
        entity.getEvents().addListener("toggleCombatInventory", this::toggleDisplay);
        entity.getEvents().addListener("itemMove", this::addSlotListeners);
    }

    /**
     * Toggles the inventory display on or off based on its current state.
     */
    @Override
    public void toggleDisplay() {
        logger.debug(String.format("CombatInventoryDisplay toggled. Display is: %s", getToggleState()));
        if (stage.getActors().contains(inventoryDisplay, true)) {
            logger.debug("Inventory toggled off.");
            stage.getActors().removeValue(inventoryDisplay, true); // close inventory
            toggle = false;
        } else {
            logger.debug("Inventory toggled on.");
            generateInventory();
            stage.addActor(inventoryDisplay);
            toggle = true;
        }
    }

    /**
     * Determines if the toggle is active
     * @return returns the toggle
     */
    public boolean getToggleState() {
        return toggle;
    }

    /**
     * Generates the inventory window and populates it with inventory slots.
     */
    @Override
    public void generateInventory() {
        super.generateInventory();
    }

    /**
     * Adds listeners to the inventory slots for handling hover and click events.
     * This allows items to be used and inventory to be regenerated.
     *
     * @param slot  The ImageButton representing the inventory slot.
     * @param item  The item in the slot.
     * @param index The index of the slot in the inventory.
     */
    @Override
    public void addSlotListeners(ImageButton slot, AbstractItem item, int index) {
        slot.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                //double calls when mouse held, to be fixed
                if (item instanceof SpeedPotion) {
                    String[][] itemText = {{((AttackPotion) item).getWarning()}};
                    ServiceLocator.getDialogueBoxService().updateText(itemText);
                } else {
                    String[][] itemText = {{item.getDescription() + ". Quantity: "
                            + item.getQuantity() + "/" + item.getLimit()}};
                    ServiceLocator.getDialogueBoxService().updateText(itemText);

                }
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                ServiceLocator.getDialogueBoxService().hideCurrentOverlay();
            }
        });

        slot.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("Item {} was used", item.getName());
                ItemUsageContext context = new ItemUsageContext(entity);
                if (!(item instanceof SpeedPotion)) {
                    inventory.useItemAt(index, context);
                    entity.getEvents().trigger("itemUsed", item);
                }
                regenerateDisplay();
            }
        });
    }

    /**
     * Regenerates the inventory display by toggling it off and on.
     * This method is used to refresh the inventory UI without duplicating code.
     */
    @Override
    public void regenerateDisplay() {
        toggleDisplay();
        toggleDisplay();
    }

    /**
     * Disposes of the resources used by the component, including the window, table, and slots.
     */
    @Override
    public void dispose() {
        if (inventoryDisplay != null) {
            InventoryUtils.disposeGroupRecursively(inventoryDisplay);
            inventoryDisplay =null;
        }
    }
}
