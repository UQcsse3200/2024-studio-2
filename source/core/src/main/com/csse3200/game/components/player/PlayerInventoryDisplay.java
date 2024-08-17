package com.csse3200.game.components.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.csse3200.game.inventory.Inventory;
import com.csse3200.game.inventory.items.AbstractItem;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
//import com.badlogic.gdx.utils.Array;


public class PlayerInventoryDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(PlayerInventoryDisplay.class);
    private final Inventory inventory;
    private final int xRange, yRange;
    private boolean toggle = false;
    private Window window = new Window("Inventory", skin);

    /**
     * Constructor for a Player Inventory // TODO!!!!
     * Must have capacity = xRange * yRange
     *
     * @param capacity TODO
     * @param xRange TODO
     * @param yRange TODO
     */
    public PlayerInventoryDisplay(int capacity, int xRange, int yRange) {
        this.inventory = new Inventory(capacity);
        this.xRange = xRange;
        this.yRange = yRange;
    }

    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener("toggleInventory", this::toggleInventory);
    }

    private void toggleInventory() {
        logger.info("Inventory toggled.");
        if (stage.getActors().contains(window, true)) {
            logger.info("Inventory toggled off.");
            stage.getActors().removeValue(window, true); // close inventory
        } else {
            logger.info("Inventory toggled on.");
            updateWindow();
            stage.addActor(window);
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        // Handled by stage
    }

    private void updateWindow() {
        // Create the window (pop-up)
        window.clear();
        window.setSize(400, 400);  // Set appropriate size
        window.setPosition(100, 100);  // Set position on screen

        // Create the table for inventory slots
        Table table = new Table();
        table.setFillParent(true);

        // Add the inventoryTable to the window
        window.add(table).expand().fill();

        // Iterate over the inventory and add slots
        int numColumns = 9; // Example for 9 columns (like Minecraft)
//        int numRows = (int) Math.ceil(inventory.getCapacity() / (float) numColumns);
        int numRows = 1;

        Drawable slotBackground = skin.getDrawable("slot-background");
        // TODO: MAKE THE SLOTBACKGROUND LOAD IN PROPERLY!!!

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numColumns; col++) {
                int index = row * numColumns + col;
                if (index < inventory.getCapacity()) {
                    AbstractItem item = inventory.getAt(index);

                    // Create the slot with a background
                    Table slot = new Table();
                    slot.setBackground(slotBackground);

                    // Add the item image to the slot
                    Image itemImage = new Image(new Texture("images/box_boy.png"));
                    slot.add(itemImage).center().size(32, 32); // Adjust size to fit

                    // Add the slot to the inventory table
                    table.add(slot).size(50, 50).pad(5); // Adjust size and padding
                } else {
                    // Add an empty slot if no item exists for the cell
                    Table emptySlot = new Table();
                    emptySlot.setBackground(slotBackground);
                    table.add(emptySlot).size(50, 50).pad(5);
                }
            }
            table.row(); // Move to the next row in the table
        }
    }

    // TODO: Need to override dispose (with the new texture)

}
