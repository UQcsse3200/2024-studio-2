package com.csse3200.game.components.story;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class listens to events relevant to the Main Menu Screen and does something when one of the
 * events is triggered.
 */
public class StoryActions extends Component {
    private static final Logger logger = LoggerFactory.getLogger(StoryActions.class);
    private final GdxGame game;
    private final int finalScreen = 5;

    public StoryActions(GdxGame game) {
        this.game = game;
    }

    @Override
    public void create() {
        entity.getEvents().addListener("next", this::onNext);
        entity.getEvents().addListener("back", this::onBack);
    }

    /**
     * Exits the game.
     */
    private void onNext(int screenNum) {
        if(screenNum == finalScreen) {
            game.setScreen(GdxGame.ScreenType.LOADING_SCREEN);
            return;
        }
        screenNum += 1;
        logger.debug("Creating UI");
        Stage stage = ServiceLocator.getRenderService().getStage();
        Entity ui = new Entity();
        ui.addComponent(new StoryDisplay(screenNum))
                .addComponent(new InputDecorator(stage, 10))
                .addComponent(new StoryActions(game));
        ServiceLocator.getEntityService().register(ui);
    }

    private void onBack(int screenNum) {
        screenNum -= 1;
        logger.debug("Creating UI");
        Stage stage = ServiceLocator.getRenderService().getStage();
        Entity ui = new Entity();
        ui.addComponent(new StoryDisplay(screenNum))
                .addComponent(new InputDecorator(stage, 10))
                .addComponent(new StoryActions(game));
        ServiceLocator.getEntityService().register(ui);
    }

}
