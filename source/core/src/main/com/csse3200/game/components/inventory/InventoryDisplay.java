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
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.inventory.*;
import com.csse3200.game.inventory.items.AbstractItem;
import com.csse3200.game.inventory.items.ItemUsageContext;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.badlogic.gdx.utils.Align.left;

/**
 * Abstract class for displaying an inventory. Subclasses can extend this to implement specific types of inventory
 * displays, such as player inventories, with additional features (e.g., hotBar, drag-and-drop).
 */
public abstract class InventoryDisplay extends UIComponent {
    protected static final Logger logger = LoggerFactory.getLogger(InventoryDisplay.class);
    private static final float Z_INDEX = 3f;

    protected final Inventory inventory;
    private Window mainInventoryDisplay;
    private Table hotBarDisplay;
    private DragAndDrop dragAndDrop;
    private boolean toggle = false;

    private final boolean hasHotBar;
    protected final int numCols;
    protected final int numRows;
    private final int hotBarCapacity;
    private final ImageButton[] slots; // Each slot corresponding to a position in the inventory

    // Skins (created by @PratulW5):
    private final Skin inventorySkin = new Skin(Gdx.files.internal("Inventory/inventory.json"));
    private final Skin slotSkin = new Skin(Gdx.files.internal("Inventory/skinforslot.json"));
    //Minecraft Skin for label
    private final Skin descrip= new Skin(Gdx.files.internal("Inventory/18font.json"));
    private final Texture hotBarTexture = new Texture("Inventory/hotbar.png");
    Texture discpbg = new Texture(Gdx.files.internal("Inventory/descp.png"));
    private final Skin sortButtonskin= new Skin(Gdx.files.internal("Inventory/sortButton.json"));
    ImageButton dustbin =new ImageButton(new Skin(Gdx.files.internal("Inventory/dustbin/dustbin.json")));//+1 for dustbin
    private static final int DUSTBIN_INDEX = 51;
    Label descriptionLabel = new Label("", descrip);
    /**
     * Constructs a PlayerInventoryDisplay with the specified capacity and number of columns.
     * The capacity must be evenly divisible by the number of columns.
     *
     * @param inventory The inventory from which to build the display
     * @param numCols  The number of columns in the inventory display.
     * @throws IllegalArgumentException if numCols is less than 1 or if capacity is not divisible by numCols.
     */
    protected InventoryDisplay(Inventory inventory, int numCols, int hotBarCapacity,
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
        mainInventoryDisplay.getTitleTable().padTop(150).padBottom(10);

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
                logger.debug("Item dragged from index: {}", sourceIndex);

                if (targetIndex == DUSTBIN_INDEX && item == null) {
                    logger.debug("Item dropped into the dustbin.");
                    inventory.deleteItemAt(sourceIndex);
                    createSlot(sourceIndex);
                    updateDisplay();

                }
                else{
                inventory.swap(sourceIndex, targetIndex);
                createSlot(sourceIndex);
                createSlot(targetIndex);
                updateDisplay();}


            }
        });
    }

    /**
     * Generates the inventory window and populates it with inventory slots.
     */
    private void generateInventory() {
        mainInventoryDisplay.clearChildren();

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
        TextButton sortButton = new TextButton("Sort", sortButtonskin);
        sortButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                inventory.sortByCode();
                regenerateDisplay();
            }
        });

        setupDragAndDrop(dustbin, DUSTBIN_INDEX, null);
        table.pack();
        // Create the scroll pane with the table
        ScrollPane scrollPane = new ScrollPane(table, skin);
        scrollPane.setSize(450, 450);

        // For description
        Table discp = new Table();
        discp.setBackground(new TextureRegionDrawable(discpbg)); // Set the background for the tooltip
        descriptionLabel.setWrap(true);
    descriptionLabel.setAlignment(Align.topLeft);

        // Add the description label to the table
        discp.add(descriptionLabel).pad(10).top().left().padLeft(50).size(350,470); // Align to top-left of the table
        discp.top().left(); // Ensure the table itself is aligned correctly

        // Add the table to the window
        mainInventoryDisplay.add(scrollPane).size(450, 450).align(Align.left).padTop(2).padLeft(20);
        mainInventoryDisplay.add(discp).size(400, 470).align(Align.top | Align.right).padTop(20).padRight(0); // Align to top-right
        mainInventoryDisplay.row();
        Table buttonTable = new Table();
        buttonTable.add(dustbin).size(80, 80).padRight(350); // Adjust padding as needed
        buttonTable.add(sortButton).size(100, 50).padLeft(350);
        mainInventoryDisplay.add(buttonTable).colspan(2).bottom().padTop(5);
        scrollPane.setScrollingDisabled(true, false);  // Enable both vertical and horizontal scrolling
        mainInventoryDisplay.pack();

        // Set position in stage top-center
        mainInventoryDisplay.setPosition(
                (stage.getWidth() - mainInventoryDisplay.getWidth()) / 2 - 10,  // Center horizontally
                (stage.getHeight() - mainInventoryDisplay.getHeight()) / 2 // Center vertically
        );
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
        slots[index] = new ImageButton(slotSkin);

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
