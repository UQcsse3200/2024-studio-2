package com.csse3200.game.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.gameover.GameOverWinDisplay;
import com.csse3200.game.components.gameover.GameOverActions;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.factories.RenderFactory;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.input.InputService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.rendering.Renderer;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceContainer;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.eventservice.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The game screen for winning the game.
 */
public class GameOverWinScreen extends ScreenAdapter {
    private static final Logger logger = LoggerFactory.getLogger(GameOverWinScreen.class);
    private final GdxGame game;
    private final Renderer renderer;
    private final Entity enemy;


    public GameOverWinScreen(GdxGame game, Entity enemy) {
        this.game = game;
        this.enemy = enemy;

        logger.debug("Initialising game over win screen services");
        ServiceLocator.registerInputService(new InputService());
        ServiceLocator.registerResourceService(new ResourceService());
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());
        ServiceLocator.registerEventService(new EventService());

        renderer = RenderFactory.createRenderer();

        createUI();
    }

    @Override
    public void render(float delta) {
        ServiceLocator.getEntityService().update();
        renderer.render();
    }

    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
        logger.trace("Resized renderer: ({} x {})", width, height);
    }

    @Override
    public void pause() {
        logger.info("Game paused");
    }

    @Override
    public void resume() {
        logger.info("Game resumed");
    }

    @Override
    public void dispose() {
        logger.debug("Disposing main menu screen");

        renderer.dispose();
        ServiceLocator.getRenderService().dispose();
        ServiceLocator.getEntityService().dispose();
        ServiceLocator.getEventService().dispose();

        ServiceLocator.clear();
    }

    /**
     * Creates the main menu's ui including components for rendering ui elements to the screen and
     * capturing and handling ui input.
     */
    private void createUI() {
        logger.debug("Creating UI");
        Stage stage = ServiceLocator.getRenderService().getStage();
        Entity ui = new Entity();
        ui.addComponent(new InputDecorator(stage, 10))
            .addComponent(new GameOverActions(this.game, this.enemy));
        ServiceLocator.getEntityService().register(ui);
    }
}
