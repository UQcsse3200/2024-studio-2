package com.csse3200.game.components.story;

import com.csse3200.game.GdxGame;
import com.csse3200.game.components.Component;
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
        logger.info("Exit game");
        game.exit();
    }

    private void onBack() {
        logger.info("Launching achievements screen");
        game.setScreen(GdxGame.ScreenType.ACHIEVEMENTS);
    }

}
