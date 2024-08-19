package com.csse3200.game.components.player;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.csse3200.game.inventory.Inventory;
import com.csse3200.game.inventory.items.AbstractItem;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PlayerInventoryDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(PlayerInventoryDisplay.class);
    private final Inventory inventory;
    private final int numCols, numRows;
    private Window window;
    private Table table;
    private Table selectedSlot;

    /**
     * Constructor for a Player Inventory // TODO!!!!
     * Must have capacity = xRange * yRange
     *
     * @param capacity TODO
     * @param numCols TODO
     */
    public PlayerInventoryDisplay(int capacity, int numCols) {
        if (numCols < 1) {
            throw new IllegalArgumentException("Inventory dimensions must be positive!");
        }
        if (capacity % numCols != 0) { // TODO: WHAT IS THE RIGHT EXCEPTION TO THROW HERE?
            String msg = String.format("numCols (%d) must divide capacity (%d)", numCols, capacity);
            throw new NumberFormatException(msg);
        }

        this.inventory = new Inventory(capacity);
        this.numCols = numCols;
        this.numRows = capacity / numCols;

//        // TODO: MOVE THIS INTO THE PLAYER CLASS MAYBE? NOT SURE WHETHER PLAYER SHOULD HAVE THIS
//        //  OR INVENTORY SHOULD HAVE THIS!
//        this.inputComponent = new PlayerInventoryInputComponent(
//                numRows, numCols, 100, 100, 300, 300);
    }

    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener("toggleInventory", this::toggleInventory);
        entity.getEvents().addListener("slotClicked", this::handleSlotClicked);
    }

    private void toggleInventory() {
        if (stage.getActors().contains(window, true)) {
            logger.info("Inventory toggled off.");
            stage.getActors().removeValue(window, true); // close inventory
            disposeWindow();
        } else {
            logger.info("Inventory toggled on.");
            generateWindow();
            stage.addActor(window);
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        // Handled by stage
    }

    private void disposeWindow() {
        // Delete old window
        if (window != null) {
            window.clear();
            window.remove();
            window = null;
        }
    }

    private void generateWindow() {
        // Create the window (pop-up)
        window = new Window("Inventory", skin);

        // Create the table for inventory slots
        table = new Table();
        table.setFillParent(true);

        // Add the inventoryTable to the window
        window.add(table).expand().fill();

        // Iterate over the inventory and add slots

        Drawable slotBackground = skin.getDrawable("slot-background");
        final Drawable slotHighlight = skin.getDrawable("slot-selected");

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                int index = row * numCols + col;
                AbstractItem item = inventory.getAt(index);

                // Create the slot with a background
                final Table slot = new Table();
                slot.setBackground(slotBackground);

                // Add the item image to the slot TODO: ADD ITEM TEXTURES!
                if (item != null) {
                    Image itemImage = new Image(new Texture("images/box_boy.png"));
                    slot.add(itemImage).center().size(100, 100);

                    // TODO: ADD ITEM DESCRIPTION TO ABSTRACT ITEM!
                    // TODO: FIGURE OUT HOW TO DO ITEM DESCRIPTION WITH HOVERING
                    // Tooltip for item description
//                    String description = "Hi my name is";
//                    Label tooltipLabel = new Label(description, new Label.LabelStyle(skin.getFont("default-font"), Color.WHITE));
//                    Table x = new Table();
//                    x.add(tooltipLabel);
//                    Tooltip<Table> tooltip = new Tooltip<>(x);
//                    entity.getEvents().addListener(tooltip);
                }

                // Add the slot to the inventory table
                table.add(slot).size(120, 120).pad(5);

            }
            table.row(); // Move to the next row in the table
        }

        window.pack();
        // Set position in stage center
        window.setPosition(
                (stage.getWidth() - window.getWidth()) / 2,
                (stage.getHeight() - window.getHeight())
        );
    }

    // TODO: CHANGE THIS TO CALCULATE ROW/COL RATHER THAN X/Y
    // TODO: ALSO NEED TO CHANGE TO IGNORE PADDING (IE IF SOMEONE CLICKS BETWEEN ITEMS IGNORE THIS!)
    private void handleSlotClicked(int sX, int sY) {
        String msg1 = String.format("Received slot clicked at position (%d, %d)", sX, sY);

        Vector2 coords = table.stageToLocalCoordinates(new Vector2(sX, sY));
        int x = (int) coords.x;
        int y = (int) coords.y;
        String msg2 = String.format("These correspond to coordinates (%d, %d) of the table", x,
                y);

        int tX = x / 130;
        int tY = - (1 + (y / 130));
        String msg3 = String.format("These correspond to the slot at (%d, %d)", tX, tY);

        String isIn = (tX >= 0 && tY >= 0 && tX < numCols && tY < numRows) ? "in" : "out";
        String msg4 = "This is " + isIn + " of the inventory";

        String msg = String.join("\n", new String[]{msg1, msg2, msg3, msg4});
        logger.info(msg);
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

    @Override
    public void dispose() {
        disposeWindow();
        super.dispose();
    }
}
