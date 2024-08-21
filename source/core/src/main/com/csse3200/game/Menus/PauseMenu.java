package com.csse3200.game.Menus;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PauseMenu extends Menu{
    private static final Logger logger = LoggerFactory.getLogger(PauseMenu.class);

    public PauseMenu() {
        logger.debug("Initialising PauseMenu");
        createUI();
    }

    @Override
    public void pause() {
        logger.debug("PauseMenu paused");
        super.pause();
    }

    @Override
    public void resume() {
        logger.debug("PauseMenu resumed");
        super.resume();
    }

    private void createUI() {
        logger.debug("Creating ui");
        Stage stage = ServiceLocator.getRenderService().getStage();
        Entity ui = new Entity();
        entities.add(ui);
        ui.addComponent(new PauseDisplay()).addComponent(new InputDecorator(stage, 10));
        ServiceLocator.getEntityService().register(ui);
    }
}
