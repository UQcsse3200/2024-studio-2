package com.csse3200.game.overlays;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.screens.PausableScreen;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Represents an overlay that displays quest-related information. */
public class QuestOverlay extends Overlay {
    private static final Logger logger = LoggerFactory.getLogger(QuestOverlay.class);
    private PausableScreen screen;


    /**
     * Constructs a new QuestOverlay.
     */
    public QuestOverlay(PausableScreen screen) {
        super(OverlayType.QUEST_OVERLAY);
        this.screen = screen;
        logger.debug("Initialising QuestOverlay");
        createUI();
    }

    /**
     * Handles the rest state of the overlay.
     */
    @Override
    public void rest() {
        logger.info("QuestOverlay rested");
        super.rest();
    }

    /**
     * Handles the wake state of the overlay.
     */
    @Override
    public void wake() {
        logger.info("QuestOverlay awoken");
        super.wake();
    }

    /**
     * Creates the UI components for the quest overlay.
     * Sets up the stage and adds the quest display components.
     */
    private void createUI() {
        logger.debug("Creating ui");
        Stage stage = ServiceLocator.getRenderService().getStage();
        Entity ui = new Entity();
        super.add(ui);
        ui.addComponent(new QuestDisplay(screen)).addComponent(new InputDecorator(stage, 10));
        ServiceLocator.getEntityService().register(ui);
    }
}
