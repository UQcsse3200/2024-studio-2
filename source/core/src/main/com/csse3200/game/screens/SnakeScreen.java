package com.csse3200.game.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.csse3200.game.components.minigame.snake.controller.Events;
import com.badlogic.gdx.Screen;
import com.csse3200.game.components.minigame.Direction;
import com.csse3200.game.components.minigame.KeyboardMiniGameInputComponent;
import com.csse3200.game.components.minigame.snake.controller.KeyboardSnakeInputComponent;
import com.csse3200.game.components.minigame.snake.rendering.SnakeGameRenderer;
import com.csse3200.game.overlays.Overlay;
import com.csse3200.game.overlays.PauseOverlay;
import com.csse3200.game.services.ServiceContainer;
import com.csse3200.game.services.eventservice.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.csse3200.game.GdxGame;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.factories.RenderFactory;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.input.InputService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.rendering.Renderer;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.components.gamearea.PerformanceDisplay;
import com.csse3200.game.components.maingame.MainGameExitDisplay;
import com.csse3200.game.components.minigame.snake.SnakeGame;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.csse3200.game.components.minigame.MiniGameNames;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;

/**
 * Represents the screen for the Snake game.
 * Handles the rendering of the game components.
 */
public class SnakeScreen extends ScreenAdapter {

    private static final Logger logger = LoggerFactory.getLogger(SnakeScreen.class);
    private final GdxGame game;
    private final SnakeGame snakeGame;
    private final SnakeGameRenderer snakeRenderer;
    private final Renderer renderer;
    private final BitmapFont font;
    private final Skin skin;
    private final Stage stage;
    private float scale;
    private Table exitButtonTable;

    /**
     * Queue of currently enabled overlays in the game screen.
     */
    private final Deque<Overlay> enabledOverlays = new LinkedList<>();
    /**
     * Flag indicating whether the game is currently paused.
     */
    private boolean isPaused = false;
    /**
     * Flag indicating whether the screen is in a resting state.
     */
    private boolean resting = false;
    /**
     * Map of active overlay types and their statuses.
     */
    private final Map<Overlay.OverlayType, Boolean> activeOverlayTypes = Overlay.getNewActiveOverlayList();
    private final Screen oldScreen;
    private final ServiceContainer oldScreenServices;

    /**
     * Initialises the SnakeScreen with the provided game instance.
     *
     * @param game The main game instance that controls the screen.
     */
    public SnakeScreen(GdxGame game, Screen screen, ServiceContainer container) {
        this.game = game;
        this.scale = 1;
        this.exitButtonTable = new Table();
        this.oldScreen = screen;
        this.oldScreenServices = container;

        this.skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
        logger.debug("Initialising snake minigame screen services");
        ServiceLocator.registerInputService(new InputService());
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());
        ServiceLocator.registerTimeSource(new GameTime());
        ServiceLocator.registerEventService(new EventService());

        ServiceLocator.getEventService().getGlobalEventHandler().addListener("addOverlay",this::addOverlay);
        ServiceLocator.getEventService().getGlobalEventHandler().addListener("removeOverlay",this::removeOverlay);

        renderer = RenderFactory.createRenderer();

        // Sets the score
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(5.0f);

        this.stage = ServiceLocator.getRenderService().getStage();

        logger.debug("Initialising snake minigame entities");
        this.snakeGame = new SnakeGame();
        this.snakeRenderer = new SnakeGameRenderer(snakeGame);

