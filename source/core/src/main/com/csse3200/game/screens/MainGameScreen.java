package com.csse3200.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.quests.QuestManager;
import com.csse3200.game.gamestate.GameState;
import com.csse3200.game.gamestate.SaveHandler;
import com.csse3200.game.overlays.Overlay;
import com.csse3200.game.overlays.Overlay.OverlayType;
import com.csse3200.game.overlays.PauseOverlay;
import com.csse3200.game.overlays.QuestOverlay;
import com.csse3200.game.areas.ForestGameArea;
import com.csse3200.game.areas.GameArea;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.components.animal.AnimalSelectionActions;
import com.csse3200.game.components.maingame.MainGameActions;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.EntityChatService;
import com.csse3200.game.entities.factories.RenderFactory;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.input.InputService;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.rendering.Renderer;
import com.csse3200.game.services.eventservice.EventService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.entities.EntityChatService;
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
public class MainGameScreen extends ScreenAdapter {

  /**
   * Logger instance for logging debug, info, and warning messages.
   */
  private static final Logger logger = LoggerFactory.getLogger(MainGameScreen.class);
  /**
   * Array of texture paths used in the main game screen.
   */
  private static final String[] mainGameTextures = {"images/health_bar_x1.png",
          AnimalSelectionActions.getSelectedAnimalImagePath(), "images/player_icon_forest.png",
          "images/xp_bar.png", "images/hunger_bar.png", "images/QuestsOverlay/Quest_SBG.png", "images/PauseOverlay/TitleBG.png", "images/PauseOverlay/Button.png"};
  /**
   * Initial position of the camera in game.
   */
  private static final Vector2 CAMERA_POSITION = new Vector2(7.5f, 7.5f);
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
   * Reference to the main game instance.
   */
  private final GdxGame game;
  /**
   * Renderer for rendering game graphics.
   */
  private final Renderer renderer;
  /**
   * Physics engine for handling physics simulations in the game.
   */
  private final PhysicsEngine physicsEngine;
  /**
   * The game area where the main game takes place.
   */
  private final ForestGameArea gameArea;
  /**
   * Map of active overlay types and their statuses.
   */
  private final Map<OverlayType, Boolean> activeOverlayTypes = Overlay.getNewActiveOverlayList();


  /**
   * Constructs a MainGameScreen instance.
   * @param game The main game instance used.
   */
    public MainGameScreen(GdxGame game) {
    this.game = game;

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
    ServiceLocator.registerEntityChatService(new EntityChatService());

    ServiceLocator.registerEventService(new EventService());

    ServiceLocator.registerEntityChatService(new EntityChatService());

    renderer = RenderFactory.createRenderer();
    renderer.getCamera().getEntity().setPosition(CAMERA_POSITION);
    renderer.getDebug().renderPhysicsWorld(physicsEngine.getWorld());

    loadAssets();
    createUI();

    ServiceLocator.getEventService().getGlobalEventHandler().addListener("addOverlay",this::addOverlay);
    ServiceLocator.getEventService().getGlobalEventHandler().addListener("removeOverlay",this::removeOverlay);
    logger.debug("Initialising main game screen entities");
    TerrainFactory terrainFactory = new TerrainFactory(renderer.getCamera());
        this.gameArea = new ForestGameArea(terrainFactory, game);

    gameArea.create();
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
    gameArea.pauseMusic();
    logger.info("Game paused");
  }


  /**
   * Resumes the game and restarts music if not in resting state.
   */
  @Override
  public void resume() {
    isPaused = false;
    ServiceLocator.getEventService().getGlobalEventHandler().trigger("resetVelocity");
    if (!resting) {
      gameArea.playMusic();
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
    ServiceLocator.getEventService().dispose();

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
    ui.addComponent(new InputDecorator(stage, 10))
        .addComponent(new PerformanceDisplay())
        .addComponent(new MainGameActions(this.game))
        .addComponent(new MainGameExitDisplay())
        .addComponent(new Terminal())
        .addComponent(inputComponent)
        .addComponent(new TerminalDisplay());

    ServiceLocator.getEntityService().register(ui);
  }

  /**
   * Adds an overlay to the screen.
   * @param overlayType The type of overlay to add.
   */
  public void addOverlay(OverlayType overlayType){
    logger.debug("Attempting to Add {} Overlay", overlayType);
    if (activeOverlayTypes.get(overlayType)){
      return;
    }
      if (enabledOverlays.isEmpty()) {
          this.rest();
      }
      else {
        enabledOverlays.getFirst().rest();
      }
    switch (overlayType) {
      case QUEST_OVERLAY:
        enabledOverlays.addFirst(new QuestOverlay());
        break;
      case PAUSE_OVERLAY:
        enabledOverlays.addFirst(new PauseOverlay());
        break;
      default:
        logger.warn("Unknown Overlay type: {}", overlayType);
        break;
    }
    logger.info("Added {} Overlay", overlayType);
    activeOverlayTypes.put(overlayType,true);
  }

  /**
   * Removes the topmost overlay from the screen.
   */

  public void removeOverlay(){
    logger.info("Removing top Overlay");

    if (enabledOverlays.isEmpty()){
        this.wake();
        return;
    }
    Overlay currentFirst = enabledOverlays.getFirst();
    activeOverlayTypes.put(currentFirst.overlayType,false);
    currentFirst.remove();
    enabledOverlays.removeFirst();

    if (enabledOverlays.isEmpty()){
        this.wake();

    } else {
      enabledOverlays.getFirst().wake();
    }
  }

  /**
   * Puts the screen into a resting state, pausing music and resting all entities.
   */
  public void rest() {
    logger.info("Screen is resting");
    resting = true;
    gameArea.pauseMusic();
    ServiceLocator.getEntityService().restWholeScreen();
  }

  /**
   * Wakes the screen from a resting state.
   */
  public void wake() {
    logger.info("Screen is Awake");
    resting = false;
    ServiceLocator.getEventService().getGlobalEventHandler().trigger("resetVelocity");
    gameArea.playMusic();
    ServiceLocator.getEntityService().wakeWholeScreen();
  }
}
