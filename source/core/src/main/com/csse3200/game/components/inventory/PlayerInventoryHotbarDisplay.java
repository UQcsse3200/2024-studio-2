package com.csse3200.game.components.inventory;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.csse3200.game.inventory.Inventory;
import com.csse3200.game.inventory.items.AbstractItem;
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
        create();
    }

    /**
     * To initialise the stage
     */
    public void create() {
        super.create();
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
                inventoryUI.addSlotListeners(slot, item, i);
                Image itemImage = new Image(new Texture(item.getTexturePath()));
                slot.add(itemImage).center().size(75, 75);
            }
            table.add(slot).size(80, 80).pad(5).padRight(45);
            table.row();
        }
        float tableX = stage.getWidth() - table.getWidth() - 20;
        float tableY = (stage.getHeight() - table.getHeight()) / 2;
        table.setPosition(tableX, tableY);
        stage.addActor(table);
    }

    @Override
    public void dispose() {
        disposeGroupRecursively(table);

        super.dispose();
    }


    /**
     * Disposes of the table by clearing its contents and removing it from the stage.
     */
    void disposeTable() {
        disposeGroupRecursively(table);
    }

    private void disposeGroupRecursively(Group group) {
        for (Actor child : group.getChildren()) {
            // Dispose if child implements Disposable
            if (child instanceof Disposable) {
                ((Disposable) child).dispose();
            }
            // If the child is a Group (including Table), dispose of its children as well
            if (child instanceof Group) {
                disposeGroupRecursively((Group) child);
            }
        }
        group.clearChildren(); // Remove all children from the group
        group.remove();
    }
}