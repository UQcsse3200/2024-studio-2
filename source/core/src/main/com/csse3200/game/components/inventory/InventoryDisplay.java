package com.csse3200.game.components.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.inventory.*;
import com.csse3200.game.inventory.items.AbstractItem;
import com.csse3200.game.inventory.items.ItemUsageContext;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract class for displaying an inventory. Subclasses can extend this to implement specific types of inventory
 * displays, such as player inventories, with additional features (e.g., hotbar, drag-and-drop).
 */
public abstract class InventoryDisplay extends UIComponent {
    protected static final Logger logger = LoggerFactory.getLogger(InventoryDisplay.class);
    private static final float Z_INDEX = 3f;

    protected final Inventory inventory;
    private Window inventoryDisplay;
    private Table hotBarDisplay;
    private DragAndDrop dragAndDrop;

    private final boolean hasHotBar;
    protected final int numCols;
    protected final int numRows;
    private final int hotBarCapacity;
    private boolean toggle = false; // Whether inventory is toggled on;
    private ImageButton[] slots;
    // TODO: HOLD SLOTS WHICH CAN BE UPDATED DIRECTLY
    //  SET AND REMOVE VISIBILITY RATHER THAN DISPOSING
    //  PUSH AND CHECK SPEEDS!

    // Skins (created by @PratulW5):
    private final Skin inventorySkin = new Skin(Gdx.files.internal("Inventory/inventory.json"));
    private final Skin slotSkin = new Skin(Gdx.files.internal("Inventory/skinforslot.json"));
    private final Texture hotBarTexture = new Texture("Inventory/hotbar.png");

    /**
     * Constructs a PlayerInventoryDisplay with the specified capacity and number of columns.
     * The capacity must be evenly divisible by the number of columns.
     *
     * @param inventory The inventory from which to build the display
     * @param numCols  The number of columns in the inventory display.
     * @throws IllegalArgumentException if numCols is less than 1 or if capacity is not divisible by numCols.
     */
    public InventoryDisplay(Inventory inventory, int numCols, int hotBarCapacity,
                            boolean displayHotBar) {
        if (numCols < 1) {
            String msg = String.format("numCols (%d) must be positive", numCols);
            throw new IllegalArgumentException(msg);
        }

        if (hotBarCapacity < 0) {
            String msg = String.format("hotBarCapacity (%d) must be positive", hotBarCapacity);
            throw new IllegalArgumentException(msg);
        }
        this.hotBarCapacity = hotBarCapacity;
        this.hasHotBar = displayHotBar;

        int capacity = inventory.getCapacity() - hotBarCapacity;
        if (capacity % numCols != 0) {
            String msg = String.format("numCols (%d) must divide capacity (%d)", numCols, capacity);
            throw new IllegalArgumentException(msg);
        }
        this.inventory = inventory;
        this.numCols = numCols;
        this.numRows = capacity / numCols;

        this.slots = new ImageButton[inventory.getCapacity()];
    }

    /**
     * Initializes the component by setting up event listeners for toggling the inventory display
     * and adding items.
     */
    @Override
    public void create() {
        super.create();
        dragAndDrop = new DragAndDrop();
        initDisplays();
        if (hasHotBar) {
            generateHotBar();
            hotBarDisplay.setVisible(false);
        }
        generateInventory();
        inventoryDisplay.setVisible(false);
        entity.getEvents().addListener(toggleMsg(), this::toggleDisplay);
        entity.getEvents().addListener("addItem", this::addItem);
    }

