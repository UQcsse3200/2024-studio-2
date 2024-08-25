package com.csse3200.game.components.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.csse3200.game.inventory.Inventory;
import com.csse3200.game.inventory.items.AbstractItem;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static com.csse3200.game.utils.math.EuclideanDivision.mod;


// TODO: HANDLE DISPOSAL OF TABLES!!!
// TODO: MAKE SLOT SIZE (IE INVENTORY SIZE) NOT A CONSTANT IN GENERATE WINDOW - MAKE IT A CLASS
//  CONSTANT!!!
public class PlayerInventoryDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(PlayerInventoryDisplay.class);
    private final Inventory inventory;
    private static final float Z_INDEX = 3f;
    private final int numCols, numRows;
    private Window window;
    private Table table;
    private int selectedSlot = -1;
    private final ImageButton[] slots;
    private final Drawable slotBackground = skin.getDrawable("slot-background");
    private final Drawable slotHighlight = skin.getDrawable("slot-selected");

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
        if (capacity % numCols != 0) { // TODO: WHAT IS THE RIGHT EXCEPTION TO THROW HERE?
            String msg = String.format("numCols (%d) must divide capacity (%d)", numCols, capacity);
            throw new NumberFormatException(msg);
        }

        this.inventory = new Inventory(capacity);
        this.numCols = numCols;
        this.numRows = capacity / numCols;
        slots = new ImageButton[numRows * numCols];

        // TODO: MOVE THIS INTO THE PLAYER CLASS MAYBE? NOT SURE WHETHER PLAYER SHOULD HAVE THIS
        //  OR INVENTORY SHOULD HAVE THIS!
