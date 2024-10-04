package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.maingame.TimeDisplay;
import com.csse3200.game.components.player.KeyboardPlayerInputComponent;
import com.csse3200.game.areas.GameArea;
import com.csse3200.game.areas.MapHandler;
import com.csse3200.game.components.maingame.MainGameActions;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.lighting.DayNightCycle;
import com.csse3200.game.services.DialogueBoxService;
import com.csse3200.game.entities.factories.RenderFactory;
import com.csse3200.game.gamestate.GameState;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.input.InputService;
import com.csse3200.game.lighting.LightingEngine;
import com.csse3200.game.lighting.LightingService;
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
import com.badlogic.gdx.math.Rectangle;
import java.util.ArrayList;
import java.util.List;

import com.csse3200.game.areas.MiniMapDisplay;

/**
 * The game screen containing the main game.
 *
 * <p>
 * Details on libGDX screens:
 * https://happycoding.io/tutorials/libgdx/game-screens
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
          GameState.player.selectedAnimalPath, "images/player_icon_forest.png", "images/vignette.png",
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
  private final LightingEngine lightingEngine;
  private final DayNightCycle dayNightCycle;

  /**
   * The game area containing the main game.
   */
  private GameArea gameArea;

  // Map tab variables
  private Texture mapTexture;
  private boolean isMapVisible = false;
  private SpriteBatch batch;
  private Texture playerLocationTexture;
  private Texture landmarkIconTexture;
  private List<Vector2> landmarks;
  private Texture xButtonTexture;
  private Rectangle xButtonBounds;
  /**
   * Constructs a MainGameScreen instance.
   * 
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

    // register the EntityChatService
    renderer = RenderFactory.createRenderer();
    renderer.getCamera().getEntity().setPosition(CAMERA_POSITION);
    renderer.getDebug().renderPhysicsWorld(physicsEngine.getWorld());

    // Load the map texture and initialise
    mapTexture = new Texture(Gdx.files.internal("map/MAP.png"));
    playerLocationTexture = new Texture(Gdx.files.internal("map/Lion_Icon.png"));
    landmarkIconTexture = new Texture(Gdx.files.internal("map/landmark_icon.png"));
    batch = new SpriteBatch();
    preloadLandmarks();
    // Load the 'X' button texture and set up the bounds
    xButtonTexture = new Texture(Gdx.files.internal("map/x_button.jpg"));
    xButtonBounds = new Rectangle(Gdx.graphics.getWidth() - 50, Gdx.graphics.getHeight() - 50, 40, 40);

    lightingEngine = new LightingEngine(physicsEngine.getWorld(),
            renderer.getCamera().getCamera());

    lightingEngine.getRayHandler().setAmbientLight(new Color(0.5f, 0.45f, 0.3f, 0.6f));

    ServiceLocator.getRenderService().register(lightingEngine);

    ServiceLocator.registerLightingService(new LightingService(lightingEngine));

    dayNightCycle = new DayNightCycle(lightingEngine.getRayHandler());

    loadAssets();
    this.gameArea = MapHandler.createNewMap(MapHandler.MapType.FOREST, renderer, this.game);
    createUI();
    logger.debug("Initialising main game screen entities");


    Stage stage = ServiceLocator.getRenderService().getStage();
    ServiceLocator.registerDialogueBoxService(new DialogueBoxService(stage));
  }

    /**
     * Preloads important landmarks with their coordinates.
     */
  private void preloadLandmarks() {
    landmarks = new ArrayList<>();
    landmarks.add(new Vector2(500, 800));  // Example
    // ADD MORE
  }

  /**
   * Sets the beginning map of the game.
   * 
   * @param mapType The map type to set the map to.
   */
  public void setMap(MapHandler.MapType mapType) {
    this.gameArea = MapHandler.switchMapTo(mapType, renderer, game, true);
  }

  /**
   * Renders the game screen and updates the physics engine, game entities, and renderer.
   * @param delta The time elapsed since the last render call.
   */
  @Override
  public void render(float delta) {
      // Check if 'M' key is pressed to toggle map visibility
      if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
          isMapVisible = !isMapVisible;

          // Pause the game logic without triggering the full game pause when the map is opened
          if (isMapVisible) {
              isPaused = true;  // Pause game logic but don't call GdxGame.pause()
          } else {
              isPaused = false; // Resume game logic
          }
      }

      // Check if the 'X' button is clicked while the map is visible
      if (isMapVisible && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
          float mouseX = Gdx.input.getX();
          float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();

          // If the X button is clicked, close the map and resume the game
          if (xButtonBounds.contains(mouseX, mouseY)) {
              isMapVisible = false;  // Close the map
              isPaused = false;      // Resume game logic
          }
      }

      if (!isPaused){
          physicsEngine.update();
          ServiceLocator.getEntityService().update();
          dayNightCycle.update();
          renderer.render();
      }
      // If map is visible, draw the map
      if (isMapVisible) {
          drawMap();
      }
  }

  /**
   * Draws the map on the screen when visible.
   */
  private void drawMap() {
      batch.begin();
      // Draw the map
      batch.draw(mapTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

      // Get player position
      Vector2 playerWorldPosition = gameArea.getPlayer().getPosition();

      // Convert the player's game position to the map coordinates
      Vector2 playerMapPosition = convertGamePositionToMap(playerWorldPosition);

      // Draw the player location icon
      batch.draw(playerLocationTexture, playerMapPosition.x, playerMapPosition.y, 32, 32);

      // Draw all landmarks on the map, ADD LANDMARKS TO LIST !!!
      for (Vector2 landmarkPosition : landmarks) {
          Vector2 mapPos = convertGamePositionToMap(landmarkPosition);
          batch.draw(landmarkIconTexture, mapPos.x, mapPos.y, 32, 32);
      }

      batch.draw(xButtonTexture, Gdx.graphics.getWidth() - 50, Gdx.graphics.getHeight() - 50, 40, 40);
      batch.end();
  }

    /**
     * Converts the player's position in the game world to the map's coordinate system.
     */
    private Vector2 convertGamePositionToMap(Vector2 gamePosition) {
        // ASSUMING GAME WORLD IS 5000x5000 AND MAP IS 1024*1024
        float scaleX = Gdx.graphics.getWidth() / 5000f;
        float scaleY = Gdx.graphics.getHeight() / 5000f;

        // Convert the player's position in the game world to the corresponding position on the map
        return new Vector2(gamePosition.x * scaleX, gamePosition.y * scaleY);
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
      KeyboardPlayerInputComponent inputComponent = gameArea.getPlayer().getComponent(KeyboardPlayerInputComponent.class);
      inputComponent.resetVelocity();
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
      batch.dispose();
      mapTexture.dispose();
      playerLocationTexture.dispose();
      landmarkIconTexture.dispose();
      xButtonTexture.dispose();
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
              .addComponent(new TerminalDisplay())
              .addComponent(new MiniMapDisplay(gameArea))
              .addComponent(new TimeDisplay());
      
      ServiceLocator.getEntityService().register(ui);
  }
  
  /**
   * Puts the screen into a resting state, pausing music and resting all entities.
   */
  public void rest() {
      super.rest();
      gameArea.pauseMusic();
  }
  
  /**
   * Wakes the screen from a resting state.
   */
  public void wake() {
      super.wake();
      KeyboardPlayerInputComponent inputComponent = gameArea.getPlayer().getComponent(KeyboardPlayerInputComponent.class);
      inputComponent.resetVelocity();
      gameArea.playMusic();
  }

  public GameArea getGameArea() {
    return gameArea;
  }
}
