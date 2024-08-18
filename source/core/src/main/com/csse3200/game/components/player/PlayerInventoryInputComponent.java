package com.csse3200.game.components.player;

import com.csse3200.game.input.InputComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerInventoryInputComponent extends InputComponent {
    private static final Logger logger = LoggerFactory.getLogger(PlayerInventoryInputComponent.class);
    private boolean inventoryOpen = false;
    private final int numRows, numCols, slotWidth, slotHeight, windowX, windowY;

    public PlayerInventoryInputComponent(int capacity, int numCols

//            int numRows,
//            int numCols,
//            int slotHeight,
//            int slotWidth,
//            int windowX,
//            int windowY
    ) {
        this.numCols = numCols;
        this.numRows = capacity / numCols;
        this.slotHeight = 100;
        this.slotWidth = 100;
        this.windowX = 300;
        this.windowY = 300;
    }

    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener("toggleInventory", this::toggleInventory);
    }

    private void toggleInventory() {
        logger.info("Inventory toggled.");
        inventoryOpen = !inventoryOpen;
    }

    // TODO: FIGURE OUT WHEN THIS SHOULD RETURN FALSE, AND DECIDE HOW TO DETERMINE WHEN THE MOUSE
    //  MOVES OFF THE SLOT!!!!
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        if (inventoryOpen) {
            logger.info("MOUSE MOVED");
            // Handle hover event, e.g., show item description
            handleHover(screenX, screenY);
        }
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (inventoryOpen) {
            logger.info("TOUCH DOWN");
            int clickedSlot = getClickedSlot(screenX, screenY);
            if (clickedSlot != -1) {
                entity.getEvents().trigger("slotClicked", clickedSlot);
                return true;
            }
        }
        return false;
    }

    // TODO: DO THE MAPPING CORRECTLY HERE!
    private int getClickedSlot(int screenX, int screenY) {
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                int x = col * slotWidth;
                int y = row * slotHeight;
                if (screenX >= x && screenX < x + slotWidth && screenY >= y && screenY < y + slotHeight) {
                    return row * numCols + col;
                }
            }
        }
        return -1;  // No slot clicked
    }

    // TODO: DEAL WITH THIS FOR TOOLTIP OR JUST TRIGGER EVENT!
    private void handleHover(int screenX, int screenY) {
        // Handle hover logic here (e.g., show item description in a tooltip)
    }
}