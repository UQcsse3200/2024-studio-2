package com.csse3200.game.screens.minigame.bird;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
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

import java.util.ArrayList;
import java.util.List;

public class BirdScreen extends ScreenAdapter {
    private final GdxGame game;
    private final MinigameRenderer renderer;
    private final ObstacleRenderer obstacleRenderer;
    private List<Obstacle> obstacles;
    private static final Logger logger = LoggerFactory.getLogger(BirdScreen.class);
    private Stage stage;
    private Skin skin;

    public BirdScreen(GdxGame game) {
        this.game = game;
        registerServices();
        RenderFactory.createRenderer();

        this.renderer = new MinigameRenderer();

        obstacles = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            obstacles.add(new Obstacle(400 + i * 300));
        }
        this.obstacleRenderer = new ObstacleRenderer(obstacles, renderer);

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

        for (Obstacle obstacle : obstacles) {
            obstacle.setPosition(dt);
        }

        this.renderer.render();
        stage.act(dt);
        stage.draw();
    }

    private void clearBackground() {
        Gdx.gl.glClearColor(50f / 255f, 82f / 255f, 29f / 255f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

    private void createUI() {
        logger.debug("Creating bird minigame UI");
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);


        skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));


        TextButton exitButton = new TextButton("Exit", skin);
        exitButton.addListener(event -> {
            if (exitButton.isPressed()) {
                game.setScreen(GdxGame.ScreenType.MAIN_MENU);
                return true;
            }
            return false;
        });


        Table table = new Table();
        table.setFillParent(true);
        table.align(Align.topRight);
        table.pad(10);
        table.add(exitButton);

        stage.addActor(table);

        InputComponent inputComponent = ServiceLocator.getInputService().getInputFactory().createForTerminal();
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
