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

import static com.csse3200.game.utils.math.EuclideanDivision.mod;


public class PlayerInventoryDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(PlayerInventoryDisplay.class);
    private final Inventory inventory;
    private final int numCols, numRows;
    private Window window;
    private Table table;
    private int selectedSlot = -1;
    private final Table[] slots;
    private final Drawable slotBackground = skin.getDrawable("slot-background");
    private final Drawable slotHighlight = skin.getDrawable("slot-selected");

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
        slots =  new Table[numRows * numCols];

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
        // TODO: Maybe just iterate to numRows * numCols
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
                slots[index] = slot;
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

    private void handleSlotClicked(int sX, int sY) {
        if (selectedSlot != -1) { // De-select previously selected slot
            slots[selectedSlot].setBackground(slotBackground);
        }

        selectedSlot = screenPosToClickedSlot(sX, sY); // Find newly selected slot

        if (selectedSlot != -1) { // Highlight the selected slot
            slots[selectedSlot].setBackground(slotHighlight);

            String msg = String.format("Clicked slot at index %d", selectedSlot);
            logger.info(msg);
        }
    }

    // Returns slot index if clicked, otherwise -1
    // TODO: Decide whether to return index or slot itself
    // TODO: NEED TO CLEAN UP THE MAPPING A LOT - FOR NOW JUST GET IT WORKING!
    // TODO: Probably should add some of the math operations to the math util package
    private int screenPosToClickedSlot(int sX, int sY) {
        // Convert to table coordinates
        Vector2 localXY = table.stageToLocalCoordinates(new Vector2(sX, sY));
        int x = (int) localXY.x;
        int y = (int) localXY.y;
        String msg1 = String.format("Received click at (%d, %d)", x, y);
        logger.info(msg1);

        // Check position not in padded layer.
        if (mod(x, 130) < 5 || mod(x, 130) >= 125 || mod(y, 130) < 5 || mod(y, 130) >= 125) {
            logger.info("Click in padded layer (or out of bounds)");
            return -1;
        }

        // Convert to table row/col (0 indexed from top left corner)
        int row = numRows + (y / 130); // y coordinate starts at top left of table
        int col = x / 130;
        String msg2 = String.format("Clicked at row/col (%d, %d)", row, col);
        logger.info(msg2);

        // Check row/col is in table
        if (row < 0 || col < 0 || row >= numRows || col >= numCols) {
            logger.info("Clicked out of bounds");
            return -1;
        }

        // Convert to slot index
        return row * numCols + col;
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
