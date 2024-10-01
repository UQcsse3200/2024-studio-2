package com.csse3200.game.components.inventory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csse3200.game.inventory.Inventory;
import com.csse3200.game.inventory.items.AbstractItem;
import com.badlogic.gdx.scenes.scene2d.ui.*;

/**
 * PlayerInventoryDisplay extends InventoryDisplay and adds features like hotbar and drag-and-drop functionality.
 */
public class PlayerInventoryDisplay2 extends InventoryDisplay {
    private DragAndDrop dragAndDrop;
    private final Texture hotBarTexture = new Texture("Inventory/hotbar.png");
    private final int hotBarCapacity;
    private Table hotBarDisplay;

    public PlayerInventoryDisplay2(Inventory inventory, int numCols, int hotBarCapacity) {
        super(inventory, numCols, inventory.getCapacity() - hotBarCapacity);
        this.hotBarCapacity = hotBarCapacity;
    }

    @Override
    public void create() {
        super.create();
        dragAndDrop = new DragAndDrop();
        generateHotBar();
    }

    @Override
    protected void draw(SpriteBatch batch) {

    }

    /**
     * Toggles the display of the inventory, including the hotbar.
     */
    @Override
    public void toggleDisplay() {
        super.toggleDisplay();
        generateHotBar();
    }

    public void generateHotBar() {
        hotBarDisplay = new Table();
        hotBarDisplay.clear();
        hotBarDisplay.center().right();
        hotBarDisplay.setBackground(new TextureRegionDrawable(hotBarTexture));
        hotBarDisplay.setSize(160, 517);

        for (int i = 0; i < hotBarCapacity; i++) {
            hotBarDisplay.add(createSlot(i)).size(80, 80).pad(5).padRight(45);
            hotBarDisplay.row();
        }
        stage.addActor(hotBarDisplay);
    }

    @Override
    public void regenerateDisplay() {
        super.regenerateDisplay();
        generateHotBar();
    }

    @Override
    public void addSlotListeners(ImageButton slot, AbstractItem item, int index) {
        super.addSlotListeners(slot, item, index);
        setupDragAndDrop(slot, index, item);
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

    @Override
    public void dispose() {
        super.dispose();
        if (hotBarDisplay != null) {
            InventoryUtils.disposeGroupRecursively(hotBarDisplay);
            hotBarDisplay = null;
        }
    }
}
