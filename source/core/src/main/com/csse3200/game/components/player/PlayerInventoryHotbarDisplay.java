package com.csse3200.game.components.player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csse3200.game.inventory.Inventory;
import com.csse3200.game.inventory.items.AbstractItem;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;

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
    private final int capacity;
    private final Texture hotBarTexture = new Texture("Inventory/hotbar.png");//created by @PratulW5
    private final PlayerInventoryDisplay inventoryUI;

    /**
     * Constructs a PlayerInventoryHotbarDisplay with the specified hotbar capacity and inventory.
     *
     * @param capacity Number of slots in the hotbar
     * @param inventory      Player's inventory
     * @param InventoryUI Inventory display manager
     */
    public PlayerInventoryHotbarDisplay(int capacity, Inventory inventory,
                                        PlayerInventoryDisplay InventoryUI) {
        if (capacity < 1) {
            throw new IllegalArgumentException("Inventory Hotbar dimensions must be more than one!");
        }
        if(capacity >= inventory.getCapacity()) {
            throw new IllegalArgumentException(
                    "Inventory Hotbar capacity must be less than inventory capacity!");
        }
        this.inventory = inventory;
        this.capacity = capacity;
        this.inventoryUI = InventoryUI;
        this.hotBarSlots = new ImageButton[capacity];
        stage = ServiceLocator.getRenderService().getStage();
        createHotbar();

    }

    /**
     * Toggles the visibility of the hotbar.
     * If the hotbar is currently visible, it will be removed, and vice versa.
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
                inventoryUI.addSlotListeners(slot, item, i);
                Image itemImage = new Image(new Texture(item.getTexturePath()));
                slot.add(itemImage).center().size(75, 75);
            }
            table.add(slot).size(80, 80).pad(5).padRight(45);
            table.row();
            hotBarSlots[i] = slot;
        }
        float tableX = stage.getWidth() - table.getWidth() - 20;
        float tableY = (stage.getHeight() - table.getHeight()) / 2;
        table.setPosition(tableX, tableY);
        stage.addActor(table);
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