package com.csse3200.game.components.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.inventory.Inventory;
import com.csse3200.game.inventory.items.AbstractItem;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO: MAKE SLOT SIZE (IE INVENTORY SIZE) NOT A CONSTANT IN GENERATE WINDOW - MAKE IT A CLASS
//  CONSTANT!!!
public class PlayerInventoryDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(PlayerInventoryDisplay.class);
    private final Inventory inventory;
    private static final float Z_INDEX = 3f;
    private final int numCols, numRows;
    private Window window;
    private Table table;
    private final ImageButton[] slots;
    private boolean toggle = false; // Whether inventory is toggled on;

    /**
     * Constructor for a Player Inventory // TODO!!!!
     * Must have capacity = xRange * yRange
     *
     * @param capacity TODO
     * @param numCols  TODO
     */
    public PlayerInventoryDisplay(int capacity, int numCols) {
        if (numCols < 1) {
            throw new IllegalArgumentException("Inventory dimensions must be positive!");
        }
        if (capacity % numCols != 0) {
            String msg = String.format("numCols (%d) must divide capacity (%d)", numCols, capacity);
            throw new IllegalArgumentException(msg);
        }

        this.inventory = new Inventory(capacity);
        this.numCols = numCols;
        this.numRows = capacity / numCols;
        slots = new ImageButton[numRows * numCols];
    }

    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener("toggleInventory", this::toggleInventory);
        entity.getEvents().addListener("addItem", this::addItem);
    }

    private void toggleInventory() {
        if (stage.getActors().contains(window, true)) {
            logger.debug("Inventory toggled off.");
            stage.getActors().removeValue(window, true); // close inventory
            disposeWindow();
            toggle = false;
        } else {
            logger.debug("Inventory toggled on.");
            generateWindow();
            stage.addActor(window);
            toggle = true;
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        // Handled by stage
    }

    private void generateWindow() {
        // Create the window (pop-up)
        window = new Window("Inventory", skin);

        // Create the table for inventory slots
        table = new Table();

        // Iterate over the inventory and add slots
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                int index = row * numCols + col;
                AbstractItem item = inventory.getAt(index);

                // Create the slot with the inventory background
                final ImageButton slot = new ImageButton(skin);

                // final ImageButton slot = new ImageButton(skin, "inventory-slot");
                // TODO: ADD INVENTORY STYLE - this requires adding these images to the skin!

                // Add the item image to the slot
                if (item != null) {
                    addSlotListeners(slot, item, index);
                    Image itemImage = new Image(new Texture(item.getTexturePath()));
                    slot.add(itemImage).center().size(100, 100);
//
//                    // Create the label to show quantity/limit
//                    String quantityLimitText = String.format("%d/%d", item.getQuantity(), item.getLimit());
//                    Label quantityLimitLabel = new Label(quantityLimitText, skin);
//                    slot.add(quantityLimitLabel).center();
                }

                table.add(slot).size(120, 120).pad(5); // Add the slot to the table
                slots[index] = slot;
            }
            table.row(); // Move to the next row in the table
        }

        // Add the table to the window
        window.add(table).expand().fill();

        window.pack();
        // Set position in stage top-center
        window.setPosition(
                (stage.getWidth() - window.getWidth()) / 2,
                (stage.getHeight() - window.getHeight() - 25)
        );
    }

    /**
     * This code was partially inspired by the code generated the highlighting of buttons on the
     * main menu screen. TODO: These should be abstracted away into a utility class!
     */
    private void addSlotListeners(ImageButton slot, AbstractItem item, int index) {
        // Add hover listener for highlighting and showing the message
        slot.addListener(new InputListener() {
            @Override
            public boolean mouseMoved(InputEvent event, float x, float y) {
                return true;
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
            }
        });

        slot.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("Item {} was used", item.getName());
                inventory.useItemAt(index, null);
                regenerateInventory();
            }
        });
    }

    /**
     * Removes the item from the inventory when player deletes are uses up an item
     *
     * @param item to be deleted in inventory
     */
    public void removeItem(AbstractItem item) {
        inventory.deleteItem(item.getItemCode());
        generateWindow();
    }

    private void addItem(AbstractItem item) {
        if (this.inventory.add(item)) {
            entity.getEvents().trigger("itemPickedUp", true);
        } else {
            entity.getEvents().trigger("itemPickedUp", false);
        }
        regenerateInventory();
    }

    private void regenerateInventory() {
        if (toggle) {
            toggleInventory(); // Hacky way to regenerate inventory without duplicating code
            toggleInventory();
        }
    }

    @Override
    public void dispose() {
        disposeSlots();
        disposeTable();
        disposeWindow();
        super.dispose();
    }

    private void disposeWindow() {
        // Delete old window
        if (window != null) {
            window.clear();
            window.remove();
            window = null;
        }
    }

    private void disposeTable() {
        if (table != null) {
            table.clear();
            table.remove();
            table = null;
        }
    }

    private void disposeSlots() {
        for (int i = 0; i < inventory.getCapacity(); i++) {
            if (slots[i] != null) {
                slots[i].clear();
                slots[i].remove();
                slots[i] = null;
            }
        }
    }

    @Override
    public float getZIndex() {
        return Z_INDEX;
    }


}
