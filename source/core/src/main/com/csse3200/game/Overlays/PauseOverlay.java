package com.csse3200.game.Overlays;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PauseOverlay extends Overlay {
    private static final Logger logger = LoggerFactory.getLogger(PauseOverlay.class);

    public PauseOverlay() {
        super();
        logger.debug("Initialising PauseOverlay");
        createUI();
    }

    @Override
    public void rest() {
        logger.debug("PauseOverlay rested");
        super.rest();
    }

    @Override
    public void wake() {
        logger.debug("PauseOverlay woken");
        super.wake();
    }

    private void createUI() {
        logger.debug("Creating ui");
        Stage stage = ServiceLocator.getRenderService().getStage();
        Entity ui = new Entity();
        super.add(ui);
        ui.addComponent(new PauseDisplay()).addComponent(new InputDecorator(stage, 10));
        ServiceLocator.getEntityService().register(ui);
    }
}
