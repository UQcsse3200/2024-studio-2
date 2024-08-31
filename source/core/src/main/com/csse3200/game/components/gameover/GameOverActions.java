package com.csse3200.game.components.gameover;

import com.badlogic.gdx.Screen;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.gamestate.GameState;
import com.csse3200.game.gamestate.SaveHandler;
import com.csse3200.game.services.ServiceContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class listens to events relevant to the Main Menu Screen and does something when one of the
 * events is triggered.
 */
public class GameOverActions extends Component {
    private static final Logger logger = LoggerFactory.getLogger(GameOverActions.class);
    private GdxGame game;

    private final Entity enemy;

    public GameOverActions(GdxGame game, Entity enemy) {
        this.game = game;
        this.enemy = enemy;
    }

    @Override
    public void create() {
        entity.getEvents().addListener("returnToMainGame", this::onReturnToMainGame);
        entity.getEvents().addListener("achievements", this::onAchievements);
    }

    private void onAchievements() {
        logger.info("Launching achievements screen");
        game.setScreen(GdxGame.ScreenType.ACHIEVEMENTS);
    }

    private void onReturnToMainGame(Screen screen, ServiceContainer container) {
        logger.info("Returning to main game screen");
        // Kill enemy.
//        this.enemy.dispose();
//        this.enemy.update();
//        container.getEntityService().unregister(enemy);
//        container.getEntityService().update();
        // Set current screen to main game screen
        game.setOldScreen(screen, container);
//        game.setScreen((GdxGame.ScreenType.MAIN_GAME));
    }

}
