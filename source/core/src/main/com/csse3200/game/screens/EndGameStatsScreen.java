package com.csse3200.game.screens;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.stats.StatDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.input.InputService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The game screen for winning the game.
 */
public class EndGameStatsScreen extends ResizableScreen {
    private static final Logger logger = LoggerFactory.getLogger(EndGameStatsScreen.class);
    private final GdxGame game;

    public EndGameStatsScreen(GdxGame game) {
        this.game = game;

        logger.debug("Initialising end game stats screen services");
        ServiceLocator.registerInputService(new InputService());
        ServiceLocator.registerResourceService(new ResourceService());

        renderer.getCamera().getEntity().setPosition(5f, 5f);
        createUI();
    }

    /**
     * Creates the end game stat screen's ui including components for rendering
     * ui elements to the screen and capturing and handling ui input.
     */
    private void createUI() {
        logger.debug("Creating UI");
        Stage stage = ServiceLocator.getRenderService().getStage();
        Entity ui = new Entity();
        ui.addComponent(new InputDecorator(stage, 10))
                .addComponent(new StatDisplay(game));

        ServiceLocator.getEntityService().register(ui);
    }
}
