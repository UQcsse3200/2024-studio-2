package com.csse3200.game.components.combat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.components.inventory.InventoryUtils;
import com.csse3200.game.inventory.Inventory;
import com.csse3200.game.inventory.items.AbstractItem;
import com.csse3200.game.inventory.items.ItemUsageContext;
import com.csse3200.game.inventory.items.potions.AttackPotion;
import com.csse3200.game.inventory.items.potions.DefensePotion;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PlayerInventoryDisplay is a UI component that displays the player's inventory in a grid format.
 * It creates a window with a table of inventory slots that can display items, handle item usage,
 * and update dynamically when the inventory changes.
 */
public class CombatInventoryDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(CombatInventoryDisplay.class);
    private final Inventory inventory;
    private static final float Z_INDEX = 3f;
    private final int numCols, numRows, hotBarCapacity;
    private Window inventoryDisplay;
    private Table table;
    private final ImageButton[] slots;
    private boolean toggle = false; // Whether inventory is toggled on;
    private final Skin inventorySkin = new Skin(Gdx.files.internal("Inventory/inventory.json"));
    private final Skin slotSkin = new Skin(Gdx.files.internal("Inventory/skinforslot.json"));

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
        if (numCols < 1) {
            String msg = String.format("numCols (%d) must be positive", numCols);
            throw new IllegalArgumentException(msg);
        }

        if (hotBarCapacity < 1) {
            String msg = String.format("hotBarCapacity (%d) must be positive", hotBarCapacity);
            throw new IllegalArgumentException(msg);
        }
        this.hotBarCapacity = hotBarCapacity;

        int capacity = inventory.getCapacity() - hotBarCapacity;
        if (capacity % numCols != 0) {
            String msg = String.format("numCols (%d) must divide capacity (%d)", numCols, capacity);
            throw new IllegalArgumentException(msg);
        }
        this.inventory = inventory;
        this.numCols = numCols;
        this.numRows = capacity / numCols;
        slots = new ImageButton[numRows * numCols];
    }

    /**
     * Initializes the component by setting up event listeners for toggling the inventory display
     * and adding items.
     */
    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener("toggleCombatInventory", this::toggleInventory);
    }

    /**
     * Toggles the inventory display on or off based on its current state.
     */
    private void toggleInventory() {
        logger.info(String.format("CombatInventoryDisplay toggled. Display is: %s", getToggleState()));
        if (stage.getActors().contains(inventoryDisplay, true)) {
            logger.debug("Inventory toggled off.");
            stage.getActors().removeValue(inventoryDisplay, true); // close inventory
            toggle = false;
        } else {
            logger.debug("Inventory toggled on.");
            generateWindow();
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
     * Checks to see if inventory is full
     * @return boolean for if inventory is full
     */
    public boolean hasSpaceFor() {
        // Logic to check if there's space in the inventory for all the items
        // For simplicity, assume each item takes one slot and check if enough slots are available
        return inventory.isFull();
    }

    /**
     * Handles drawing of the component. The actual rendering is managed by the stage.
     *
     * @param batch The SpriteBatch used for drawing.
     */
    @Override
    public void draw(SpriteBatch batch) {
        // Handled by stage
    }

    /**
     * Generates the inventory window and populates it with inventory slots.
     */
    private void generateWindow() {
        // Create the window (pop-up)
        inventoryDisplay = new Window("Inventory", inventorySkin);
        Label.LabelStyle titleStyle = new Label.LabelStyle(inventoryDisplay.getTitleLabel().getStyle());
        titleStyle.fontColor = Color.BLACK;
        inventoryDisplay.getTitleLabel().setAlignment(Align.center);
        inventoryDisplay.getTitleTable().padTop(150); // Adjust the value to move the title lower
        // Create the table for inventory slots
        inventoryDisplay.getTitleLabel().setStyle(titleStyle);
        table = new Table();
        inventoryDisplay.getTitleTable().padBottom(20);
        // Iterate over the inventory and add slots
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                int index = row * numCols + col;
                AbstractItem item = inventory.getAt(index);
                // Create the slot with the inventory background
                final ImageButton slot = new ImageButton(slotSkin);
                // Add the item image to the slot
                if (item != null) {
                    addSlotListeners(slot, item, index);
                    Image itemImage = new Image(new Texture(item.getTexturePath()));
                    slot.add(itemImage).center().size(80, 80);
                }
                table.add(slot).size(90, 90).pad(5); // Add the slot to the table
                slots[index] = slot;
            }
            table.row(); // Move to the next row in the table
        }
        // Add the table to the window
        inventoryDisplay.add(table).expand().fill();
        inventoryDisplay.pack();
        // Set position in stage top-center
        inventoryDisplay.setPosition(
                (stage.getWidth() - inventoryDisplay.getWidth()) / 2,  // Center horizontally
                (stage.getHeight() - inventoryDisplay.getHeight()) / 2 // Center vertically
        );
    }

    /**
     * Adds listeners to the inventory slots for handling hover and click events.
     * This allows items to be used and inventory to be regenerated.
     *
     * @param slot  The ImageButton representing the inventory slot.
     * @param item  The item in the slot.
     * @param index The index of the slot in the inventory.
     */
    public void addSlotListeners(ImageButton slot, AbstractItem item, int index) {
        // Add hover listener for highlighting and showing the message
        slot.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                //double calls when mouse held, to be fixed
                String[][] itemText = {{item.getDescription() + ". Quantity: "
                        + item.getQuantity() + "/" + item.getLimit()}};
                ServiceLocator.getDialogueBoxService().updateText(itemText);
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
                inventory.useItemAt(index, context);
                entity.getEvents().trigger("itemUsed", item);
                entity.getEvents().trigger("toggleCombatInventory");
                regenerateInventory();
            }
        });
    }

    /**
     * Return the player inventory display
     * @return player inventory display
     */
    public CombatInventoryDisplay getCombatInventoryDisplay() {
        return this;
    }

    /**
     * Removes an item from the inventory and updates the display.
     *
     * @param item The item to be removed from the inventory.
     */
    public void removeItem(AbstractItem item) {
        inventory.deleteItem(item.getItemCode());
        generateWindow();
    }

    /**
     * Regenerates the inventory display by toggling it off and on.
     * This method is used to refresh the inventory UI without duplicating code.
     */
    void regenerateInventory() {
        if (toggle) {
            toggleInventory(); // Hacky way to regenerate inventory without duplicating code
            toggleInventory();
        }
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
        super.dispose();
    }

    /**
     * Disposes of the inventory table, clearing its contents and removing it from the stage.
     */
    private void disposeTable() {
        if (table != null) {
            table.clear();
            table.remove();
            table = null;
        }
    }

    /**
     * @return The z-index for this component.
     */
    @Override
    public float getZIndex() {
        return Z_INDEX;
    }

    /**
     * Loads the inventory attached to the player from a save.
     */
    public void loadInventoryFromSave() {
        inventory.loadInventoryFromSave();
    }
  
    /** Returns inventory - for quests. */
    public Inventory getInventory() {
        return inventory;
    }
}