    private void initDisplays() {
        for (int i = 0; i < inventory.getCapacity(); i++) {
            createSlot(i);
        }
        // INVENTORY:
        // Create the inventory window (pop-up)
        inventoryDisplay = new Window("Inventory", inventorySkin);
        Label.LabelStyle titleStyle = new Label.LabelStyle(inventoryDisplay.getTitleLabel().getStyle());
        titleStyle.fontColor = Color.BLACK;
        inventoryDisplay.getTitleLabel().setAlignment(Align.center);
        inventoryDisplay.getTitleTable().padTop(150).padBottom(10);

        // Create the table for inventory slots
        inventoryDisplay.getTitleLabel().setStyle(titleStyle);

        stage.addActor(inventoryDisplay);
        inventoryDisplay.setVisible(false);

        // HOT-BAR:
        if (hasHotBar) {
            hotBarDisplay = new Table();
        }
    }

    public abstract String toggleMsg(); // The event to listen for to toggle the display

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
     * Determines if the toggle is active
     * @return returns the toggle
     */
    public boolean getToggle() {
        return toggle;
    }

    /**
     * Toggles the inventory display on or off based on its current state.
     */
    public void toggleDisplay() {
        toggle = !toggle;
        logger.debug("Inventory toggled " + (toggle ? "on." : "off."));
        inventoryDisplay.setVisible(toggle);
    }

    /**
     * Sets up drag-and-drop functionality for the inventory system using the provided slot and item.
     * The method defines both the drag source and the drop target, allowing users to drag items between slots.
     * The items are swapped between the source and target slots upon successful drop.
     *
     * @param slot        The {@link ImageButton} that represents the inventory slot where the item is being dragged from or dropped into.
     * @param targetIndex The target index of the slot in the inventory where the item will be dropped.
     * @param item        The {@link AbstractItem} representing the item in the source slot being dragged.
     */
    private void setupDragAndDrop (ImageButton slot, int targetIndex, AbstractItem item) {
        // Define the source
        dragAndDrop.addSource(new DragAndDrop.Source(slot) {
            @Override
            public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                DragAndDrop.Payload payload = new DragAndDrop.Payload();
                if(item != null) {
                    payload.setObject(targetIndex);
                    Image draggedImage = new Image(new Texture(item.getTexturePath()));
                    draggedImage.setSize(80, 80);
                    payload.setDragActor(draggedImage);
                }
                return payload;
            }
        });

