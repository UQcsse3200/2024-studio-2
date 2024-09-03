package com.csse3200.game.components.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csse3200.game.inventory.Inventory;
import com.csse3200.game.inventory.InventoryHotBar;
import com.csse3200.game.inventory.items.AbstractItem;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PlayerInventoryHotbarDisplay is a UI component that displays players quick selection panel for
 * better user interaction during gameplay
 * It creates a table of first n inventory slots that can display items, handle item usage,
 * and update dynamically when the inventory changes.
 */

public class PlayerInventoryHotbarDisplay extends UIComponent {

    private static final Logger logger = LoggerFactory.getLogger(PlayerInventoryHotbarDisplay.class);
    private Skin skinSlots=new Skin(Gdx.files.internal("Inventory/skinforslot.json"));
    private final Table table;
    private final Inventory inventory;
    private final ImageButton[] hotBarSlots;
    private final int numberOfSlots;
    private Texture hotBarTexture;

    public PlayerInventoryHotbarDisplay(int hotBarCapacity, int capacity) {
        if (hotBarCapacity < 1) {
            throw new IllegalArgumentException("Inventory Hotbar dimensions must be more than one!");
        }
        this.inventory = new Inventory(capacity);
        this.hotBarTexture=new Texture("Inventory/hotbar.png");

        this.hotBarSlots = new ImageButton[hotBarCapacity];
        this.numberOfSlots = hotBarCapacity;
        this.table = new Table();
        stage = ServiceLocator.getRenderService().getStage();
        initializeSlots();

    }
    @Override
    public void create() {
        super.create();

    }

    @Override
    protected void draw(SpriteBatch batch) {
        // handled by stage
    }

    private void initializeSlots() {
        table.clear(); // Clear previous content
        table.center().right();



        // Use the full texture for the background
        Drawable backgroundDrawable = new TextureRegionDrawable(hotBarTexture); // Use the full texture
        // Set the full texture as the background
        table.setBackground(backgroundDrawable);


// Set the table size based on the full texture size
        table.setSize(150, 517);
        for (int i = 0; i < numberOfSlots; i++) {
            AbstractItem item = inventory.getAt(i);

            final ImageButton slot = new ImageButton(skinSlots);

            if (item != null) {
                addSlotListeners(slot, item, i);
                Image itemImage = new Image(new Texture(item.getTexturePath()));
                slot.add(itemImage).center().size(60, 60); // Scale down slot content as well
            }

            table.add(slot).size(82,78).pad(5).padRight(35); // Scale down slot size
            table.row(); // Move to the next row after adding each slot
            hotBarSlots[i] = slot;
        }

        // Calculate the position to align the table to the right center
        float tableX = stage.getWidth() - table.getWidth() - 20; // Align to the right with padding
        float tableY = (stage.getHeight() - table.getHeight()) / 2; // Center vertically

        table.setPosition(tableX, tableY); // Set the calculated position

        stage.addActor(table);
    }



    private void addSlotListeners(ImageButton slot, AbstractItem item, int index) {
        slot.addListener(new InputListener() {
            @Override
            public boolean mouseMoved(InputEvent event, float x, float y) {
                // Handle mouse moved event
                return true;
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                // Handle exit event
            }
        });
    }

    @Override
    public void dispose() {
        disposeSlots();
        disposeTable();

        super.dispose();
    }

    private void disposeTable() {
        if (table != null) {
            table.clear();
            table.remove();
        }
    }

    private void disposeSlots() {
        for (int i = 0; i < numberOfSlots; i++) {
            if (hotBarSlots[i] != null) {
                hotBarSlots[i].clear();
                hotBarSlots[i].remove();
            }
        }
    }
}
