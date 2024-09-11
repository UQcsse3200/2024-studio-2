package com.csse3200.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.stats.Stat;
import com.csse3200.game.components.stats.StatActions;
import com.csse3200.game.components.stats.StatDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.factories.RenderFactory;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.input.InputService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.rendering.Renderer;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The game screen for winning the game.
 */
public class EndGameStatsScreen extends ScreenAdapter {
    private static final Logger logger = LoggerFactory.getLogger(EndGameStatsScreen.class);
    private final GdxGame game;
    private final Renderer renderer;

    public EndGameStatsScreen(GdxGame game) {
        this.game = game;

        logger.debug("Initialising end game stats screen services");
        ServiceLocator.registerInputService(new InputService());
        ServiceLocator.registerResourceService(new ResourceService());
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());

        renderer = RenderFactory.createRenderer();
        renderer.getCamera().getEntity().setPosition(5f, 5f);
        createUI();
    }

    @Override
    public void render(float delta) {
        ServiceLocator.getEntityService().update();
        renderer.render();
    }

    /**
     * Resize the achievement screen window.
     * @param width The width of the new screen.
     * @param height The height of the new screen.
     */
    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
        logger.trace("Resized renderer: ({} x {})", width, height);
    }

    /**
     * Pause the current screen.
     */
    @Override
    public void pause() {
        logger.info("Game paused");
    }

    /**
     * Resume the current screen.
     */
    @Override
    public void resume() {
        logger.info("Game resumed");
    }

    /**
     * Dispose of the current screen, disposing of the relevant services.
     */
    @Override
    public void dispose() {
        logger.debug("Disposing end game stats screen");

        renderer.dispose();
        ServiceLocator.getRenderService().dispose();
        ServiceLocator.getEntityService().dispose();

        ServiceLocator.clear();
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
