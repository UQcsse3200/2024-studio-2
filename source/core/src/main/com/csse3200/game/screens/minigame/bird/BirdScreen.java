package com.csse3200.game.screens.minigame.bird;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.gamearea.PerformanceDisplay;
import com.csse3200.game.components.maingame.MainGameActions;
import com.csse3200.game.components.maingame.MainGameExitDisplay;
import com.csse3200.game.components.minigame.MinigameRenderer;
import com.csse3200.game.components.minigame.flappybird.entities.Obstacle;
import com.csse3200.game.components.minigame.flappybird.rendering.ObstacleRenderer;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.factories.RenderFactory;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.input.InputService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.rendering.Renderer;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.eventservice.EventService;
import com.csse3200.game.ui.terminal.Terminal;
import com.csse3200.game.ui.terminal.TerminalDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BirdScreen extends ScreenAdapter {
    private final GdxGame game;
    private final MinigameRenderer renderer;
    private final Obstacle obstacle;
    private final ObstacleRenderer obstacleRenderer;

    private static final Logger logger = LoggerFactory.getLogger(BirdScreen.class);

    public BirdScreen(GdxGame game) {
        this.game = game;
        registerServices();
        RenderFactory.createRenderer();

        this.renderer = new MinigameRenderer();
        this.obstacle = new Obstacle(200);
        this.obstacleRenderer = new ObstacleRenderer(obstacle, renderer);
        createUI();

    }

    private void registerServices() {
        logger.debug("Registering services for BirdScreen");
        ServiceLocator.registerResourceService(new ResourceService());
        ServiceLocator.registerInputService(new InputService());
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());
        ServiceLocator.registerTimeSource(new GameTime());
        ServiceLocator.registerEventService(new EventService());
        logger.debug("Services registered successfully for BirdScreen");
    }


    @Override
    public void render(float dt) {
        clearBackground();

        this.renderer.render();
    }

    private void clearBackground() {
        Gdx.gl.glClearColor(50f / 255f, 82f / 255f, 29f / 255f, 1f / 255f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);

    }

    @Override
    public void dispose() {

    }

    private void createUI() {
        logger.debug("Creating bird minigame ui");
        Stage stage = ServiceLocator.getRenderService().getStage();
        InputComponent inputComponent =
                ServiceLocator.getInputService().getInputFactory().createForTerminal();

        Entity ui = new Entity();
        ui.addComponent(new InputDecorator(stage, 10))
                .addComponent(new PerformanceDisplay())
                .addComponent(new MainGameActions(this.game))
                .addComponent(new MainGameExitDisplay())
                .addComponent(new Terminal())
                .addComponent(inputComponent)
                .addComponent(new TerminalDisplay());

        ServiceLocator.getEntityService().register(ui);
    }
}