//        this.inputComponent = new PlayerInventoryInputComponent(
//                numRows, numCols, 100, 100, 300, 300);
    }

    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener("toggleInventory", this::toggleInventory);
        entity.getEvents().addListener("slotClicked", this::handleSlotClicked);
        entity.getEvents().addListener("addItem", this::addItem);
    }

    private void toggleInventory() {
        if (stage.getActors().contains(window, true)) {
            logger.debug("Inventory toggled off.");
            stage.getActors().removeValue(window, true); // close inventory
            disposeWindow();
        } else {
            logger.debug("Inventory toggled on.");
            generateWindow();
            stage.addActor(window);
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
        table.setFillParent(true);

        // Iterate over the inventory and add slots
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                int index = row * numCols + col;
                AbstractItem item = inventory.getAt(index);

                // Create the slot with the inventory background
                final ImageButton slot = new ImageButton(skin);
                slot.setBackground(slotBackground);

                // Add the item image to the slot TODO: ADD ITEM TEXTURES!
                if (item != null) {
                    Image itemImage = new Image(new Texture("images/box_boy.png"));
                    slot.add(itemImage).center().size(100, 100);
                }

                // Add hover listener for highlighting
                slot.addListener(new InputListener() {
                    @Override
                    public boolean mouseMoved(InputEvent event, float x, float y) {
                        slot.setBackground(slotHighlight); // Highlight slot on hover
                        return true;
                    }

                    @Override
                    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                        slot.setBackground(slotBackground); // Revert to default background when mouse exits
                    }
                });

                table.add(slot).size(120, 120).pad(5); // Add the slot to the table
                slots[index] = slot;
            }
            table.row(); // Move to the next row in the table
        }


        TextButton startBtn = new TextButton("Start", skin);
        addButtonElevationEffect(startBtn);

        startBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("Start button clicked");
                entity.getEvents().trigger("start");
            }
        });
        table.add(startBtn).padTop(30f).width(200f).height(60f);

        // Add the table to the window
        window.add(table).expand().fill();

        // Set position in stage top-center
        window.pack();
        window.setPosition(
                (stage.getWidth() - window.getWidth()) / 2,
                (stage.getHeight() - window.getHeight())
        );

    }

    private void addButtonElevationEffect(TextButton button) {
        button.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                button.addAction(Actions.parallel(
                        Actions.moveBy(0, 5, 0.1f),
                        Actions.scaleTo(1.05f, 1.05f, 0.1f)
                ));
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                button.addAction(Actions.parallel(
                        Actions.moveBy(0, -5, 0.1f),
                        Actions.scaleTo(1f, 1f, 0.1f)
                ));
            }
        });
    }

    private void handleSlotClicked(int sX, int sY) {
        // De-select previously selected slot
        if (selectedSlot != -1) {
            slots[selectedSlot].setBackground(slotBackground);
        }

        selectedSlot = screenPosToClickedSlot(sX, sY); // Find newly selected slot

        // Highlight the selected slot
        if (selectedSlot != -1) {
            slots[selectedSlot].setBackground(slotHighlight);
        }
    }

    /**
     * Converts screen coordinates to the index of the clicked slot in the inventory table.
     *
     * @param sX the x-coordinate of the screen position
     * @param sY the y-coordinate of the screen position
     * @return the index of the clicked slot, or -1 if the click is outside the table or within the
     * padded area (ie the user clicked on a position where a slot doesn't exist)
     */
    private int screenPosToClickedSlot(int sX, int sY) {
        // Convert to table coordinates
        Vector2 localXY = table.stageToLocalCoordinates(new Vector2(sX, sY));
        int x = (int) localXY.x;
        int y = (int) localXY.y;

        // Check position not in padded layer
        if (mod(x, 130) < 5 || mod(x, 130) >= 125 || mod(y, 130) < 5 || mod(y, 130) >= 125) {
            return -1;
        }

        // Convert to table row/col (0 indexed from top left corner)
        int row = numRows + (y / 130); // y coordinate starts at top left of table
        int col = x / 130;

        // Check row/col is in table
        if (row < 0 || col < 0 || row >= numRows || col >= numCols) {
            return -1;
        }

        String msg = String.format("Clicked at row/col (%d, %d)", row, col);
        logger.debug(msg); // For debugging purposes

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

    private void addItem(AbstractItem item) {
        if (this.inventory.add(item)) {
            entity.getEvents().trigger("itemPickedUp", true);
        } else {
            entity.getEvents().trigger("itemPickedUp", false);
        }
    }

    @Override
    public void dispose() {
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

    // TODO - FIGURE OUT IF THIS FUNCTION IS NEEDED!!!!
    private void disposeTable(Table table) {
//        if (table != null) {
//            // Remove the table from its parent (e.g., a window or stage)
//            Actor parent = table.getParent();
//            if (parent != null) {
//                parent.removeActor(table);
//            }
//
//            // Dispose of any children actors (if they are no longer needed)
//            for (Actor actor : table.getChildren()) {
//                if (actor instanceof Disposable) {
//                    ((Disposable) actor).dispose();
//                }
//            }
//
//            // Clear all children
//            table.clear();
//
//            // Optional: Set the table reference to null
//            table = null;
//        }
    }

    @Override
    public float getZIndex() {
        return Z_INDEX;
    }


    private void addHoverHighlighter(ImageButton button) {
        button.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                button.addAction(Actions.parallel(
                        Actions.moveBy(0, 5, 0.1f),
                        Actions.scaleTo(1.05f, 1.05f, 0.1f)
                ));
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                button.addAction(Actions.parallel(
                        Actions.moveBy(0, -5, 0.1f),
                        Actions.scaleTo(1f, 1f, 0.1f)
                ));
            }
        });
    }

    private void createButton() {
        // Create the ImageButtonStyle
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = skin.getDrawable("button-up");
        style.down = skin.getDrawable("button-down");
        style.checked = skin.getDrawable("button-checked");

        // Create the ImageButton using the style
        ImageButton imageButton = new ImageButton(style);

        // Set a listener to handle button clicks
        addHoverHighlighter(imageButton);

        // Add the ImageButton to a stage or table
            stage.addActor(imageButton);
    }
}
