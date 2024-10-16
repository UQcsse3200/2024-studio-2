package com.csse3200.game.components.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.GdxGame;
import com.csse3200.game.inventory.*;
import com.csse3200.game.inventory.items.AbstractItem;
import com.csse3200.game.inventory.items.ItemUsageContext;
import com.csse3200.game.screens.MainGameScreen;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract class for displaying an inventory. Subclasses can extend this to implement specific types of inventory
 * displays, such as player inventories, with additional features (e.g., hotBar, drag-and-drop).
 */
public abstract class InventoryDisplay extends UIComponent {
    protected static final Logger logger = LoggerFactory.getLogger(InventoryDisplay.class);
    private static final float Z_INDEX = 5f;

    protected final Inventory inventory;
    private Window mainInventoryDisplay;
    private Table hotBarDisplay;
    private DragAndDrop dragAndDrop;
    private boolean toggle = false;
    private final GdxGame game;
    private final boolean hasHotBar;
    protected final int numCols;
    protected final int numRows;
    private final int hotBarCapacity;
    private final ImageButton[] slots; // Each slot corresponding to a position in the inventory

    // Skins (created by @PratulW5):
    Skin inventorySkin= new Skin(Gdx.files.internal("Inventory/InventorySkin/InventorySkin.json"));
    //Textures (created by @PratulW5)
    private final Texture hotBarTexture = new Texture("Inventory/hotbar.png");
    private final Texture descriptionbg= new Texture("Inventory/descp.png");
    Label descriptionLabel = new Label("", inventorySkin);
    private final Texture alert = new Texture(Gdx.files.internal("Inventory/skinforalert.png"));
    Drawable backgroundDrawable = new TextureRegionDrawable(new TextureRegion(alert));
    /**
     * Constructs a PlayerInventoryDisplay with the specified capacity and number of columns.
     * The capacity must be evenly divisible by the number of columns.
     *
     * @param inventory The inventory from which to build the display
     * @param numCols  The number of columns in the inventory display.
     * @throws IllegalArgumentException if numCols is less than 1 or if capacity is not divisible by numCols.
     */
    protected InventoryDisplay(Inventory inventory, int numCols, int hotBarCapacity, boolean displayHotBar, GdxGame game) {
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
        this.game = game;

        this.slots = new ImageButton[inventory.getCapacity()];
    }
    /**
     * Checks if the current screen is an instance of MainGameScreen.
     *
     * @return true if the current screen is MainGameScreen, false otherwise.
     */
    protected boolean isMainGameScreen() {
        Screen currentScreen = game.getScreen();
        return currentScreen instanceof MainGameScreen;
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
        if (hasHotBar) {generateHotBar();}
        generateInventory();
        entity.getEvents().addListener(toggleMsg(), this::toggleDisplay);
        entity.getEvents().addListener("addItem", this::addItem);
    }

