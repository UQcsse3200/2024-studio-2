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
    private void onNext() {
        logger.debug("Creating UI");
        Stage stage = ServiceLocator.getRenderService().getStage();
        Entity ui = new Entity();
        ui.addComponent(new StoryDisplay(1))
                .addComponent(new InputDecorator(stage, 10))
                .addComponent(new StoryActions(game));
        ServiceLocator.getEntityService().register(ui);
    }

    private void onBack() {
        logger.info("Launching achievements screen");
        game.setScreen(GdxGame.ScreenType.ACHIEVEMENTS);
    }

}
