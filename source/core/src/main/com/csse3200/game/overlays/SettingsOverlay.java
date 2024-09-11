package com.csse3200.game.overlays;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.screens.PausableScreen;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import com.csse3200.game.components.settingsmenu.SettingsMenuDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the settings overlay displayed in the game.
 * The overlay manages the settings UI and allows the player to modify game settings.
 */
public class SettingsOverlay extends Overlay {
    private static final Logger logger = LoggerFactory.getLogger(SettingsOverlay.class);
    private PausableScreen screen;

    /**
     * Constructs a new SettingsOverlay.
     *
     * @param screen the pausable screen that this overlay is part of
     */
    public SettingsOverlay(PausableScreen screen) {
        super(OverlayType.SETTINGS_OVERLAY);
        this.screen = screen;
        logger.debug("Initializing SettingsOverlay");
        createUI();
    }

    /**
     * Handles the resting state of the overlay.
     * This method is called when the overlay is put into a resting state.
     */
    @Override
    public void rest() {
        logger.debug("SettingsOverlay rested");
        super.rest();
    }

    /**
     * Handles the waking state of the overlay.
     * This method is called when the overlay is brought to the foreground.
     */
    @Override
    public void wake() {
        logger.debug("SettingsOverlay woken");
        super.wake();
    }

    /**
     * Creates and sets up the user interface for the settings overlay.
     * This method initializes the UI components and adds them to the stage.
     */
    private void createUI() {
        logger.debug("Creating UI for SettingsOverlay");
        Stage stage = ServiceLocator.getRenderService().getStage();
        Entity ui = new Entity();
        ui.addComponent(new SettingsMenuDisplay());  // Use the SettingsMenuDisplay for settings UI
        ui.addComponent(new InputDecorator(stage, 10));
        super.add(ui);
        ServiceLocator.getEntityService().register(ui);
    }
}