        // Define the target
        dragAndDrop.addTarget(new DragAndDrop.Target(slot) {
            @Override
            public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                // Optional: Highlight the target slot to indicate a valid drop zone
                getActor().setColor(Color.LIGHT_GRAY);
                return true; // Return true to indicate the slot is a valid target
            }

            @Override
            public void reset(DragAndDrop.Source source, DragAndDrop.Payload payload) {
                // Reset the color of the slot when dragging is reset
                getActor().setColor(Color.WHITE);
            }

            @Override
            public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                int sourceIndex = (int) payload.getObject();
                inventory.swap(sourceIndex, targetIndex);
                createSlot(sourceIndex);
                createSlot(targetIndex);
                updateDisplay();
            }
        });
    }

    /**
     * Generates the inventory window and populates it with inventory slots.
     */
    public void generateInventory() {
        inventoryDisplay.clearChildren();
        Table table = new Table();
        // Iterate over the inventory and add slots
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                int index = row * numCols + col + hotBarCapacity;
                table.add(slots[index]).size(80, 80).pad(5); // Add the slot to the table
            }
            table.row(); // Move to the next row in the table
        }

        // Add sort button:
        TextButton sortButton = new TextButton("Sort", skin);
        sortButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                inventory.sortByCode();
                regenerateDisplay();
            }
        });
        table.row();
        table.add(sortButton);

        // Add the table to the window
        inventoryDisplay.add(table).expand().fill();
        inventoryDisplay.pack();
        // Set position in stage top-center
        inventoryDisplay.setPosition(
                (stage.getWidth() - inventoryDisplay.getWidth()) / 2 - 10,  // Center horizontally
                (stage.getHeight() - inventoryDisplay.getHeight()) / 2 // Center vertically
        );
    }

    /**
     * Creates the hot-bar UI, populates it with slots, and positions it on the stage.
     */
    void generateHotBar() {
        if (!hasHotBar) {return;} // Early exit if there should be no hotBar
        hotBarDisplay.clearChildren();
        hotBarDisplay.center().right();
        hotBarDisplay.setBackground(new TextureRegionDrawable(hotBarTexture));
        hotBarDisplay.setSize(160, 517);
        //creating slots
        for (int i = 0; i < hotBarCapacity; i++) {
            hotBarDisplay.add(slots[i]).size(80, 80).pad(5).padRight(45);
            hotBarDisplay.row();
        }
        float tableX = stage.getWidth() - hotBarDisplay.getWidth() - 20;
        float tableY = (stage.getHeight() - hotBarDisplay.getHeight()) / 2;
        hotBarDisplay.setPosition(tableX, tableY);
        stage.addActor(hotBarDisplay);
    }

    private void createSlot(int index) {
        slots[index] = new ImageButton(slotSkin);
        populateSlot(index);
    }

    private void populateSlot(int index) {
        AbstractItem item = inventory.getAt(index);
        if (item != null) {
            addSlotListeners(slots[index], item, index);
            Image itemImage = new Image(new Texture(item.getTexturePath()));
            slots[index].add(itemImage).center().size(70, 70);
            // Add a label for item quantity (subscript)
            int itemCount = item.getQuantity();
            Label itemCountLabel = new Label(String.valueOf(itemCount), new Label.LabelStyle(new BitmapFont(), Color.BLACK));
            itemCountLabel.setFontScale(1.2f); // Scale the font size of the label
            slots[index].add(itemCountLabel).bottom().right(); // Position the label at the bottom right
        }

        setupDragAndDrop(slots[index], index, item); // Setup drag and drop between hotBar and inventory
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
                enterSlot(item);
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                exitSlot(item);
            }
        });

        slot.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("Item {} was clicked", item.getName());
                useItem(item, index);
                createSlot(index);
                updateDisplay();
            }
        });
    }

    protected abstract void enterSlot(AbstractItem item);

    protected abstract void exitSlot(AbstractItem item);

    protected abstract void useItem(AbstractItem item, int index);

    /**
     * Adds an item to the inventory and triggers an event if successful.
     *
     * @param item The item to be added to the inventory.
     */
    private void addItem(AbstractItem item) {
        int index = this.inventory.add(item); // Keeping this line to avoid side effects
        entity.getEvents().trigger("itemPickedUp", index != -1);
        createSlot(index);
        updateDisplay();
    }

    private void updateDisplay() {
        generateHotBar();
        generateInventory();
    }

    // TODO: CHECK IF THE FOLLOWING ACTUALLY WORKS!!!
    /**
     * Regenerates the inventory display by toggling it off and on.
     * This method is used to refresh the inventory UI without duplicating code.
     */
    public void regenerateDisplay() {
        for (int i = 0; i < inventory.getCapacity(); i++) {
            createSlot(i);
        }
        updateDisplay();
//        generateInventory();
//        generateHotBar();
//        toggleDisplay(); // Hacky way to regenerate inventory without duplicating code
//        toggleDisplay();
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
        if (hotBarDisplay != null) { // hotBarDisplay is null if hasHotBar is false
            InventoryUtils.disposeGroupRecursively(hotBarDisplay);
            hotBarDisplay = null;
        }

        super.dispose();
    }

    /**
     * @return The z-index for this component.
     */
    @Override
    public float getZIndex() {
        return Z_INDEX;
    }

    /**
     * return the num of cols
     * @return num of cols
     */
    public int getNumCols() {
        return this.numCols;
    }

    /**
     * Consumes an item from the inventory and triggers an event if successful.
     * @param item The item to be consumed.
     * @param context The context in which the item is being used.
     * @param index The index of the item in the inventory.
     */
    protected void consumeItem(AbstractItem item, ItemUsageContext context, int index) {
        inventory.useItemAt(index, context);
        entity.getEvents().trigger("itemUsed", item);
    }
}
