package com.csse3200.game.components.settingsmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A wrapper class for the settings menu that handles display and interaction.
 */
public class SettingsMenuWrapper extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(SettingsMenuWrapper.class);

    private final SettingsMenuDisplay settingsMenuDisplay;  // Logic component for the settings menu
    private Table rootTable;  // Main table to hold the menu components
    private Texture settingsBackground;  // Background texture for the settings menu
    public boolean isVisible;

    public SettingsMenuWrapper() {
        settingsMenuDisplay = new SettingsMenuDisplay();  // Initialize the settings logic
        isVisible = false;  // Initially the settings menu is hidden
    }

    @Override
    public void create() {
        super.create();
        if (rootTable == null) { // Only create and add if not already done
            addActors();
            setVisible(false); // Ensure menu is hidden on start
        }
    }

    /**
     * Adds all UI components (title, content, buttons) to the stage.
     */
    private void addActors() {
        settingsBackground = new Texture("images/SettingBackground.png");
        Drawable settingDrawable = new TextureRegionDrawable(new TextureRegion(settingsBackground));

        // Set the screen dimensions
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        // Create the root table and set background
        rootTable = new Table();
        rootTable.setSize(663, 405);  // Size of the settings menu
        rootTable.setBackground(settingDrawable);


        // Create the title section
        Table topTable = new Table();
        topTable.top().padTop(30);
        Label title = new Label("Settings", skin, "title-white");
        title.setFontScale(1.2f); // Adjust the scale of the font if necessary
        topTable.add(title).expandX().center().padTop(10);
        topTable.row();

        // Close button on the top right
        Button closeButton = new Button(new TextureRegionDrawable(new TextureRegion(new Texture("images/CloseButton.png"))));
        topTable.add(closeButton).size(60, 60).right().expandX().padRight(10).padTop(10);

        // Create the content table using settingsMenuDisplay
        Table contentTable = settingsMenuDisplay.makeSettingsTable();

        // Create the bottom-right apply button
        Table bottomRightTable = new Table();
        bottomRightTable.bottom();  // Align contents to bottom-right
        TextButton applyButton = new TextButton("Apply", skin);
        bottomRightTable.add(applyButton).size(80, 40).padBottom(30f).padRight(30f);

        // Add everything to the root table
        rootTable.add(topTable).expandX().fillX();  // Top-right table for title and close button
        rootTable.row().padTop(20f);
        rootTable.add(contentTable).expandX().expandY().padLeft(30).padRight(30);  // Main content
        rootTable.row().padTop(20f);
        rootTable.add(bottomRightTable).expandX().right();  // Apply button

        // Add the root table to the stage
        stage.addActor(rootTable);
        rootTable.setZIndex(3000);

        // Center the root table on the screen
        updatePosition();

        // Add listeners for buttons
        closeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                logger.info("Close button clicked");
                setVisible(false);  // Hide the settings menu
            }
        });

        applyButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                logger.info("Apply button clicked");
                settingsMenuDisplay.applyChanges();  // Apply settings changes
                setVisible(false);  // Hide the settings menu after applying changes
            }
        });
    }

    /**
     * Toggles the visibility of the settings menu.
     */
    public void toggleVisibility() {
        setVisible(!isVisible);
    }

    /**
     * Sets the visibility of the settings menu.
     * @param visible true to show, false to hide
     */
    public void setVisible(boolean visible) {
        rootTable.setVisible(visible);
        isVisible = visible;
        //table.setTouchable(visible ? Touchable.disabled : Touchable.enabled);  // Toggle table touch
    }

    /**
     * Updates the position of the settings menu based on screen size.
     */
    public void updatePosition() {
        if (rootTable != null) {
            // Center the menu on the screen
            float screenWidth = Gdx.graphics.getWidth();
            float screenHeight = Gdx.graphics.getHeight();
            rootTable.setPosition(
                    (screenWidth - rootTable.getWidth()) / 2,
                    (screenHeight - rootTable.getHeight()) / 2
            );
        }
    }

    @Override
    protected void draw(SpriteBatch batch) {
        // Drawing handled by stage
    }

    @Override
    public void update() {
        stage.act(ServiceLocator.getTimeSource().getDeltaTime());
    }

    @Override
    public void dispose() {
        rootTable.clear();
        super.dispose();
    }
}
