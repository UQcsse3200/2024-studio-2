package com.csse3200.game.components.lootboxview;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.inventory.items.AbstractItem;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.List;

/**
 * LootBoxOverlayComponent is responsible for displaying a UI overlay that shows items
 * received from a loot box on the player's screen. It manages the creation, display,
 * and disposal of the overlay window containing the loot items.
 */
public class LootBoxOverlayComponent extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(LootBoxOverlayComponent.class);
    private Window overlayWindow;

    /**
     * Initializes the loot box overlay component. Creates the overlay window and sets up the
     * event listener to display the loot when triggered. The overlay is initially hidden.
     */
    @Override
    public void create() {
        super.create();
        overlayWindow = new Window("Loot Box Rewards", skin);
        overlayWindow.setVisible(false); // Start hidden
        overlayWindow.setMovable(false);
        overlayWindow.setKeepWithinStage(true);

        // Add the overlay to the stage
        stage.addActor(overlayWindow);

        // Register the event listener to show the loot
        entity.getEvents().addListener("showLoot", this::showLoot);
        logger.info("LootBoxOverlayComponent created and listener registered.");
    }

    /**
     * Displays the loot items in the overlay window when the "showLoot" event is triggered.
     * Each item is represented by its image and name. A close button is also added to the overlay
     * for hiding the window.
     *
     * @param items A list of AbstractItem objects representing the loot items to be displayed.
     *              If the list is null or empty, the overlay will not be shown.
     */
    private void showLoot(List<AbstractItem> items) {
        if (items == null || items.isEmpty()) {
            logger.warn("Received showLoot event with no items.");
            return;
        }
        logger.info("Showing loot with items: {}", items);

        // Clear the previous contents of the overlay
        overlayWindow.clearChildren();

        // Create a new table for displaying the loot items
        Table contentTable = new Table(skin);

        // Set up a grid-like layout to prevent cramming, adjust columns based on items
        int numColumns = 4;  // Adjust the number of columns to suit your layout
        int itemCount = 0;

        for (AbstractItem item : items) {
            Texture itemTexture = new Texture(item.getTexturePath());
            Image itemImage = new Image(itemTexture);
            Label itemLabel = new Label(item.getName(), skin);

            // Set a fixed size for each image, ensuring they are uniform and not cramped
            itemImage.setSize(64, 64);  // Adjust the size as needed
            contentTable.add(itemImage).size(64, 64).pad(10);  // Add padding between items
            contentTable.add(itemLabel).pad(10).align(Align.center);

            itemCount++;

            // Add a new row after every 'numColumns' items
            if (itemCount % numColumns == 0) {
                contentTable.row();
            }
        }

        // Add the content table to the overlay window and make it expand/fill
        overlayWindow.add(contentTable).expand().fill().row();

        // Create and add a close button
        TextButton closeButton = new TextButton("Close", skin);

        closeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("Close button clicked.");
                hideOverlay();
            }
        });

        // Add the close button to the window
        overlayWindow.add(closeButton).padTop(10);

        // Automatically resize the window based on the content
        overlayWindow.pack();  // This will resize the window to fit its contents

        // Ensure the window is centered on the screen
        overlayWindow.setPosition(
                (stage.getWidth() - overlayWindow.getWidth()) / 2,
                (stage.getHeight() - overlayWindow.getHeight()) / 2
        );

        // Set Z-Index to ensure it is on top
        overlayWindow.setZIndex(stage.getActors().size + 1);
        overlayWindow.toFront();  // Bring the overlay to the front

        // Make the overlay visible
        overlayWindow.setVisible(true);
        logger.debug("LootBoxOverlay is set to visible with items displayed.");
    }

    /**
     * Hides the loot box overlay and clears its contents. This method is typically called
     * when the close button on the overlay is clicked.
     */
    private void hideOverlay() {
        overlayWindow.setVisible(false);
        overlayWindow.clearChildren(); // Optionally clear contents if needed
        logger.info("LootBoxOverlay hidden by close button.");
    }

    /**
     * Disposes of the loot box overlay component by hiding the overlay and removing it from the stage.
     */
    @Override
    public void dispose() {
        overlayWindow.setVisible(false);
        overlayWindow.remove();
        logger.info("LootBoxOverlayComponent disposed, hiding overlay.");
        super.dispose();
    }

    /**
     * Draws the loot box overlay using the provided SpriteBatch. The actual rendering is handled by the stage.
     *
     * @param batch The SpriteBatch used for drawing.
     */
    @Override
    protected void draw(SpriteBatch batch) {
        // Drawing is handled by the stage
    }

}