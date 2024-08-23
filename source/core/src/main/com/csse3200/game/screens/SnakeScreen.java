package com.csse3200.game.screens;

import com.badlogic.gdx.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.minigame.snake.SnakeGrid;
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
import com.csse3200.game.components.minigame.Direction;
import com.csse3200.game.components.minigame.snake.Apple;
import com.csse3200.game.components.minigame.snake.Snake;
import com.csse3200.game.components.minigame.snake.SnakeGame;

/**
 * Represents the screen for the Snake game.
 * Handles the rendering of the game components.
 */
public class SnakeScreen extends ScreenAdapter {
    private static final Logger logger = LoggerFactory.getLogger(SnakeScreen.class);
    private static final String[] snakeMiniGameTextures = {};
    private static final Vector2 CAMERA_POSITION = new Vector2(7.5f, 7.5f);
    private final int CELL_SIZE = 55;
    private final GdxGame game;
    private final SnakeGame snakeGame;
    private final SnakeGrid grid;
    private final Apple apple;
    private final Snake snake;
    private final Renderer renderer;
    private ShapeRenderer shapeRenderer;

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

        loadAssets();
        createUI();

        logger.debug("Initialising snake minigame entities");
        this.grid = new SnakeGrid();
        this.apple = new Apple(grid);
        this.snake = new Snake(grid, 0, 0, Direction.RIGHT, 2, 1f / 20);
        this.snakeGame = new SnakeGame(snake, apple, grid);
        this.shapeRenderer = new ShapeRenderer();
    }

    /**
     * Renders the Snake game screen, including the grid and the apple.
     *
     * This method is made for future use, so it will have a lot to change probably
     *
     * @param delta Time in seconds since the last frame.
     */
    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        ServiceLocator.getEntityService().update();
        renderer.render();

        // Check if the snake has hit the boundary
        if (snakeGame.boundaryDetection()) {
            handleBoundaryCollision();
            delta = 0;
        }

        // Render the grid and the apple
        updateDirection();
        snakeGame.attemptEatFruit();
        snake.update(delta);
        renderGrid();
        renderApple();
        renderHead();
        renderBody();
    }

    private void handleBoundaryCollision() {
        logger.info("Snake has hit the boundary!");
        System.out.println("hit the boundary");
        // TODO: Add logic to handle the game over or reset snake position.
    }

    /**
     * Renders the game grid on the screen.
     * The grid is centered and each cell is drawn with a white outline.
     */
    private void renderGrid() {
        int gridWidthInPixels = grid.getWidth() * CELL_SIZE;
        int gridHeightInPixels = grid.getHeight() * CELL_SIZE;

        // Calculate the offset to center the grid
        float offsetX = (Gdx.graphics.getWidth() - gridWidthInPixels) / 2f;
        float offsetY = (Gdx.graphics.getHeight() - gridHeightInPixels) / 2f;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);

        for (int x = 0; x < grid.getWidth(); x++) {
            for (int y = 0; y < grid.getHeight(); y++) {
                shapeRenderer.rect(offsetX + x * CELL_SIZE, offsetY + y * CELL_SIZE, CELL_SIZE,
                        CELL_SIZE);
            }
        }

        shapeRenderer.end();
    }

    /**
     * Renders the apple on the grid.
     * The apple is displayed as a filled red square on the grid.
     */
    private void renderApple() {

        int gridWidthInPixels = grid.getWidth() * CELL_SIZE;
        int gridHeightInPixels = grid.getHeight() * CELL_SIZE;

        // Calculate the offset to center the grid
        float offsetX = (Gdx.graphics.getWidth() - gridWidthInPixels) / 2f;
        float offsetY = (Gdx.graphics.getHeight() - gridHeightInPixels) / 2f;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);

        shapeRenderer.rect(offsetX + apple.getX() * CELL_SIZE, offsetY + apple.getY() * CELL_SIZE, CELL_SIZE, CELL_SIZE);

        shapeRenderer.end();
    }

    /**
     * Renders the snake head on the grid.
     * The snake head is displayed as a filled green square on the grid.
     */
    private void renderHead() {

        int gridWidthInPixels = grid.getWidth() * CELL_SIZE;
        int gridHeightInPixels = grid.getHeight() * CELL_SIZE;

        // Calculate the offset to center the grid
        float offsetX = (Gdx.graphics.getWidth() - gridWidthInPixels) / 2f;
        float offsetY = (Gdx.graphics.getHeight() - gridHeightInPixels) / 2f;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.GREEN);

        shapeRenderer.rect(offsetX + snake.getX() * CELL_SIZE, offsetY + snake.getY() * CELL_SIZE, CELL_SIZE, CELL_SIZE);

        shapeRenderer.end();
    }

    /**
     * Renders the snake head on the grid.
     * The snake head is displayed as a filled green square on the grid.
     */
    private void renderBody() {
        for (Snake.Segment segment: snake.getBodySegments()) {
            int gridWidthInPixels = grid.getWidth() * CELL_SIZE;
            int gridHeightInPixels = grid.getHeight() * CELL_SIZE;
    
            // Calculate the offset to center the grid
            float offsetX = (Gdx.graphics.getWidth() - gridWidthInPixels) / 2f;
            float offsetY = (Gdx.graphics.getHeight() - gridHeightInPixels) / 2f;
    
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.GOLD);
    
            shapeRenderer.rect(offsetX + segment.getX() * CELL_SIZE, offsetY + segment.getY() * CELL_SIZE, CELL_SIZE, CELL_SIZE);
    
            shapeRenderer.end();
        }
    }

    public Direction getInputDirection() {
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            return Direction.RIGHT;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            return Direction.LEFT;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            return Direction.UP;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            return Direction.DOWN;
        }
        return Direction.ZERO;
    }

    public void updateDirection() {
        Direction direction = getInputDirection();
        snake.updateDirectionOnInput(direction);
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
        logger.debug("Disposing snake minigame screen");

        renderer.dispose();
        unloadAssets();

        ServiceLocator.getEntityService().dispose();
        ServiceLocator.getRenderService().dispose();
        ServiceLocator.getResourceService().dispose();

        ServiceLocator.clear();

        shapeRenderer.dispose();
    }

    private void loadAssets() {
        logger.debug("Loading snake minigame assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(snakeMiniGameTextures);
        ServiceLocator.getResourceService().loadAll();
    }

    private void unloadAssets() {
        logger.debug("Unloading snake minigame assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(snakeMiniGameTextures);
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
