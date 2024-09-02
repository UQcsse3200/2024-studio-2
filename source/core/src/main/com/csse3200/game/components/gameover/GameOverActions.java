package com.csse3200.game.components.gameover;

import com.csse3200.game.GdxGame;
import com.csse3200.game.components.Component;
import com.csse3200.game.gamestate.GameState;
import com.csse3200.game.gamestate.SaveHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class listens to events relevant to the Main Menu Screen and does something when one of the
 * events is triggered.
 */
public class GameOverActions extends Component {
    private static final Logger logger = LoggerFactory.getLogger(GameOverActions.class);
    private GdxGame game;

    public GameOverActions(GdxGame game) {
        this.game = game;
    }

    @Override
    public void create() {
        entity.getEvents().addListener("exit", this::onExit);
        entity.getEvents().addListener("achievements", this::onAchievements);
        entity.getEvents().addListener("replay", this::onReplay);
    }

    /**
     * Exits the game.
     */
    private void onExit() {
        logger.info("Exit game");
        game.exit();
    }

    private void onAchievements() {
        logger.info("Launching achievements screen");
        game.setScreen(GdxGame.ScreenType.ACHIEVEMENTS);
    }
    private void onReplay() {
        logger.info("Returning to MainGame screen");
        game.setScreen(GdxGame.ScreenType.MAIN_GAME);
    }

}
