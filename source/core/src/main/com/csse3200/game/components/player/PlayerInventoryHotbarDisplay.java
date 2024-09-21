package com.csse3200.game.components.player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.tasks.TimedUseItemTask;
import com.csse3200.game.inventory.Inventory;
import com.csse3200.game.inventory.items.AbstractItem;
import com.csse3200.game.inventory.items.ItemUsageContext;
import com.csse3200.game.inventory.items.TimedUseItem;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * UI component for displaying the player's quick-selection hotbar.
 * It manages a table with a fixed number of inventory slots that display items,
 * handle item usage, and update dynamically based on inventory changes.
 */
public class PlayerInventoryHotbarDisplay extends UIComponent {

    private final Skin skinSlots = new Skin(Gdx.files.internal("Inventory/skinforslot.json")); //created by @PratulW5
    private final Table table = new Table();
    private final Inventory inventory;
    private final ImageButton[] hotBarSlots;
    AITaskComponent aiComponent = new AITaskComponent();
    private static final Logger logger = LoggerFactory.getLogger(PlayerInventoryHotbarDisplay.class);
    private static final int timedUseItemPriority = 23;
    private final int capacity;
    private final DragAndDrop dnd;
    private final Texture hotBarTexture = new Texture("Inventory/hotbar.png");//created by @PratulW5

    /**
     * Constructs a PlayerInventoryHotbarDisplay with the specified hotbar capacity and inventory.
     *
     * @param inventory Player's inventory
     * @param capacity Hotbar's capacity
     */
    public PlayerInventoryHotbarDisplay(Inventory inventory,int capacity) {
        this.inventory= inventory;
        this.capacity=capacity;
        this.hotBarSlots=new ImageButton[capacity];
        this.dnd= DragAndDropService.getDragAndDrop();

    }

