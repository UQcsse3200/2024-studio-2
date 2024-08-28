package com.csse3200.game.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
import com.csse3200.game.Overlays.Overlay;
import com.csse3200.game.Overlays.PauseOverlay;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.combat.CombatEnvironmentDisplay;
import com.csse3200.game.components.combat.CombatExitDisplay;
import com.csse3200.game.components.combat.CombatStatsDisplay;
import com.csse3200.game.components.combat.CombatActions;
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
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceContainer;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.eventservice.EventService;
import com.csse3200.game.ui.terminal.Terminal;
import com.csse3200.game.ui.terminal.TerminalDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Deque;
import java.util.LinkedList;

/**
 * The game screen containing the combat feature.
 *
 * <p>Details on libGDX screens: https://happycoding.io/tutorials/libgdx/game-screens
 */
public class CombatScreen extends ScreenAdapter {
  private static final Logger logger = LoggerFactory.getLogger(CombatScreen.class);
  private static final String[] mainGameTextures = {
          "images/heart.png","images/PauseOverlay/TitleBG.png","images/PauseOverlay/Button.png", "images/grass_3.png"
  };
  private static final Vector2 CAMERA_POSITION = new Vector2(7.5f, 7.5f);
  private boolean isPaused = false;
  private final GdxGame game;
  private final Renderer renderer;
  private final PhysicsEngine physicsEngine;
  private final Screen oldScreen;
  private final ServiceContainer oldScreenServices;
  private final Entity player;
  private final Entity enemy;
  private CombatStatsComponent playerCombatStats;
  private CombatStatsComponent enemyCombatStats;
  private final Deque<Overlay> enabledOverlays = new LinkedList<>();

  public CombatScreen(GdxGame game, Screen screen, ServiceContainer container, Entity player, Entity enemy) {
    this.game = game;
    this.oldScreen = screen;
    this.oldScreenServices = container;
    this.player = player;
    this.enemy = enemy;

    this.playerCombatStats = player.getComponent(CombatStatsComponent.class);
    this.enemyCombatStats = enemy.getComponent(CombatStatsComponent.class);

    logger.debug("Initialising combat screen services");
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
    logger.debug("Initialising main game dup screen entities");
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

  @Override
  public void pause() {
    isPaused = true;
    //gameArea.pauseMusic(); // No GameArea to contain music is initialised as of yet.
    logger.info("Game paused");
  }

  @Override
  public void resume() {
    isPaused = false;
    //gameArea.playMusic(); // No GameArea to contain music is initialised as of yet.
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
        .addComponent(new CombatActions(this.game, this.enemy))
        .addComponent(new CombatEnvironmentDisplay())
        .addComponent(new CombatExitDisplay(oldScreen, oldScreenServices))
        .addComponent(new CombatStatsDisplay(playerCombatStats, enemyCombatStats))
        .addComponent(new Terminal())
        .addComponent(inputComponent)
        .addComponent(new TerminalDisplay());

    ServiceLocator.getEntityService().register(ui);
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
    //gameArea.pauseMusic();
    ServiceLocator.getEntityService().restWholeScreen();
  }

  public void wake() {
    logger.info("Screen is Awake");
    //gameArea.playMusic();
    ServiceLocator.getEntityService().wakeWholeScreen();
  }
}
