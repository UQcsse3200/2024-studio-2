package com.csse3200.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
import com.csse3200.game.Overlays.Overlay;
import com.csse3200.game.Overlays.Overlay.OverlayType;
import com.csse3200.game.Overlays.PauseOverlay;
import com.csse3200.game.Overlays.QuestOverlay;
import com.csse3200.game.areas.ForestGameArea;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.components.maingame.MainGameActions;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
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
import com.csse3200.game.ui.terminal.Terminal;
import com.csse3200.game.ui.terminal.TerminalDisplay;
import com.csse3200.game.components.gamearea.PerformanceDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * The game screen containing the main game.
 *
 * <p>Details on libGDX screens: https://happycoding.io/tutorials/libgdx/game-screens
 */
public class MainGameScreen extends ScreenAdapter {
  private static final Logger logger = LoggerFactory.getLogger(MainGameScreen.class);
  private static final String[] mainGameTextures = {"images/heart.png","images/PauseOverlay/TitleBG.png","images/PauseOverlay/Button.png"};
  private static final Vector2 CAMERA_POSITION = new Vector2(7.5f, 7.5f);
  private final Deque<Overlay> enabledOverlays = new LinkedList<>();
  private boolean isPaused = false;
  private boolean resting = false;
  private final GdxGame game;
  private final Renderer renderer;
  private final PhysicsEngine physicsEngine;
  private final ForestGameArea gameArea;
  private final HashMap<OverlayType, Boolean> activeOverlayTypes = Overlay.getNewActiveOverlayList();

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

    ServiceLocator.registerEventService(new EventService());

    renderer = RenderFactory.createRenderer();
    renderer.getCamera().getEntity().setPosition(CAMERA_POSITION);
    renderer.getDebug().renderPhysicsWorld(physicsEngine.getWorld());

    loadAssets();
    createUI();

    ServiceLocator.getEventService().globalEventHandler.addListener("addOverlay",this::addOverlay);
    ServiceLocator.getEventService().globalEventHandler.addListener("removeOverlay",this::removeOverlay);
    logger.debug("Initialising main game screen entities");
    TerrainFactory terrainFactory = new TerrainFactory(renderer.getCamera());
        this.gameArea = new ForestGameArea(terrainFactory, game);
    gameArea.create();
  }

  @Override
  public void render(float delta) {
    if (!isPaused){
      physicsEngine.update();
      ServiceLocator.getEntityService().update();
      renderer.render();
    }
  }

  @Override
  public void resize(int width, int height) {
    renderer.resize(width, height);
    logger.trace("Resized renderer: ({} x {})", width, height);
  }

  /**
   * pauses all the entities, stops ONLY the screen from rendering,
   * stops the physics engine from making any calculations, and stops
   * the music of this screen.
   */
  @Override
  public void pause() {
    isPaused = true;
    gameArea.pauseMusic();
    logger.info("Game paused");
  }

  /**
   * resumes entities, rendering, physics and music, resets player velocity.
   * Note: if the game is NOT already paused, entities, rendering and physics
   *       remain unchanged, however the player velocity will still be reset.
   */
  @Override
  public void resume() {
    isPaused = false;
    ServiceLocator.getEventService().globalEventHandler.trigger("resetVelocity");
    if (!resting) {
      gameArea.playMusic();
    }
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
    ServiceLocator.getEventService().dispose();

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
        .addComponent(new MainGameActions(this.game))
        .addComponent(new Terminal())
        .addComponent(inputComponent)
        .addComponent(new TerminalDisplay());

    ServiceLocator.getEntityService().register(ui);
  }

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

  public void rest() {
    logger.info("Screen is resting");
    resting = true;
    gameArea.pauseMusic();
    ServiceLocator.getEntityService().restWholeScreen();
  }

  public void wake() {
    logger.info("Screen is Awake");
    resting = false;
    ServiceLocator.getEventService().globalEventHandler.trigger("resetVelocity");
    gameArea.playMusic();
    ServiceLocator.getEntityService().wakeWholeScreen();
  }
}
