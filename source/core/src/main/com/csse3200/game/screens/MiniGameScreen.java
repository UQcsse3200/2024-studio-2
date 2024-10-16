package com.csse3200.game.screens;

import com.badlogic.gdx.Screen;
import com.csse3200.game.GdxGame;
import com.csse3200.game.services.ServiceContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

public class MiniGameScreen extends PausableScreen {
    private static final Logger logger = LoggerFactory.getLogger(MiniGameScreen.class);
    // Used for preserving screen
    protected final Screen oldScreen;
    protected final ServiceContainer oldScreenServices;

    public MiniGameScreen(GdxGame game, Screen screen, ServiceContainer container) {
        super(game);
        this.oldScreen = screen;
        this.oldScreenServices = container;
    }

    /**
     * Called from event to restart the game
     */
    public void restartGame() {
        dispose();
        try {
            game.setScreen(this.getClass()
                    .getDeclaredConstructor(GdxGame.class, Screen.class, ServiceContainer.class)
                    .newInstance(game, oldScreen, oldScreenServices));
        } catch(IllegalAccessException | IllegalArgumentException | InstantiationException |
                InvocationTargetException | NoSuchMethodException | SecurityException e) {
            logger.error("Could not restart new mini-game screen");
        }
    }

    /**
     * Called from event to exit the game back to the previous screen
     */
    public void exitGame() {
        game.setOldScreen(oldScreen, oldScreenServices);
    }
}
