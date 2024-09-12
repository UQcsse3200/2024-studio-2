package com.csse3200.game.overlays;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csse3200.game.components.settingsmenu.SettingsMenuDisplay;
import com.csse3200.game.screens.PausableScreen;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the settings overlay displayed in the game.
 * The overlay manages the settings UI and allows the player to modify game settings.
 */
public class SettingsOverlay extends Overlay {
    private static final Logger logger = LoggerFactory.getLogger(SettingsOverlay.class);
    private Table rootTable;  // Root table for layout
    private Texture settingsBackground;
    private SettingsMenuDisplay settingsMenuDisplay;  // For handling settings logic
    private PausableScreen screen; // Add screen to control removal


    public SettingsOverlay(PausableScreen screen) {  // Pass screen for closing control
        super(OverlayType.SETTINGS_OVERLAY);
        this.screen = screen;
        logger.debug("Initializing SettingsOverlay");
        createUI();
    }

    /**
     * Creates and sets up the user interface for the settings overlay.
     * This method initializes the UI components and adds them to the stage.
     */
    private void createUI() {
        logger.debug("Creating UI for SettingsOverlay");
        Stage stage = ServiceLocator.getRenderService().getStage();

        // Load background texture for the settings overlay
        settingsBackground = new Texture("images/SettingBackground.png");

        // Create the root table for layout and set background
        rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.setBackground(new TextureRegionDrawable(settingsBackground));

        // Add settings table to the root table
        Table settingsTable = createSettingsTable();
        rootTable.add(settingsTable).expand().center();

        // Add root table to the stage
        stage.addActor(rootTable);
    }

    /**
     * Creates the settings table, similar to how it's done in MainMenuDisplay.
     * This method constructs the layout of the settings menu UI using SettingsMenuDisplay.
     */
    private Table createSettingsTable() {
        // Initialize settings menu display to handle the settings content
        settingsMenuDisplay = new SettingsMenuDisplay();

        // Create the table for holding settings elements (obtained from SettingsMenuDisplay)
        Table settingsTable = new Table();

        // Use the skin for UI elements
        Skin skin = UIComponent.skin;

        // Create the title and control buttons
        Label titleLabel = new Label("Settings", skin, "title-white");
        TextButton applyButton = new TextButton("Apply", skin);
        TextButton closeButton = new TextButton("Close", skin);

        // Get the content table from SettingsMenuDisplay
        Table contentTable = settingsMenuDisplay.makeSettingsTable();

        // Create layout for the "Apply" and "Close" buttons
        Table controlButtonsTable = new Table();
        controlButtonsTable.add(applyButton).padRight(10f);
        controlButtonsTable.add(closeButton).padLeft(10f);

        // Set up listeners for the buttons
        // Set up listeners for the buttons
        applyButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                logger.debug("Apply button clicked");
                settingsMenuDisplay.applyChanges();  // Apply the settings changes
                rootTable.setVisible(false);
                screen.removeOverlay();  // Properly close the settings overlay
            }
        });

        closeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                logger.debug("Close button clicked");
                rootTable.setVisible(false);
                screen.removeOverlay();  // Properly close the settings overlay
            }
        });

        // Assemble the settings table layout
        settingsTable.add(titleLabel).center().padBottom(20f);
        settingsTable.row().padTop(10f);
        settingsTable.add(contentTable).expand().fill().padBottom(20f);
        settingsTable.row().padTop(10f);
        settingsTable.add(controlButtonsTable).center().padTop(20f);

        return settingsTable;
    }

    public void dispose() {
        // Clean up settings overlay when it is no longer needed
        rootTable.clear();
        settingsBackground.dispose();
    }
}
