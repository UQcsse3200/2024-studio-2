package com.csse3200.game.screens;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.Screen;
import com.csse3200.game.components.CameraComponent;
import com.csse3200.game.minigames.maze.areas.MazeGameArea;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.lighting.LightingEngine;
import com.csse3200.game.lighting.LightingService;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.minigames.maze.areas.terrain.MazeTerrainFactory;
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
import com.csse3200.game.entities.factories.RenderFactory;
import com.csse3200.game.input.InputService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.components.gamearea.PerformanceDisplay;

import static com.csse3200.game.entities.factories.RenderFactory.createCamera;

/**
 * Class for Underwater Maze Game Screen
 */
public class MazeGameScreen extends PausableScreen {

    private static final Logger logger = LoggerFactory.getLogger(MazeGameScreen.class);
    private static final String[] mainGameTextures = {"images/heart.png"};
    private final Renderer renderer;
    private final PhysicsEngine physicsEngine;
    private final LightingEngine lightingEngine;
    private final Screen oldScreen;
    private final ServiceContainer oldScreenServices;
    private static final float GAME_WIDTH = 40f;

    public MazeGameScreen(GdxGame game, Screen screen, ServiceContainer container) {
        super(game);
        this.oldScreen = screen;
        this.oldScreenServices = container;

        logger.debug("Initialising main game screen services");
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

        lightingEngine = new LightingEngine(physicsEngine.getWorld(), camComponent.getCamera());

        ServiceLocator.getRenderService().register(lightingEngine);

        ServiceLocator.registerLightingService(new LightingService(lightingEngine));

        loadAssets();
        createUI();

        logger.debug("Initialising main game screen entities");
        MazeTerrainFactory terrainFactory = new MazeTerrainFactory(camComponent);
        MazeGameArea mazeGameArea = new MazeGameArea(terrainFactory);
        mazeGameArea.create();
    }

    @Override
    public void render(float delta) {
        physicsEngine.update();
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
        logger.debug("Disposing main game screen");

        renderer.dispose();
        unloadAssets();

        ServiceLocator.getEntityService().dispose();
        ServiceLocator.getRenderService().dispose();
        ServiceLocator.getResourceService().dispose();

        ServiceLocator.clear();
    }

    private void loadAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(mainGameTextures);
        ServiceLocator.getResourceService().loadAll();
    }

    private void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(mainGameTextures);
    }

    /**
     * Creates the main game's ui including components for rendering ui elements to the screen and
     * capturing and handling ui input.
     */
    private void createUI() {
        logger.debug("Creating ui");
        Stage stage = ServiceLocator.getRenderService().getStage();
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

    /**
     * Called from event to exit the game back to the previous screen
     */
    void exitGame() {
        game.setOldScreen(oldScreen, oldScreenServices);
    }
}
