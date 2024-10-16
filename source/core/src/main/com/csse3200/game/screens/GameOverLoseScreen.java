package com.csse3200.game.screens;


import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.gameover.GameOverActions;
import com.csse3200.game.components.gameover.GameOverLoseDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.input.InputService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The game screen for losing the game.
 */
public class GameOverLoseScreen extends ResizableScreen {
    private static final Logger logger = LoggerFactory.getLogger(GameOverLoseScreen.class);
    private final GdxGame game;
    private static final String[] mainMenuTextures = {"images/box_boy_title.png"};

    public GameOverLoseScreen(GdxGame game) {
        super();
        
        this.game = game;

        logger.debug("Initialising game over win screen services");
        ServiceLocator.registerInputService(new InputService());
        ServiceLocator.registerResourceService(new ResourceService());

        loadAssets();
        createUI();
    }

    private void loadAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(mainMenuTextures);
        resourceService.loadAll();
    }

    private void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(mainMenuTextures);
    }
    /**
     * Creates the main menu's ui including components for rendering ui elements to the screen and
     * capturing and handling ui input.
     */
    private void createUI() {
        logger.debug("Creating UI");
        Stage stage = ServiceLocator.getRenderService().getStage();
        Entity ui = new Entity();
        ui.addComponent(new GameOverLoseDisplay())
                .addComponent(new InputDecorator(stage, 10))
                .addComponent(new GameOverActions(game));
        ServiceLocator.getEntityService().register(ui);
    }
}