        setupExitButton();
        createUI();
    }

    /**
     * Renders the Snake game screen, including the grid and the apple.
     * This method is made for future use, so it will have a lot to change probably
     *
     * @param delta Time in seconds since the last frame.
     */
    @Override
    public void render(float delta) {
        clearBackground();

        ServiceLocator.getEntityService().update();
        renderer.render();

        updateGame(delta);

        if (!snakeGame.getIsGameOver()) {
            snakeRenderer.render(snakeGame.getScore());
        }
    }


    /**
     * Clears the screen with a specific background color.
     */
    public void clearBackground() {
        Gdx.gl.glClearColor(50f / 255f, 82f / 255f, 29f / 255f, 1f / 255f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    /**
     * Updates the game state, including moving the snake and checking for game-over conditions.
     *
     * @param delta Time in seconds since the last frame.
     */
    private void updateGame(float delta) {
        if (resting) {
            return;
        }
        snakeGame.snakeMove(delta);
        if (snakeGame.getIsGameOver()) {
            dispose();
            game.setScreen(new EndMiniGameScreen(game, snakeGame.getScore(), MiniGameNames.SNAKE, oldScreen, oldScreenServices));
        }
    }

    /**
     * Resizes the renderer
     * @param width the width to resize to
     * @param height the height to resize to
     */
    @Override
    public void resize(int width, int height) {
//        logger.trace("Resized renderer: ({} x {})", width, height);
//        renderer.resize(width, height);
        stage.getViewport().update(width, height, true);
        float baseWidth = 1920f;
        float baseHeight = 1200f;
        float scaleWidth = width / baseWidth;
        float scaleHeight = height / baseHeight;
        scale = Math.min(scaleWidth, scaleHeight);
//        stage.clear();
        setupExitButton();
        snakeRenderer.resize(width, height);
    }

    /**
     * Game pause
     */
    @Override
    public void pause() {
        logger.info("Game paused");
    }

    /**
     * Game resume
     */
    @Override
    public void resume() {
        logger.info("Game resumed");
    }

    /**
     * Disposes of resources used by the SnakeScreen.
     * This includes the ShapeRenderer used for rendering the grid and apple.
     */
    @Override
    public void dispose() {
        // colour is rgb(248,249,178,255)
        Gdx.gl.glClearColor(248f / 255f, 249f / 255f, 178f / 255f, 1f);

        logger.debug("Disposing snake mini-game screen");

        renderer.dispose();
        snakeRenderer.dispose();
        ServiceLocator.getEntityService().dispose();
        ServiceLocator.getRenderService().dispose();
        ServiceLocator.clear();
        font.dispose();
        skin.dispose();
        stage.dispose();
    }

    private void setupExitButton() {
        // Set up exit button
        exitButtonTable.clear();
        TextButton exitButton = new TextButton("Exit", skin);
        exitButton.getLabel().setFontScale(scale);

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Return to main menu and original screen colour
                Gdx.gl.glClearColor(248f / 255f, 249f / 255f, 178f / 255f, 1f);
                game.setScreen(new MainMenuScreen(game));
            }
        });

        // Set up the table for UI layout
        exitButtonTable.setFillParent(true);
        exitButtonTable.top().right();
        exitButtonTable.add(exitButton).width(exitButton.getWidth() * scale).height(exitButton.getHeight() * scale).center().pad(10 * scale).row();

        // Add the table to the stage
        stage.addActor(exitButtonTable);
    }

    /**
     * Creates the snake mini-game's ui including components for rendering ui elements to the screen and
     * capturing and handling ui input.
     */
    private void createUI() {
        logger.debug("Creating snake mini-game ui");
        InputComponent inputComponent =
                new KeyboardSnakeInputComponent();

        Entity ui = new Entity();
        ui
                .addComponent(new PerformanceDisplay())
                .addComponent(inputComponent)
                .addComponent(new KeyboardMiniGameInputComponent());

        ui.getEvents().addListener("move", this::handleSnakeInput);
        ui.getEvents().addListener("restart", this::restartGame);
        ui.getEvents().addListener("exit", this::exitGame);
        //ui.getEvents().addListener("pause", this::pause);
        ServiceLocator.getEntityService().register(ui);
    }

    /**
     * Update snake direction based on an input direction.
     * If the input direction is reversed 180 degrees from the current
     * direction of the snake, it will not change.
     *
     * @param direction the direction to update to
     */
    void handleSnakeInput(Direction direction) {
        snakeGame.handleSnakeInput(direction);
    }

    /**
     * Handles player input for restarting or exiting the game.
     *
     * @return true if a screen change was triggered, false otherwise.
     */
    void restartGame() {
        dispose();
        game.setScreen(new SnakeScreen(game, oldScreen, oldScreenServices));
    }

    void exitGame() {
        game.setOldScreen(oldScreen, oldScreenServices);
    }

    public void addOverlay(Overlay.OverlayType overlayType){
        logger.info("Adding Overlay {}", overlayType);
        if (enabledOverlays.isEmpty()) {
            this.rest();
        }
        else {
            enabledOverlays.getFirst().rest();
        }
        switch (overlayType) {
            case PAUSE_OVERLAY:
                enabledOverlays.addFirst(new PauseOverlay());
                break;
            default:
                logger.warn("Unknown Overlay type: {}", overlayType);
                break;
        }
    }

    public void removeOverlay(){
        logger.debug("Removing top Overlay");

        if (enabledOverlays.isEmpty()){
            this.wake();
            return;
        }

        enabledOverlays.getFirst().remove();

        enabledOverlays.removeFirst();

        if (enabledOverlays.isEmpty()){
            this.wake();

        } else {
            enabledOverlays.getFirst().wake();
        }
    }

    public void rest() {
        logger.info("Screen is resting");
        resting = true;
        ServiceLocator.getEntityService().restWholeScreen();
    }

    public void wake() {
        logger.info("Screen is Awake");
        resting = false;
        ServiceLocator.getEntityService().wakeWholeScreen();
    }
}

