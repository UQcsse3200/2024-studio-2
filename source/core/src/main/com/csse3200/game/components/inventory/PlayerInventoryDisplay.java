package com.csse3200.game.components.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.inventory.*;
import com.csse3200.game.inventory.items.AbstractItem;
import com.csse3200.game.inventory.items.ItemUsageContext;
import com.csse3200.game.inventory.items.TimedUseItem;
import com.csse3200.game.inventory.items.potions.AttackPotion;
import com.csse3200.game.inventory.items.potions.DefensePotion;
import com.csse3200.game.inventory.items.potions.SpeedPotion;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * PlayerInventoryDisplay is a UI component that displays the player's inventory in a grid format.
 * It creates a window with a table of inventory slots that can display items, handle item usage,
 * and update dynamically when the inventory changes.
 */
public class PlayerInventoryDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(PlayerInventoryDisplay.class);
    private static final int timedUseItemPriority = 23;
    private static final float Z_INDEX = 3f;

    protected final Inventory inventory;
    private Window inventoryDisplay;
    private  Table hotBarDisplay;
    private DragAndDrop dragAndDrop;
    AITaskComponent aiComponent = new AITaskComponent();

    protected final int numCols;
    protected final int numRows;
    private final int hotBarCapacity;
    private boolean toggle = false; // Whether inventory is toggled on;
    private ArrayList<TimedUseItem> potions = new ArrayList<TimedUseItem>();

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
    public PlayerInventoryDisplay(Inventory inventory, int numCols, int hotBarCapacity) {
        if (numCols < 1) {
            String msg = String.format("numCols (%d) must be positive", numCols);
            throw new IllegalArgumentException(msg);
        }

        if (hotBarCapacity < 1) {
            String msg = String.format("hotBarCapacity (%d) must be positive", hotBarCapacity);
            throw new IllegalArgumentException(msg);
        }
        this.hotBarCapacity = hotBarCapacity;

        int capacity = inventory.getCapacity() - hotBarCapacity;
        if (capacity % numCols != 0) {
            String msg = String.format("numCols (%d) must divide capacity (%d)", numCols, capacity);
            throw new IllegalArgumentException(msg);
        }
        this.inventory = inventory;
        this.numCols = numCols;
        this.numRows = capacity / numCols;
    }

    /**
     * updates the potions effects if in or out of combat
     */
    public void updatePotions(ItemUsageContext context) {
        if (this.potions != null) {
                for (int i = 0; i < potions.size(); i++) {
                    if (potions.get(i) instanceof DefensePotion) {
                        potions.get(i).update(context);
                        potions.remove(i);
                    }
                    if (potions.get(i) instanceof AttackPotion) {
                        potions.get(i).update(context);
                        potions.remove(i);
                    }
                }

            for (int i = 0; i < potions.size(); i++) {
                if (potions.get(i) instanceof SpeedPotion) {
                    potions.get(i).update(context);
                    potions.remove(i);
                }
            }
        }
    }



    /**
     * Initializes the component by setting up event listeners for toggling the inventory display
     * and adding items.
     */
    @Override
    public void create() {
        super.create();
        dragAndDrop = new DragAndDrop();
        generateHotBar();
        entity.getEvents().addListener("toggleInventory", this::toggleDisplay);
        entity.getEvents().addListener("addItem", this::addItem);
    }

    /**
     * Checks if the player is in combat or not to restrict certain actions i.e. using defense and attack potion when
     * not in combat or ensuring hotbar does not appear during combat
     * @param item the item in inventory
     * @param context context of the item usage
     * @param index index of the item in inventory
     */
    private void tryUseItem(AbstractItem item, ItemUsageContext context, int index) {
        if (item instanceof DefensePotion || item instanceof AttackPotion) {
                logger.warn("Cannot use defense or attack potions outside of combat.");
                return;
        }
        // Otherwise, allow item use
        inventory.useItemAt(index, context);
        entity.getEvents().trigger("itemUsed", item);
    }

    /**
     * Toggles the inventory display on or off based on its current state.
     */
    public void toggleDisplay() {
        toggle = !stage.getActors().contains(inventoryDisplay, true);
        if (!toggle) { // Toggle off
            logger.debug("Inventory toggled off.");
            stage.getActors().removeValue(inventoryDisplay, true); // close inventory
            InventoryUtils.disposeGroupRecursively(inventoryDisplay);
        } else { // Toggle on
            logger.debug("Inventory toggled on.");
            generateInventory();
        }
        // Regenerate the hotBar in either case
        stage.getActors().removeValue(hotBarDisplay, true);
        InventoryUtils.disposeGroupRecursively(hotBarDisplay);
        generateHotBar();
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
    private void setupDragAndDrop (ImageButton slot,int targetIndex, AbstractItem item) {
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
                    inventory.swap(sourceIndex,targetIndex);
                    regenerateDisplay();
            }
        });
    }

    /**
     * Determines if the toggle is active
     * @return returns the toggle
     */
    public boolean getToggle() {
        return toggle;
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
    public void generateInventory() {
        // Create the inventory window (pop-up)
        inventoryDisplay = new Window("Inventory", inventorySkin);
        Label.LabelStyle titleStyle = new Label.LabelStyle(inventoryDisplay.getTitleLabel().getStyle());
        titleStyle.fontColor = Color.BLACK;
        inventoryDisplay.getTitleLabel().setAlignment(Align.center);
        inventoryDisplay.getTitleTable().padTop(150).padBottom(10);

        // Create the table for inventory slots
        inventoryDisplay.getTitleLabel().setStyle(titleStyle);

        Table table = new Table();
        // Iterate over the inventory and add slots
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                int index = row * numCols + col + hotBarCapacity;
                table.add(createSlot(index)).size(90, 90).pad(5); // Add the slot to the table
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
                (stage.getWidth() - inventoryDisplay.getWidth()) / 2,  // Center horizontally
                (stage.getHeight() - inventoryDisplay.getHeight()) / 2 // Center vertically
        );
        stage.addActor(inventoryDisplay);
    }

    /**
     * Creates the hot-bar UI, populates it with slots, and positions it on the stage.
     */
    void generateHotBar() {
        hotBarDisplay = new Table();
        hotBarDisplay.clear();
        hotBarDisplay.center().right();
        hotBarDisplay.setBackground(new TextureRegionDrawable(hotBarTexture));
        hotBarDisplay.setSize(160, 517);
        //creating slots
        for (int i = 0; i < hotBarCapacity; i++) {
            hotBarDisplay.add(createSlot(i)).size(80, 80).pad(5).padRight(45);
            hotBarDisplay.row();
        }
        float tableX = stage.getWidth() - hotBarDisplay.getWidth() - 20;
        float tableY = (stage.getHeight() - hotBarDisplay.getHeight()) / 2;
        hotBarDisplay.setPosition(tableX, tableY);
        stage.addActor(hotBarDisplay);
    }

    private ImageButton createSlot(int index) {
        ImageButton slot = new ImageButton(slotSkin);
        AbstractItem item = inventory.getAt(index);
        if (item != null) {
            addSlotListeners(slot, item, index);
            Image itemImage = new Image(new Texture(item.getTexturePath()));
            slot.add(itemImage).center().size(70, 70);
            // Add a label for item quantity (subscript)
            int itemCount = item.getQuantity();
            Label itemCountLabel = new Label(String.valueOf(itemCount), new Label.LabelStyle(new BitmapFont(), Color.BLACK));
            itemCountLabel.setFontScale(1.2f); // Scale the font size of the label
            slot.add(itemCountLabel).bottom().right(); // Position the label at the bottom right
        }

        setupDragAndDrop(slot, index, item); // Setup drag and drop between hotBar and inventory
        return slot;
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
                if (item instanceof DefensePotion) {
                    String[][] itemText = {{((DefensePotion) item).getWarning()}};
                    ServiceLocator.getDialogueBoxService().updateText(itemText);
                } else if (item instanceof AttackPotion) {
                    String[][] itemText = {{((AttackPotion) item).getWarning()}};
                    ServiceLocator.getDialogueBoxService().updateText(itemText);
                } else {
                    String[][] itemText = {{item.getDescription() + ". Quantity: "
                            + item.getQuantity() + "/" + item.getLimit()}};
                    ServiceLocator.getDialogueBoxService().updateText(itemText);
                }
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                ServiceLocator.getDialogueBoxService().hideCurrentOverlay();
            }
        });

        slot.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("Item {} was used", item.getName());
                ItemUsageContext context = new ItemUsageContext(entity);
                tryUseItem(item, context, index);
                if (item instanceof TimedUseItem) {
                    potions.add((TimedUseItem) item);
                }
                inventory.useItemAt(index, context);
                entity.getEvents().trigger("itemUsed", item);
                regenerateDisplay();
            }
        });
    }

    /**
     * Adds an item to the inventory and triggers an event if successful.
     *
     * @param item The item to be added to the inventory.
     */
    private void addItem(AbstractItem item) {
        boolean wasAdded = this.inventory.add(item); // Keeping this line to avoid side effects
        entity.getEvents().trigger("itemPickedUp", wasAdded);
        regenerateDisplay();
    }

    /**
     * Regenerates the inventory display by toggling it off and on.
     * This method is used to refresh the inventory UI without duplicating code.
     */
    public void regenerateDisplay() {
        toggleDisplay(); // Hacky way to regenerate inventory without duplicating code
        toggleDisplay();
        ItemUsageContext context = new ItemUsageContext(entity);
        updatePotions(context);
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
        if (hotBarDisplay != null) {
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
     * Loads the inventory attached to the player from a save.
     */
    public void loadInventoryFromSave() {
        inventory.loadInventoryFromSave();
    }

    /** Returns inventory - for quests. */
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * return the num of cols
     * @return num of cols
     */
    public int getNumCols() {
        return this.numCols;
    }
}
