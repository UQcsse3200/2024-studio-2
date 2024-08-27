package com.csse3200.game.screens;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.components.minigame.snake.AssetPaths;
import com.csse3200.game.components.minigame.snake.rendering.SnakeGameRenderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
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
import com.csse3200.game.ui.terminal.Terminal;
import com.csse3200.game.ui.terminal.TerminalDisplay;
import com.csse3200.game.components.gamearea.PerformanceDisplay;
import com.csse3200.game.components.maingame.MainGameActions;
import com.csse3200.game.components.maingame.MainGameExitDisplay;
import com.csse3200.game.components.minigame.snake.SnakeGame;
import static com.csse3200.game.components.minigame.snake.AssetPaths.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;




/**
 * Represents the screen for the Snake game.
 * Handles the rendering of the game components.
 */
public class SnakeScreen extends ScreenAdapter {

    private static final Logger logger = LoggerFactory.getLogger(SnakeScreen.class);
    private final GdxGame game;
    private final SnakeGame snakeGame;
    private final SnakeGameRenderer snakeGameRenderer;
    private final Renderer renderer;
    private Texture appleTexture, snakeTexture, snakeBodyHorizontalTexture,
            snakeBodyVerticalTexture, snakeBodyBentTexture, grassTexture;



    private final BitmapFont font;


    /**
     * Initialises the SnakeScreen with the provided game instance.
     *
     * @param game The main game instance that controls the screen.
     */
    public SnakeScreen(GdxGame game) {
        this.game = game;

        logger.debug("Initialising snake minigame screen services");
        ServiceLocator.registerInputService(new InputService());
        ServiceLocator.registerResourceService(new ResourceService());
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());
        ServiceLocator.registerTimeSource(new GameTime());

        renderer = RenderFactory.createRenderer();

        // Sets the score
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(5.0f);

        loadAssets();
        createUI();


        logger.debug("Initialising snake minigame entities");
        this.snakeGame = new SnakeGame();
        snakeGameRenderer = new SnakeGameRenderer(
                this.snakeGame,
                grassTexture,
                appleTexture,
                snakeTexture,
                snakeBodyHorizontalTexture,
                snakeBodyVerticalTexture,
                snakeBodyBentTexture
        );

    }


    /**
     * Renders the Snake game screen, including the grid and the apple.
     * This method is made for future use, so it will have a lot to change probably
     *
     * @param delta Time in seconds since the last frame.
     */
    @Override
    public void render(float delta) {
        // background colour rgb 50, 82, 29, 1
        Gdx.gl.glClearColor(50f / 255f, 82f / 255f, 29f / 255f, 1f / 255f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Key functionality for escape and restart
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {  // Restart game
            // Restart the game
            game.setScreen(new SnakeScreen(game));
        }
        
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {  // Go to minigames menu
            Gdx.gl.glClearColor(248f / 255f, 249f / 255f, 178f / 255f, 1f);
            game.setScreen(new MiniGameMenuScreen(game));
        }

        // Keeps the exit button
        ServiceLocator.getEntityService().update();
        renderer.render();

        if (!snakeGame.getIsGameOver()) {

            // Check if the snake has hit the boundary
            if (snakeGame.boundaryDetection() || snakeGame.snakeCollisionDetection()) {
                snakeGame.setIsGameOver(true);
                logger.info("Snake has hit the boundary or itself!");
            } else {
                // Only update direction and snake position if the game is not over
//                updateDirection();
                snakeGame.snakeMove(delta);

            }

            // Render the grid, apple, snake, and score

            snakeGameRenderer.render(snakeGame.getScore());

        } else {
            // Go to end game screen
            game.setScreen(new EndSnakeScreen(game, snakeGame.getScore()));
        }
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

    /**
     * Disposes of resources used by the SnakeScreen.
     * This includes the ShapeRenderer used for rendering the grid and apple.
     */
    @Override
    public void dispose() {
        // colour is rgb(248,249,178,255)
        Gdx.gl.glClearColor(248f / 255f, 249f / 255f, 178f / 255f, 1f);

        logger.debug("Disposing snake minigame screen");

        renderer.dispose();
        snakeGameRenderer.dispose();
        unloadAssets();

        ServiceLocator.getEntityService().dispose();
        ServiceLocator.getRenderService().dispose();
        ServiceLocator.getResourceService().dispose();

        ServiceLocator.clear();

        font.dispose();
    }

    /*
     * Loads the assets for the game
     */
    private void loadAssets() {
        logger.debug("Loading snake minigame assets");

        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(AssetPaths.IMAGES);
        ServiceLocator.getResourceService().loadAll();

        appleTexture = resourceService.getAsset(APPLE_IMAGE, Texture.class);
        snakeTexture = resourceService.getAsset(SNAKE_HEAD_IMAGE, Texture.class);
        snakeBodyHorizontalTexture = resourceService.getAsset(SNAKE_BODY_HORIZONTAL_IMAGE,
                Texture.class);
        snakeBodyVerticalTexture = resourceService.getAsset(SNAKE_BODY_VERTICAL_IMAGE, Texture.class);
        snakeBodyBentTexture = resourceService.getAsset(SNAKE_BODY_BENT_IMAGE, Texture.class);

        grassTexture = resourceService.getAsset(GRASS_IMAGE, Texture.class);
    }

    /*
     * Unloads assests for the game
     */
    private void unloadAssets() {
        logger.debug("Unloading snake minigame assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(IMAGES);
    }

    /**
     * Creates the snake minigame's ui including components for rendering ui elements to the screen and
     * capturing and handling ui input.
     */
    private void createUI() {
        logger.debug("Creating snake minigame ui");
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
