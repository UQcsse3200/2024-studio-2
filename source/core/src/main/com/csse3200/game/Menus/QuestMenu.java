package com.csse3200.game.Menus;


import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.components.quests.QuestDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuestMenu extends Menu {
    private static final Logger logger = LoggerFactory.getLogger(QuestMenu.class);

    public QuestMenu() {
        logger.debug("Initialising QuestMenu");
        createUI();
    }

    @Override
    public void pause() {
        logger.info("QuestMenu paused");
        super.pause();
    }

    @Override
    public void resume() {
        logger.info("QuestMenu  resumed");
        super.resume();
    }

    private void createUI() {
        logger.debug("Creating ui");
        Stage stage = ServiceLocator.getRenderService().getStage();
        Entity ui = new Entity();
        entities.add(ui);
        ui.addComponent(new QuestDisplay()).addComponent(new InputDecorator(stage, 10));
        ServiceLocator.getEntityService().register(ui);
    }
}
