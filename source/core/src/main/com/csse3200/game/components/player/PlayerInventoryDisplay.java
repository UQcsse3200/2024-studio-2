package com.csse3200.game.components.player;

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
import com.csse3200.game.inventory.Inventory;
import com.csse3200.game.inventory.items.AbstractItem;
import com.csse3200.game.ui.DialogueBox;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PlayerInventoryDisplay is a UI component that displays the player's inventory in a grid format.
 * It creates a window with a table of inventory slots that can display items, handle item usage,
 * and update dynamically when the inventory changes.
 */
public class PlayerInventoryDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(PlayerInventoryDisplay.class);
    private final Inventory inventory;
    private static final float Z_INDEX = 3f;
    private final int numCols, numRows;
    private Window window;
    private Table table;
    private final ImageButton[] slots;
    private boolean toggle = false; // Whether inventory is toggled on;
    DialogueBox itemOverlay;


    private final Skin skininv=new Skin(Gdx.files.internal("Inventory/inventory.json"));
    private final Skin skinSlots=new Skin(Gdx.files.internal("Inventory/skinforslot.json"));

    PlayerInventoryHotbarDisplay hotbar;




    /**
     * Constructs a PlayerInventoryDisplay with the specified capacity and number of columns.
     * The capacity must be evenly divisible by the number of columns.
     *
     * @param capacity The total number of slots in the inventory.
     * @param numCols  The number of columns in the inventory display.
     * @throws IllegalArgumentException if numCols is less than 1 or if capacity is not divisible by numCols.
     */
    public PlayerInventoryDisplay(int capacity, int numCols) {
        if (numCols < 1 || capacity < 1) {
            throw new IllegalArgumentException("Inventory dimensions must be positive!");
        }
        if (capacity % numCols != 0) {
            String msg = String.format("numCols (%d) must divide capacity (%d)", numCols, capacity);
            throw new IllegalArgumentException(msg);
        }

        this.inventory = new Inventory(capacity);
        this.hotbar= new PlayerInventoryHotbarDisplay(5,inventory,this);
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
        entity.getEvents().addListener("toggleInventory", this::toggleInventory);
        entity.getEvents().addListener("addItem", this::addItem);
    }

    /**
     * Toggles the inventory display on or off based on its current state.
     */
    private void toggleInventory() {
        hotbar.toggleHotbar();
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
        window = new Window("Inventory",skininv);
        Label.LabelStyle titleStyle = new Label.LabelStyle(window.getTitleLabel().getStyle());
        titleStyle.fontColor = Color.BLACK;
        window.getTitleLabel().setAlignment(Align.center);
        window.getTitleTable().padTop(150); // Adjust the value to move the title lower
        // Create the table for inventory slots
        window.getTitleLabel().setStyle(titleStyle);
        table = new Table();
        window.getTitleTable().padBottom(20);


        // Iterate over the inventory and add slots
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                int index = row * numCols + col;
                AbstractItem item = inventory.getAt(index);

                // Create the slot with the inventory background
                final ImageButton slot = new ImageButton(skinSlots);

                // final ImageButton slot = new ImageButton(skin, "inventory-slot");

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
        window.add(table).expand().fill();

        window.pack();
        // Set position in stage top-center
        window.setPosition(
                (stage.getWidth() - window.getWidth()) / 2,  // Center horizontally
                (stage.getHeight() - window.getHeight()) / 2 // Center vertically
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
    void addSlotListeners(ImageButton slot, AbstractItem item, int index) {
        // Add hover listener for highlighting and showing the message
        slot.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                //double calls when mouse held, to be fixed
                disposeItemOverlay();
                    String[] itemText = {item.getDescription() + ". Quantity: "
                            + item.getQuantity() + "/" + item.getLimit()};
                    itemOverlay = new DialogueBox(itemText);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                disposeItemOverlay();
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
     * Removes an item from the inventory and updates the display.
     *
     * @param item The item to be removed from the inventory.
     */
    public void removeItem(AbstractItem item) {
        inventory.deleteItem(item.getItemCode());
        generateWindow();
    }

    /**
     * Adds an item to the inventory and triggers an event if successful.
     *
     * @param item The item to be added to the inventory.
     */
    private void addItem(AbstractItem item) {
        if (this.inventory.add(item)) {
            entity.getEvents().trigger("itemPickedUp", true);
        } else {
            entity.getEvents().trigger("itemPickedUp", false);
        }
        regenerateInventory();
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
        hotbar.toggleHotbar(); // Hacky way to regenerate hotbar without duplicating code
        hotbar.toggleHotbar();
    }

    /**
     * Disposes of the resources used by the component, including the window, table, and slots.
     */
    @Override
    public void dispose() {
        disposeSlots();
        disposeTable();
        disposeWindow();
        disposeItemOverlay();
        super.dispose();
    }

    /**
     * Disposes of the inventory window, clearing its contents and removing it from the stage.
     */
    private void disposeWindow() {
        // Delete old window
        if (window != null) {
            window.clear();
            window.remove();
            window = null;
        }
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
     * Disposes of the inventory item overlay pop up.
     */
    private void disposeItemOverlay() {
        if (itemOverlay != null) {
            itemOverlay.dispose();
            itemOverlay = null;
        }
    }

    /**
     * Disposes of the inventory slots, clearing their contents and removing them from the stage.
     */
    private void disposeSlots() {
        for (int i = 0; i < inventory.getCapacity(); i++) {
            if (slots[i] != null) {
                slots[i].clear();
                slots[i].remove();
                slots[i] = null;
            }
        }
    }

    /**
     * @return The z-index for this component.
     */
    @Override
    public float getZIndex() {
        return Z_INDEX;
    }
}