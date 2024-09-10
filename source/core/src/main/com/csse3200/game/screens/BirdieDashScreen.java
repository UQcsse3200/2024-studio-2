package com.csse3200.game.screens;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.Screen;
import com.csse3200.game.components.minigames.KeyboardMiniGameInputComponent;
import com.csse3200.game.components.minigames.birdieDash.BirdieDashGame;
import com.csse3200.game.components.minigames.birdieDash.controller.KeyboardBirdInputComponent;
import com.csse3200.game.components.minigames.MiniGameNames;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.rendering.Renderer;
import com.csse3200.game.services.ServiceContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.csse3200.game.GdxGame;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.factories.RenderFactory;
import com.csse3200.game.input.InputService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.components.gamearea.PerformanceDisplay;

public class BirdieDashScreen extends PausableScreen {

    private static final Logger logger = LoggerFactory.getLogger(BirdieDashScreen.class);
    private final Renderer renderer;
    private final BitmapFont font;
    private final Skin skin;
    private final Stage stage;
    private float scale;
    private final Table exitButtonTable;
    private final BirdieDashGame birdGame;

    private final Screen oldScreen;
    private final ServiceContainer oldScreenServices;

    public BirdieDashScreen(GdxGame game, Screen screen, ServiceContainer container) {
        super(game);
        this.scale = 1;
        this.exitButtonTable = new Table();
        this.oldScreen = screen;
        this.oldScreenServices = container;
        this.birdGame = new BirdieDashGame();
        this.skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
        logger.debug("Initialising birdie dash screen services");
        ServiceLocator.registerInputService(new InputService());
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());
        ServiceLocator.registerTimeSource(new GameTime());

        renderer = RenderFactory.createRenderer();

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(5.0f);

        this.stage = ServiceLocator.getRenderService().getStage();
      //  Gdx.input.setInputProcessor(stage);

        logger.debug("Initialising birdie dash entities");

        setupExitButton();
        createUI();
    }

    @Override
    public void render(float delta) {
        if (!resting) {
            for (int i = 0; i < 20; i++) {
                birdGame.update(delta / 20);
            }
        }

        clearBackground();
        birdGame.render(delta);
        if (birdGame.getIsGameOver()) {
            dispose();
            game.setScreen(new EndMiniGameScreen(game, birdGame.getScore(), MiniGameNames.BIRD, oldScreen, oldScreenServices));
        }

        stage.act(delta);   // Update the stage
        stage.draw();       // Draw the UI (pause overlay)
    }

    public void clearBackground() {
        Gdx.gl.glClearColor(50f / 255f, 82f / 255f, 29f / 255f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        float baseWidth = 1920;
        float baseHeight = 1200;
        float scaleWidth = width / baseWidth;
        float scaleHeight = height / baseHeight;
        scale = Math.min(scaleWidth, scaleHeight);
        setupExitButton();
    }

    @Override
    public void dispose() {
        Gdx.gl.glClearColor(248f / 255f, 249f / 255f, 178f / 255f, 1f);

        logger.debug("Disposing birdie dash screen");

        renderer.dispose();
        ServiceLocator.getEntityService().dispose();
        ServiceLocator.getRenderService().dispose();
        ServiceLocator.clear();
        font.dispose();
        skin.dispose();
        stage.dispose();
    }

    private void setupExitButton() {
        exitButtonTable.clear();
        TextButton exitButton = new TextButton("Exit", skin);
        exitButton.getLabel().setFontScale(scale);

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.gl.glClearColor(248f / 255f, 249f / 255f, 178f / 255f, 1f);
                exitGame();
            }
        });

        exitButtonTable.setFillParent(true);
        exitButtonTable.top().right();
        exitButtonTable.add(exitButton).width(exitButton.getWidth() * scale).height(exitButton.getHeight() * scale).center().pad(10 * scale).row();
        stage.addActor(exitButtonTable);
    }

    private void createUI() {
        logger.debug("Creating birdie dash ui");
        Stage stage = ServiceLocator.getRenderService().getStage();
        InputComponent inputComponent = new KeyboardBirdInputComponent();

        Entity ui = new Entity();
        ui
                .addComponent(new InputDecorator(stage, 10))
                .addComponent(new PerformanceDisplay())
                .addComponent(inputComponent)
         .addComponent(new KeyboardMiniGameInputComponent());

        ui.getEvents().addListener("flap", this::flap);
        ui.getEvents().addListener("addOverlay", this::addOverlay);
        ui.getEvents().addListener("removeOverlay", this::removeOverlay);
        ui.getEvents().addListener("restart", this::restartGame);
        ui.getEvents().addListener("exit", this::exitGame);

        ServiceLocator.getEntityService().register(ui);
    }

     private void flap() {
            // Trigger the flap action in BirdieDashGame
            birdGame.flapBird();
        }

        void restartGame() {
            dispose();
            game.setScreen(new BirdieDashScreen(game, oldScreen, oldScreenServices));
        }

        void exitGame() {
            game.setOldScreen(oldScreen, oldScreenServices);
        }
}
