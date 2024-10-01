package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.csse3200.game.components.CameraComponent;
import com.csse3200.game.gamestate.GameState;
import com.csse3200.game.minigames.MiniGameNames;
import com.csse3200.game.minigames.maze.areas.MazeGameArea;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.lighting.LightingEngine;
import com.csse3200.game.lighting.LightingService;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.minigames.maze.areas.terrain.MazeTerrainFactory;
import com.csse3200.game.minigames.maze.components.MazePlayerScoreDisplay;
import com.csse3200.game.minigames.maze.components.player.MazePlayerStatsDisplay;
import com.csse3200.game.overlays.Overlay;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.Renderer;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceContainer;
import com.csse3200.game.ui.terminal.Terminal;
import com.csse3200.game.ui.terminal.TerminalDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.csse3200.game.GdxGame;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.input.InputService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.components.gamearea.PerformanceDisplay;

import static com.csse3200.game.entities.factories.RenderFactory.createCamera;

/**
 * Class for Underwater Maze Mini-Game Screen
 */
public class MazeGameScreen extends PausableScreen {

    private static final Logger logger = LoggerFactory.getLogger(MazeGameScreen.class);
    private static final String[] mazeGameTextures = {"images/heart.png"};
    private final Renderer renderer;
    private final PhysicsEngine physicsEngine;
    private final Screen oldScreen;
    private final ServiceContainer oldScreenServices;
    private static final float GAME_WIDTH = 5f;
    private final Stage stage;
    private final Skin skin;
    private float scale;
    private final MazeGameArea mazeGameArea;
    private int score = -1;

    public MazeGameScreen(GdxGame game, Screen screen, ServiceContainer container) {
        super(game);
        this.oldScreen = screen;
        this.oldScreenServices = container;
        this.scale = 1;

        this.skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));

        logger.debug("Initialising maze game screen services");
        ServiceLocator.registerTimeSource(new GameTime());

        PhysicsService physicsService = new PhysicsService();
        ServiceLocator.registerPhysicsService(physicsService);
        physicsEngine = physicsService.getPhysics();

        ServiceLocator.registerInputService(new InputService());
        ServiceLocator.registerResourceService(new ResourceService());

        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());

        Entity camera = createCamera();
        ServiceLocator.getEntityService().register(camera);
        CameraComponent camComponent = camera.getComponent(CameraComponent.class);

        renderer = new Renderer(camComponent, GAME_WIDTH);

        renderer.getDebug().renderPhysicsWorld(physicsEngine.getWorld());

        LightingEngine lightingEngine = new LightingEngine(physicsEngine.getWorld(), camComponent.getCamera());

        ServiceLocator.getRenderService().register(lightingEngine);

        ServiceLocator.registerLightingService(new LightingService(lightingEngine));

        this.stage = ServiceLocator.getRenderService().getStage();

        loadAssets();
        createUI();
        setupExitButton();

        logger.debug("Initialising maze game screen entities");
        MazeTerrainFactory terrainFactory = new MazeTerrainFactory(camComponent);

        this.mazeGameArea = new MazeGameArea(terrainFactory);
        mazeGameArea.create();
        mazeGameArea.getPlayer().getEvents().addListener("endGame", this::endGame);
        mazeGameArea.getPlayer().getEvents().addListener("restMenu", this::restMenu);
    }

    @Override
    public void render(float delta) {
        physicsEngine.update();
        ServiceLocator.getEntityService().update();
        renderer.render();
        if (score != -1) {
            logger.info("End of Maze Mini-Game");
            dispose();
            game.setScreen(new EndMiniGameScreen(game, score, MiniGameNames.MAZE, oldScreen, oldScreenServices));
        }
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
        logger.debug("Disposing main game screen");

        renderer.dispose();
        unloadAssets();

        ServiceLocator.getEntityService().dispose();
        ServiceLocator.getRenderService().dispose();
        ServiceLocator.getResourceService().dispose();

        ServiceLocator.clear();
        stage.dispose();
        skin.dispose();
    }

    private void endGame(int score) {
        this.score = score;
        GameState.minigame.addHighScore("maze", score);
        logger.info("Highscore is {}", GameState.minigame.getHighScore("maze"));
    }

    private void loadAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(mazeGameTextures);
        ServiceLocator.getResourceService().loadAll();
    }

    private void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(mazeGameTextures);
    }

    /**
     * Creates the main game's ui including components for rendering ui elements to the screen and
     * capturing and handling ui input.
     */
    private void createUI() {
        logger.debug("Creating ui");
        InputComponent inputComponent =
                ServiceLocator.getInputService().getInputFactory().createForTerminal();

        Entity ui = new Entity();
        ui.addComponent(new InputDecorator(stage, 10))
                .addComponent(new PerformanceDisplay())
                .addComponent(new Terminal())
                .addComponent(inputComponent)
                .addComponent(new TerminalDisplay());

        ServiceLocator.getEntityService().register(ui);
    }

    /**
     * Called from event to restart the game
     */
    void restartGame() {
        dispose();
        game.setScreen(new MazeGameScreen(game, oldScreen, oldScreenServices));
    }

    private void restMenu() {
        logger.info("Sending Pause");
        addOverlay(Overlay.OverlayType.PAUSE_OVERLAY);
    }

    /**
     * Called from event to exit the game back to the previous screen
     */
    void exitGame() {
        game.setOldScreen(oldScreen, oldScreenServices);
    }

    /**
     * Puts the exit button in the top right of the screen.
     * Will take the user back to the Main menu screen or game
     */
    private void setupExitButton() {

        TextButton exitButton = new TextButton("Exit", skin);
        // Scale the button's font
        exitButton.getLabel().setFontScale(scale);

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                exitGame();
            }
        });

        // Set up the table for UI layout
        Table exitButtonTable = new Table();
        exitButtonTable.setFillParent(true);
        exitButtonTable.top().right();
        exitButtonTable.add(exitButton).width(exitButton.getWidth() * scale).height(exitButton.getHeight() * scale).center().pad(10 * scale).row();

        // Add the table to the stage
        stage.addActor(exitButtonTable);
    }

    /**
     * Resize function that automatically gets called when the screen is resized.
     * Resizes all components with a consistent scale to maintain the screen's
     * original design.
     * @param width The width of the resized screen.
     * @param height The height of the resized screen.
     */
    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
        logger.trace("Resized renderer: ({} x {})", width, height);
        // Update the stage viewport
        stage.getViewport().update(width, height, true);
        float baseWidth = 1920f;
        float baseHeight = 1200f;
        float scaleWidth = width / baseWidth;
        float scaleHeight = height / baseHeight;
        scale = Math.min(scaleWidth, scaleHeight);
        stage.clear();  // Clears exit button, title, health and score
        mazeGameArea.getPlayer().getComponent(MazePlayerStatsDisplay.class).create();  // Reloads health
        mazeGameArea.getPlayer().getComponent(MazePlayerScoreDisplay.class).create();  // Reloads score
        mazeGameArea.displayUI();  // Reloads Title
        setupExitButton();
    }
}
