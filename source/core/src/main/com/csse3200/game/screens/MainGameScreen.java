package com.csse3200.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.player.KeyboardPlayerInputComponent;
import com.csse3200.game.overlays.Overlay;
import com.csse3200.game.overlays.Overlay.OverlayType;
import com.csse3200.game.overlays.PauseOverlay;
import com.csse3200.game.overlays.QuestOverlay;
import com.csse3200.game.areas.ForestGameArea;
import com.csse3200.game.areas.GameArea;
import com.csse3200.game.areas.MapHandler;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.components.animal.AnimalSelectionActions;
import com.csse3200.game.components.maingame.MainGameActions;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.DialogueBoxService;
import com.csse3200.game.entities.factories.RenderFactory;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.input.InputService;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.rendering.Renderer;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.terminal.Terminal;
import com.csse3200.game.ui.terminal.TerminalDisplay;
import com.csse3200.game.components.maingame.MainGameExitDisplay;
import com.csse3200.game.components.gamearea.PerformanceDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;

/**
 * The game screen containing the main game.
 *
 * <p>Details on libGDX screens: https://happycoding.io/tutorials/libgdx/game-screens
 */
public class MainGameScreen extends PausableScreen {

  /**
   * Logger instance for logging debug, info, and warning messages.
   */
  private static final Logger logger = LoggerFactory.getLogger(MainGameScreen.class);
  /**
   * Array of texture paths used in the main game screen.
   */
  private static final String[] mainGameTextures = {"images/health_bar_x1.png",
          AnimalSelectionActions.getSelectedAnimalImagePath(), "images/player_icon_forest.png", "images/vignette.png",
          "images/xp_bar.png", "images/hunger_bar.png", "images/QuestsOverlay/Quest_SBG.png", "images/PauseOverlay/TitleBG.png", "images/PauseOverlay/Button.png"};
  /**
   * Initial position of the camera in game.
   */
  private static final Vector2 CAMERA_POSITION = new Vector2(7.5f, 7.5f);
  /**
   * Flag indicating whether the game is currently paused.
   */
  private boolean isPaused = false;

  /**
   * Renderer for rendering game graphics.
   */
  private final Renderer renderer;
  /**
   * Physics engine for handling physics simulations in the game.
   */
  private final PhysicsEngine physicsEngine;

  /**
   * Constructs a MainGameScreen instance.
   * @param game The main game instance used.
   */
    public MainGameScreen(GdxGame game) {
      super(game);

      logger.debug("Initialising main game screen services");
      ServiceLocator.registerTimeSource(new GameTime());

      PhysicsService physicsService = new PhysicsService();
      ServiceLocator.registerPhysicsService(physicsService);
      physicsEngine = physicsService.getPhysics();

      ServiceLocator.registerInputService(new InputService());
      ServiceLocator.registerResourceService(new ResourceService());

      ServiceLocator.registerEntityService(new EntityService());
      ServiceLocator.registerRenderService(new RenderService());
      //register the EntityChatService

      renderer = RenderFactory.createRenderer();
      renderer.getCamera().getEntity().setPosition(CAMERA_POSITION);
      renderer.getDebug().renderPhysicsWorld(physicsEngine.getWorld());

      loadAssets();
      createUI();
      logger.debug("Initialising main game screen entities");
      
      setMap(MapHandler.MapType.FOREST);
      //MapHandler.switchMapTo(MapHandler.MapType.FOREST, getRenderer(), game, false);

      Stage stage = ServiceLocator.getRenderService().getStage();
      ServiceLocator.registerDialogueBoxService(new DialogueBoxService(stage));

    }

    public void setMap(MapHandler.MapType mapType) {
      MapHandler.switchMapTo(mapType, renderer, game, true);
    }

  /**
   * Renders the game screen and updates the physics engine, game entities, and renderer.
   * @param delta The time elapsed since the last render call.
   */
  @Override
  public void render(float delta) {
    if (!isPaused){
      physicsEngine.update();
      ServiceLocator.getEntityService().update();
      renderer.render();
    }
  }


  /**
   * Resizes the renderer to fit dimensions.
   * @param width  width of the screen.
   * @param height height of the screen.
   */
  @Override
  public void resize(int width, int height) {
    renderer.resize(width, height);
    logger.trace("Resized renderer: ({} x {})", width, height);
  }

  /**
   * Pauses the game, stopping any ongoing music and setting the paused state.
   */
  @Override
  public void pause() {
    isPaused = true;
    MapHandler.getCurrentMap().pauseMusic();
    logger.info("Game paused");
  }


  /**
   * Resumes the game and restarts music if not in resting state.
   */
  @Override
  public void resume() {
    isPaused = false;
    KeyboardPlayerInputComponent inputComponent = MapHandler.getCurrentMap().getPlayer().getComponent(KeyboardPlayerInputComponent.class);
    inputComponent.resetVelocity();
    if (!resting) {
      MapHandler.getCurrentMap().playMusic();
    }
    logger.info("Game resumed");
  }

  /**
   * Disposes of resources used by the game screen.
   */
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

  /**
   * Loads assets required for the main game screen.
   */
  private void loadAssets() {
    logger.debug("Loading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.loadTextures(mainGameTextures);
    ServiceLocator.getResourceService().loadAll();
  }

  /**
   * Unloads assets that are no longer needed.
   */
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

    Component mainGameActions = new MainGameActions(this.game);
    ui.addComponent(new InputDecorator(stage, 10))
        .addComponent(new PerformanceDisplay())
        .addComponent(mainGameActions)
        .addComponent(new MainGameExitDisplay(mainGameActions))
        .addComponent(new Terminal())
        .addComponent(inputComponent)
        .addComponent(new TerminalDisplay());

    ServiceLocator.getEntityService().register(ui);
  }

  /**
   * Puts the screen into a resting state, pausing music and resting all entities.
   */
  public void rest() {
    super.rest();
    MapHandler.getCurrentMap().pauseMusic();
  }

  /**
   * Wakes the screen from a resting state.
   */
  public void wake() {
    super.wake();
    KeyboardPlayerInputComponent inputComponent = MapHandler.getCurrentMap().getPlayer().getComponent(KeyboardPlayerInputComponent.class);
    inputComponent.resetVelocity();
    MapHandler.getCurrentMap().playMusic();
  }
}
