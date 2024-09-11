package com.csse3200.game.overlays;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.screens.PausableScreen;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Represents an overlay that displays player-related information. */
public class PlayerStatsOverlay extends Overlay {
    private static final Logger logger = LoggerFactory.getLogger(PlayerStatsOverlay.class);
    private PausableScreen screen;

    /**
     * Constructs a new PlayerStatsOverlay.
     */
    public PlayerStatsOverlay(PausableScreen screen) {
        super(OverlayType.PLAYER_STATS_OVERLAY);
        this.screen = screen;
        logger.debug("Initialising PlayerStatsOverlay");
        createUI();
    }

    /**
     * Handles the rest state of the overlay.
     */
    @Override
    public void rest() {
        logger.info("PlayerStatsOverlay rested");
        super.rest();
    }

    /**
     * Handles the wake state of the overlay.
     */
    @Override
    public void wake() {
        logger.info("PlayerStatsOverlay awoken");
        super.wake();
    }

    /**
     * Creates the UI components for the player stats overlay.
     * Sets up the stage and adds the player stats display components.
     */
    private void createUI() {
        logger.debug("Creating UI");
        Stage stage = ServiceLocator.getRenderService().getStage();
        Entity ui = new Entity();
        super.add(ui);
        ui.addComponent(new PlayerStatsDisplay(screen)).addComponent(new InputDecorator(stage, 10));
        ServiceLocator.getEntityService().register(ui);
    }
}
