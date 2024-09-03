package com.csse3200.game.overlays;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.screens.MainGameScreen;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Represents an overlay that displays quest-related information. */
public class QuestOverlay extends Overlay {
    private static final Logger logger = LoggerFactory.getLogger(QuestOverlay.class);
    private MainGameScreen mainGameScreen;


    /**
     * Constructs a new QuestOverlay.
     */
    public QuestOverlay(MainGameScreen mainGameScreen) {
        super(OverlayType.QUEST_OVERLAY);
        this.mainGameScreen = mainGameScreen;
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
        ui.addComponent(new QuestDisplay(mainGameScreen)).addComponent(new InputDecorator(stage, 10));
        ServiceLocator.getEntityService().register(ui);
    }
}
