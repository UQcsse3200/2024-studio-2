package com.csse3200.game.Overlays;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Represents an overlay that is displayed when the game is paused.
  game services.
 */

public class PauseOverlay extends Overlay {
    private static final Logger logger = LoggerFactory.getLogger(PauseOverlay.class);

    /**
     * Constructs an overlay instance and initializes its UI components.
     */
    public PauseOverlay() {
        super(OverlayType.PAUSE_OVERLAY);
        logger.debug("Initialising PauseOverlay");
        createUI();
    }

    /**
     * Handles the resting state of the overlay.
     * This method is called when the overlay is put into a resting state.
     */

    @Override
    public void rest() {
        logger.debug("PauseOverlay rested");
        super.rest();
    }

    /**
     * Handles the waking state of the overlay.
     * This method is called when the overlay is brought to the foreground.
     */

    @Override
    public void wake() {
        logger.debug("PauseOverlay woken");
        super.wake();
    }

    /**
     * Creates and sets up the user interface for the pause overlay.
     * This method initializes the UI components, adds them to the stage,
     */

    private void createUI() {
        logger.debug("Creating ui");
        Stage stage = ServiceLocator.getRenderService().getStage();
        Entity ui = new Entity();
        super.add(ui);
        ui.addComponent(new PauseDisplay()).addComponent(new InputDecorator(stage, 10));
        ServiceLocator.getEntityService().register(ui);
    }
}
