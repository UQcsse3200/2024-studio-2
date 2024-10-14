package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.gamestate.GameState;
import com.csse3200.game.gamestate.SaveHandler;
import com.csse3200.game.minigames.Direction;
import com.csse3200.game.minigames.KeyboardMiniGameInputComponent;
import com.csse3200.game.minigames.snake.controller.KeyboardSnakeInputComponent;
import com.csse3200.game.minigames.snake.rendering.SnakeGameRenderer;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.overlays.Overlay;
import com.csse3200.game.services.AudioManager;
import com.csse3200.game.services.ServiceContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.gamearea.PerformanceDisplay;
import com.csse3200.game.components.login.PlayFab;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.factories.RenderFactory;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.input.InputService;
import com.csse3200.game.minigames.MiniGameNames;
import com.csse3200.game.minigames.snake.SnakeGame;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.rendering.Renderer;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;

import static com.csse3200.game.minigames.snake.AssetPaths.SOUNDS;

/**
 * Represents the screen for the Snake game.
 * Handles the rendering of the game components.
 */
public class SnakeScreen extends PausableScreen {

    private static final Logger logger = LoggerFactory.getLogger(SnakeScreen.class);
    private final SnakeGame snakeGame;
    private final SnakeGameRenderer snakeRenderer;
    private final Renderer renderer;
    private final BitmapFont font;
    private final Skin skin;
    private final Stage stage;
    private float scale;
    private final Screen oldScreen;
    private final ServiceContainer oldScreenServices;
    private final Texture backgroundTexture;
    private final SpriteBatch spriteBatch;
    private final Entity ui;

    /**
     * Initialises the SnakeScreen with the provided game instance.
     *
     * @param game The main game instance that controls the screen.
     */
    public SnakeScreen(GdxGame game, Screen screen, ServiceContainer container) {
        super(game);
        this.scale = 1;
        this.oldScreen = screen;
        this.oldScreenServices = container;
        this.ui = new Entity();

        this.backgroundTexture = new  Texture(Gdx.files.internal("images/minigames/Background.png"));

        this.spriteBatch = new SpriteBatch();

        this.skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
        logger.debug("Initialising snake minigame screen services");
        ServiceLocator.registerInputService(new InputService());
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());
        ServiceLocator.registerTimeSource(new GameTime());

        renderer = RenderFactory.createRenderer();

        // Sets the score
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(5.0f);

        stage = ServiceLocator.getRenderService().getStage();
        logger.debug("Initialising snake minigame entities");
        snakeGame = new SnakeGame();
        snakeRenderer = new SnakeGameRenderer(snakeGame);

        // ensure sounds are loaded
        ServiceLocator.getResourceService().loadSounds(SOUNDS);
        ServiceLocator.getResourceService().loadMusic(new String[]{"sounds/minigames/snake-bg.mp3"});
        ServiceLocator.getResourceService().loadAll();
        AudioManager.playMusic("sounds/minigames/snake-bg.mp3", true);

        //setupExitButton();
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

        renderer.render();

        drawBackground();

        updateGame(delta);

        if (!snakeGame.getIsGameOver()) {
            snakeRenderer.render(snakeGame.getScore());
        }

        stage.act(delta);   // Update the stage
        stage.draw();       // Draw the UI (pause overlay)
    }

    /**
     * Clears the screen with a specific background color.
     */
    public void drawBackground() {
        Gdx.gl.glClearColor(50f / 255f, 82f / 255f, 29f / 255f, 1f / 255f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Draw the background image
        spriteBatch.begin();
        spriteBatch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        spriteBatch.end();
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
            GameState.minigame.addHighScore("snake", snakeGame.getScore());
            PlayFab.submitScore("Snake", snakeGame.getScore());
//            logger.info("{}", GameState.minigame.getHighScore("snake"));
            SaveHandler.save(GameState.class, "saves", FileLoader.Location.LOCAL);
            dispose();
            game.setScreen(new EndMiniGameScreen(game, snakeGame.getScore(), MiniGameNames.SNAKE, oldScreen, oldScreenServices));
        }
    }

    /**
     * Resizes the elements on the screen including game elements, score board and exit button
     * @param width the width to resize to
     * @param height the height to resize to
     */
    @Override
    public void resize(int width, int height) {
        float baseWidth = 1920f;
        float baseHeight = 1200f;
        float scaleWidth = width / baseWidth;
        float scaleHeight = height / baseHeight;
        scale = Math.min(scaleWidth, scaleHeight);
        stage.getViewport().update(width, height, true);
        if (scale == 0) {  // Screen has been minimised
            scale = 1;
            ui.getEvents().trigger("addOverlay", Overlay.OverlayType.PAUSE_OVERLAY);
        }
        //setupExitButton();
        snakeRenderer.resize(width, height);
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
        backgroundTexture.dispose();
        spriteBatch.dispose();
        ServiceLocator.getEntityService().dispose();
        ServiceLocator.getRenderService().dispose();
        ServiceLocator.clear();
        font.dispose();
        skin.dispose();
        stage.dispose();
    }

    /**
     * Creates the snake mini-game's ui including components for rendering ui elements to the screen and
     * capturing and handling ui input.
     */
    private void createUI() {
        logger.debug("Creating snake mini-game ui");
        InputComponent inputComponent =
                new KeyboardSnakeInputComponent();

        Stage uiStage = ServiceLocator.getRenderService().getStage();

        ui
                .addComponent(new InputDecorator(uiStage, 10))
                .addComponent(new PerformanceDisplay())
                .addComponent(inputComponent)
                .addComponent(new KeyboardMiniGameInputComponent());

        ui.getEvents().addListener("addOverlay",this::addOverlay);
        ui.getEvents().addListener("removeOverlay",this::removeOverlay);
        ui.getEvents().addListener("move", this::handleSnakeInput);
        ui.getEvents().addListener("restart", this::restartGame);
        ui.getEvents().addListener("exit", this::exitGame);
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
     */
    void restartGame() {
        dispose();
        game.setScreen(new SnakeScreen(game, oldScreen, oldScreenServices));
    }

    /**
     * Exits the game
     */
    void exitGame() {
        game.setOldScreen(oldScreen, oldScreenServices);
    }
}