    /**
     * To initialise the stage
     */
    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener("addItem", this::addItem);
        createHotbar();
    }

    /**
     * Drawing is handled by the super class
     */
    @Override
    protected void draw(SpriteBatch batch) {
        // Drawing is handled by the stage
    }

    /**
     * Creates the hotbar UI, populates it with slots, and positions it on the stage.
     */
    void createHotbar() {
        table.clear();
        table.center().right();
        table.setBackground(new TextureRegionDrawable(hotBarTexture));
        table.setSize(160, 517);
        for (int i = 0; i < capacity; i++) {
            AbstractItem item = inventory.getAt(i);
            ImageButton slot = new ImageButton(skinSlots);
            if (item != null) {
                addSlotListeners(slot, item, i);
                Image itemImage = new Image(new Texture(item.getTexturePath()));
                slot.add(itemImage).center().size(75, 75);
            }
            table.add(slot).size(80, 80).pad(5).padRight(45);
            table.row();
            hotBarSlots[i] = slot;
            // Add drag source and target for this hotbar slot
            dnd.addSource(new InventorySource(slot, i));
            dnd.addTarget(new InventoryTarget(slot, i));
        }
        float tableX = stage.getWidth() - table.getWidth() - 20;
        float tableY = (stage.getHeight() - table.getHeight()) / 2;
        table.setPosition(tableX, tableY);
        stage.addActor(table);
    }


    // Add InventorySource class as before
    public class InventorySource extends DragAndDrop.Source {
        private final int index;

        public InventorySource(Actor actor, int index) {
            super(actor);
            this.index = index;
        }

        @Override
        public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
            DragAndDrop.Payload payload = new DragAndDrop.Payload();
            AbstractItem item = inventory.getAt(index);
            if (item != null) {
                payload.setObject(item);
                Image dragImage = new Image(new Texture(item.getTexturePath()));
                payload.setDragActor(dragImage);
            }
            return payload;
        }

        @Override
        public void dragStop(InputEvent event, float x, float y, int pointer, DragAndDrop.Payload payload, DragAndDrop.Target target) {
            if (target == null) {
                regenerateHotbar();
            }
        }
    }

    @Override
    public void dispose() {
        disposeSlots();
        disposeTable();
        super.dispose();
    }

    /**
     * Disposes of the table by clearing its contents and removing it from the stage.
     */
    void disposeTable() {
        table.clear();
        table.remove();
    }
    public void addSlotListeners(ImageButton slot, AbstractItem item, int index) {
        // Time interval to determine double-click
        final int DOUBLE_CLICK_TIME = 300; // 300 milliseconds (adjust as needed)
        final int[] clickCount = {0};
        final long[] lastClickTime = {0};

        slot.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                //double calls when mouse held, to be fixed
                String[][] itemText = {{item.getDescription() + ". Quantity: "
                        + item.getQuantity() + "/" + item.getLimit()}};
                ServiceLocator.getDialogueBoxService().updateText(itemText);
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                ServiceLocator.getDialogueBoxService().hideCurrentOverlay();
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                long currentTime = System.currentTimeMillis();
                clickCount[0]++;

                if (clickCount[0] == 1) {
                    lastClickTime[0] = currentTime;
                } else if (clickCount[0] == 2) {
                    if (currentTime - lastClickTime[0] <= DOUBLE_CLICK_TIME) {
                        // Double-click detected
                        clickCount[0] = 0; // Reset click count
                        handleItemUse(); // Handle item usage on double-click
                    } else {
                        handleSingleClick();
                        clickCount[0] = 1;
                        lastClickTime[0] = currentTime;
                    }
                }

                return true; // Return true to indicate that the event was handled
            }


            private void handleItemUse() {
                logger.debug("Item {} was used", item.getName()); // Log the item usage
                ItemUsageContext context = new ItemUsageContext(entity); // Create a context for item usage
                if (item instanceof TimedUseItem) {
                    // If the item is a TimedUseItem, add a task to the AI component
                    aiComponent.addTask(
                            new TimedUseItemTask(entity, timedUseItemPriority, (TimedUseItem) item, context));
                }
                inventory.useItemAt(index, context); // Use the item in the inventory
                entity.getEvents().trigger("itemUsed", item); // Trigger an event indicating the item was used
                regenerateHotbar(); // Update the inventory display
            }

            private void handleSingleClick() {
                logger.debug("Item {} was selected", item.getName());
            }


        });
    }
    private class InventoryTarget extends DragAndDrop.Target {
        private final ImageButton slot;
        private final int index;

        public InventoryTarget(ImageButton slot, int index) {
            super(slot);
            this.slot = slot;
            this.index = index;
        }

        @Override
        public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
            AbstractItem draggedItem = (AbstractItem) payload.getObject();
            // Check if the slot can accept the dragged item
            if (inventory.getAt(index) == null || !inventory.getAt(index).equals(draggedItem)) {
                slot.setColor(Color.LIGHT_GRAY); // Indicate valid drop target
                return true; // Allow the drop
            } else {
                slot.setColor(Color.RED); // Indicate invalid drop target
                return false; // Reject the drop
            }
        }
        @Override
        public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
            AbstractItem draggedItem = (AbstractItem) payload.getObject();
            if (draggedItem != null && index < inventory.getCapacity()) {
                // Get the item currently in the target slot
                AbstractItem itemInSlot = inventory.getAt(index);
                int sourceIndex = ((PlayerInventoryDisplay.InventorySource) source).index;
                inventory.removeAt(sourceIndex);
                // Check if the slot is occupied
                if (itemInSlot != null) {
                    // Get the source index from the source object
                    // Remove the item in the target slot and the dragged item from their respective slots
                    inventory.removeAt(index);
                    // Add the dragged item to the target slot and the item that was in the target slot to the source slot
                    inventory.addAt(index, draggedItem);
                    inventory.addAt(sourceIndex, itemInSlot);
                    regenerateHotbar();
                } else {
                    // If the target slot is empty, simply add the dragged item to the slot
                    inventory.addAt(index, draggedItem);
                }

                regenerateHotbar(); // Refresh the inventory display
            }
        }

        @Override
        public void reset(DragAndDrop.Source source, DragAndDrop.Payload payload) {
            // Reset the slot color when dragging leaves the target
            slot.setColor(Color.WHITE);
        }
    }

    private void regenerateHotbar() {
        if (stage.getActors().contains(table, true)) {
            disposeTable();
            createHotbar();
        }
    }
    private void addItem(AbstractItem item) {
        if (this.inventory.add(item)) {
            entity.getEvents().trigger("itemPickedUp", true);
        } else {
            entity.getEvents().trigger("itemPickedUp", false);
        }
        createHotbar();

    }

    /**
     * loads inventory from previous save
     */
    public void loadInventoryFromSave() {
        inventory.loadInventoryFromSave();
    }

    /**
     * Disposes of each slot in the hotbar by clearing and removing them.
     */
    private void disposeSlots() {
        for (ImageButton slot : hotBarSlots) {
            slot.clear();
            slot.remove();
        }
    }
}