    private void initDisplays() {
        for (int i = 0; i < inventory.getCapacity(); i++) {
            createSlot(i);
        }
        // Initialise the inventory window (pop-up)
        mainInventoryDisplay = new Window("Inventory", inventorySkin);
        Label.LabelStyle titleStyle = new Label.LabelStyle(mainInventoryDisplay.getTitleLabel().getStyle());
        titleStyle.fontColor = Color.BLACK;
        mainInventoryDisplay.getTitleLabel().setAlignment(Align.center);
        mainInventoryDisplay.getTitleTable().padTop(140).padBottom(20);
        // Create the table for inventory slots
        mainInventoryDisplay.getTitleLabel().setStyle(titleStyle);
        stage.addActor(mainInventoryDisplay);
        mainInventoryDisplay.setVisible(false); // Not displayed to start with
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
        String msg = "Inventory toggled " + (toggle ? "on." : "off.");
        logger.debug(msg);
        mainInventoryDisplay.setVisible(toggle);
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
        if (item != null) {
            dragAndDrop.addSource(new DragAndDrop.Source(slot) {
                @Override
                public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                    DragAndDrop.Payload payload = new DragAndDrop.Payload();
                    payload.setObject(targetIndex);
                    Image draggedImage = new Image(new Texture(item.getTexturePath()));
                    draggedImage.setSize(80, 80);
                    payload.setDragActor(draggedImage);
                    return payload;
                }
                @Override
                public void dragStop(InputEvent event, float x, float y, int pointer, DragAndDrop.Payload payload, DragAndDrop.Target target) {
                    logger.info("Drag stopped at stage coordinates: ({}, {})", x, y);

                    // Check if the item was dragged outside the inventory and if we are on the MainGameScreen.
                    if (target == null && isMainGameScreen()) {
                        logger.info("No target detected and we are on the MainGameScreen. Proceeding to drop item.");

                        // Convert stage coordinates to world coordinates.
                        float worldX = event.getStageX();
                        float worldY = event.getStageY();
                        Vector2 worldPosition = stage.screenToStageCoordinates(new Vector2(worldX, worldY));
                        logger.info("Converted stage coordinates to world position: ({}, {})", worldPosition.x, worldPosition.y);

                        int sourceIndex = (int) payload.getObject();
                        logger.info("Source index of the dragged item: {}", sourceIndex);

                        // Trigger the event to remove the item from the inventory and drop it onto the map.
                        entity.getEvents().trigger("removeItemFromInventory", sourceIndex, worldPosition);
                        logger.info("Triggered 'removeItemFromInventory' event for item at index {} with world position ({}, {}).", sourceIndex, worldPosition.x, worldPosition.y);

                        // Refresh the slot and update the inventory display.
                        createSlot(sourceIndex);
                        updateDisplay();
                        logger.info("Slot at index {} refreshed and inventory display updated.", sourceIndex);
                    } else {
                        logger.info("Drag stopped with a valid target or not on the MainGameScreen. No action taken.");
                    }
                }
            });
        }

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
    private void generateInventory() {
        mainInventoryDisplay.clearChildren();
        if (inventory.isFull()) {
            showInventoryFullAlert();
        }
        // Iterate over the inventory and add slots
        Table table = new Table();
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                int index = row * numCols + col + hotBarCapacity;
                table.add(slots[index]).size(80, 80).pad(5); // Add the slot to the table
            }
            table.row(); // Move to the next row in the table
        }
        // Add sort button
        TextButton sortButton = new TextButton("Sort", inventorySkin);
        sortButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                inventory.sortByCode();
                regenerateDisplay();
            }
        });
        table.pack();
        ScrollPane scrollPane = new ScrollPane(table, skin);// Create the scroll pane with the table
        Table description= new Table();// For description
        description.setBackground(new TextureRegionDrawable(descriptionbg));
        descriptionLabel.setWrap(true);
        descriptionLabel.setText("");
        descriptionLabel.setAlignment(Align.topLeft);
        description.setSize(400, 461);
        description.add(descriptionLabel).pad(10).top().left().width(400).padLeft(60);
        description.top().left();
        mainInventoryDisplay.add(scrollPane).width(450).height(450).align(Align.left).padTop(60).padLeft(45).padRight(-22);
        mainInventoryDisplay.add(description).height(461).align(Align.top | Align.right).padTop(68).padRight(10).padLeft(-23); // Align to top-right
        mainInventoryDisplay.row();// Create a button table and add the buttons
        Table buttonTable = new Table();
        buttonTable.add(sortButton).size(100, 50).padLeft(350);
        mainInventoryDisplay.add(buttonTable).colspan(2).bottom().padTop(5);
        scrollPane.setScrollingDisabled(true, false);
        mainInventoryDisplay.pack();
        mainInventoryDisplay.setPosition(
                (stage.getWidth() - mainInventoryDisplay.getWidth()) / 2 - 10,  // Center horizontally
                (stage.getHeight() - mainInventoryDisplay.getHeight()) / 2 // Center vertically
        );
    }
    /**
     * Creates a dialog box to alert user the inventory is full
     */
    private void showInventoryFullAlert() {
        Dialog dialog = new Dialog(" ", skin) {
            public void result(Object obj) {

            }
        };

        dialog.text("Inventory is full!").padTop(50).padLeft(50);
        dialog.button("OK", true);


        dialog.setBackground(backgroundDrawable);
        dialog.setSize(600,150);
        dialog.setPosition(
                (stage.getWidth() - dialog.getWidth()) / 2 - 10,  // Center horizontally
                (stage.getHeight() - dialog.getHeight()) / 2 // Center vertically
        );  // Add a button to close the dialog

        stage.addActor(dialog);  // Show the dialog on the stage
    }

    /**
     * Creates the hot-bar UI, populates it with slots, and positions it on the stage.
     */
    private void generateHotBar() {
        if (!hasHotBar) {return;} // Early exit if there should be no hotBar

        // Remove old hotBar from stage if it exists:
        if (hotBarDisplay != null) {hotBarDisplay.remove();}

        // Create new hotBar:
        hotBarDisplay = new Table();
        hotBarDisplay.center().right();
        hotBarDisplay.setBackground(new TextureRegionDrawable(hotBarTexture));
        hotBarDisplay.setSize(160, 517);

        // Creating Slots:
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
        slots[index] = new ImageButton(inventorySkin);
        AbstractItem item = inventory.getAt(index);
        if (item != null) {
            // Add event listeners and the image to the slot
            addSlotListeners(slots[index], item, index);
            Image itemImage = new Image(new Texture(item.getTexturePath()));
            slots[index].add(itemImage).center().size(70, 70);

            // Add a label for item quantity (subscript)
            Label quantity = new Label(String.valueOf(item.getQuantity()),
                    new Label.LabelStyle(new BitmapFont(), Color.BLACK));
            quantity.setFontScale(1.2f);
            slots[index].add(quantity).bottom().right();
        }

        setupDragAndDrop(slots[index], index, item); // Setup drag and drop for hotBar and inventory
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
                descriptionLabel.setText(item.getDescription());
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                exitSlot(item);
                descriptionLabel.setText("");
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

    /**
     * Updates the display of the inventory (does not regenerate any of the images for
     * any slot, simply creates a new table to hold them).
     */
    public void updateDisplay() {
        generateHotBar();
        generateInventory();
    }

    /**
     * Regenerates the entire inventory and hotBar display (recreates every image)
     * This should only be used if the inventory has been changed (ie after
     * sorting or loading). Otherwise, use updateDisplay.
     */
    public void regenerateDisplay() {
        for (int i = 0; i < inventory.getCapacity(); i++) {createSlot(i);}
        updateDisplay();
    }

    /**
     * Disposes of the resources used by the component, including the window, table, and slots.
     */
    @Override
    public void dispose() {
        if (mainInventoryDisplay != null) {
            InventoryUtils.disposeGroupRecursively(mainInventoryDisplay);
            mainInventoryDisplay =null;
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