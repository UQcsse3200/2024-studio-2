package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.input.InputService;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages the cutscene for Enemy and Boss NPCs displayed before transitioning to the combat screen.
 * Handles initialization, rendering, and disposal of cutscene elements.
 */
public class PreCombatCutsceneScreen extends ResizableScreen {
  private static final float CUTSCENE_DURATION = 3.0f; // Cutscene lasts for 3 seconds
  private int labelBuffer = 0;
  private int imageBuffer = 0;
  private float timeElapsed = 0;
  private boolean transition;

  private static final Logger logger = LoggerFactory.getLogger(PreCombatCutsceneScreen.class);
  private static final Vector2 CAMERA_POSITION = new Vector2(7.5f, 7.5f);
  private boolean isPaused = false;
  private final GdxGame game;
  private final PhysicsEngine physicsEngine;
  private final Screen oldScreen;
  private final ServiceContainer oldScreenServices;
  private final Entity player;
  private final Entity enemy;

  /**
   * Creates a new cutscene screen.
   *
   * @param game the game instance
   * @param screen the previous screen
   * @param container services from the previous screen
   * @param player the player entity
   * @param enemy the enemy entity
   */
  public PreCombatCutsceneScreen(GdxGame game, Screen screen, ServiceContainer container, Entity player, Entity enemy) {
    super();

    this.game = game;
    this.oldScreen = screen;
    this.oldScreenServices = container;
    this.player = player;
    this.enemy = enemy;
    setLabelBuffer(enemy.getEnemyType());

    logger.debug("Initializing boss cutscene screen services");
    ServiceLocator.registerTimeSource(new GameTime());

    PhysicsService physicsService = new PhysicsService();
    ServiceLocator.registerPhysicsService(physicsService);
    physicsEngine = physicsService.getPhysics();

    ServiceLocator.registerInputService(new InputService());
    ServiceLocator.registerResourceService(new ResourceService());

    renderer.getCamera().getEntity().setPosition(CAMERA_POSITION);
    renderer.getDebug().renderPhysicsWorld(physicsEngine.getWorld());

    this.transition = false;

    // Load the music asset
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.loadMusic(new String[]{"sounds/precombat.mp3"});

    // Ensure the asset is loaded before playing
    resourceService.loadAll(); // Make sure all assets are loaded before using them
    AudioManager.playMusic("sounds/precombat.mp3", true);  // Play the pre-combat music in a loop

    createUI();

    logger.debug("Initialising main game dup screen entities");
  }

  /**
   * Renders the cutscene screen. Updates the physics engine and entity service,
   * and handles transitioning to the combat screen once the cutscene duration has passed.
   *
   * @param delta The time elapsed since the last frame, in seconds.
   */
  @Override
  public void render(float delta) {
    if (!isPaused) {
      physicsEngine.update();
      ServiceLocator.getEntityService().update();
      renderer.render();

      timeElapsed += delta;
      if (timeElapsed >= CUTSCENE_DURATION && !transition) {
        transition = true;
        logger.info("Cutscene finished, transitioning to combat screen");

        // Stop the cutscene music before transitioning
        AudioManager.stopMusic();

        game.setScreen(new CombatScreen(game, oldScreen, oldScreenServices, player, enemy));
      }
    }
  }

  /**
   * Pauses the cutscene screen. Disables updates and rendering when the game is paused.
   */
  @Override
  public void pause() {
    isPaused = true;
    logger.info("Game paused");
  }

  /**
   * Resumes the cutscene screen. Re-enables updates and rendering when the game is resumed.
   */
  @Override
  public void resume() {
    isPaused = false;
    logger.info("Game resumed");
  }

  /**
   * Disposes of the resources used by the cutscene screen, including renderer and services.
   * This method is called when the screen is no longer needed.
   */
  @Override
  public void dispose() {
    logger.debug("Disposing cutscene screen");

    // Stop the music when disposing of the screen
    AudioManager.stopMusic();

    ServiceLocator.getResourceService().dispose();

    super.dispose();
  }

  /**
   * Sets up the cutscene UI components, including background, black bars at the top and bottom of the screen,
   * and initial configurations. This method ensures that the static elements of the UI are in place before
   * any interactive elements are added or animations are applied.
   */
  private void setupUIComponents() {
    logger.debug("Setting up cutscene UI components");
    Stage stage = ServiceLocator.getRenderService().getStage();

    // Load background texture and create Image actor
    Texture backgroundTexture;
    switch (enemy.getEnemyType()) {
      case MONKEY, CHICKEN, BEAR, JOEY, KANGAROO -> backgroundTexture = new Texture("images/transitionBg.jpg");
      case FROG, EEL, OCTOPUS, BIGSAWFISH, WATER_BOSS -> backgroundTexture = new Texture("images/Water_Transition.jpg");
      case BEE, PIGEON, MACAW, AIR_BOSS -> backgroundTexture = new Texture("images/Air_Transition.jpg");
      default -> backgroundTexture = new Texture("images/transitionBg.jpg"); // Default background
    }
    Image backgroundImage = new Image(backgroundTexture);

    // Set background image to cover the whole screen
    backgroundImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    // Add actors to stage
    stage.addActor(backgroundImage);
  }

  /**
   * Configures and adds the enemy image, enemy name label, and stats labels to the stage.
   * This method handles the creation of dynamic UI elements based on the enemy type and its combat stats,
   * including setting up the layout for these elements in a table.
   */
  private void configureAndAddUIElements() {
    Stage stage = ServiceLocator.getRenderService().getStage();
    BitmapFont defaultFont = new BitmapFont();
    defaultFont.getData().setScale(2.0f);

    // Create label style with the font
    Label.LabelStyle labelStyle = new Label.LabelStyle();
    labelStyle.font = defaultFont;
    labelStyle.fontColor = Color.BLACK;

    Label enemyNameLabel;
    Texture enemyImageTexture;

    // Select enemy image and name based on enemy type
    switch (enemy.getEnemyType()) {
      case CHICKEN:
        enemyImageTexture = new Texture("images/chicken_idle.png");
        enemyNameLabel = new Label("Chicken", labelStyle);
        break;
      case FROG:
        enemyImageTexture = new Texture("images/frog_idle.png");
        enemyNameLabel = new Label("Frog", labelStyle);
        break;
      case MONKEY:
        enemyImageTexture = new Texture("images/monkey_idle.png");
        enemyNameLabel = new Label("Monkey", labelStyle);
        break;
      case BEAR:
        enemyImageTexture = new Texture("images/bear_idle.png");
        enemyNameLabel = new Label("Bear", labelStyle);
        break;
      case BEE:
        enemyImageTexture = new Texture("images/bee_idle.png");
        enemyNameLabel = new Label("Bee", labelStyle);
        break;
      case PIGEON:
        enemyImageTexture = new Texture("images/pigeon_idle.png");
        enemyNameLabel = new Label("Pigeon", labelStyle);
        break;
      case EEL:
        enemyImageTexture = new Texture("images/eel_idle.png");
        enemyNameLabel = new Label("Eel", labelStyle);
        break;
      case OCTOPUS:
        enemyImageTexture = new Texture("images/octopus_idle.png");
        enemyNameLabel = new Label ("Octopus", labelStyle);
        break;
      case BIGSAWFISH:
        enemyImageTexture = new Texture("images/bigsawfish_idle.png");
        enemyNameLabel = new Label("Bigsawfish", labelStyle);
        break;
      case MACAW:
        enemyImageTexture = new Texture("images/macaw_idle.png");
        enemyNameLabel = new Label("Macaw", labelStyle);
        break;
      case JOEY:
        enemyImageTexture = new Texture("images/joey_idle.png");
        enemyNameLabel = new Label("Joey", labelStyle);
        break;
      case KANGAROO:
        enemyImageTexture = new Texture("images/final_boss_kangaroo_idle.png");
        enemyNameLabel = new Label("Kanga", labelStyle);
        break;
      case WATER_BOSS:
        enemyImageTexture = new Texture("images/water_boss_idle.png");
        enemyNameLabel = new Label("Leviathan", labelStyle);
        break;
      case AIR_BOSS:
        enemyImageTexture = new Texture("images/air_boss_idle.png");
        enemyNameLabel = new Label("Griffin", labelStyle);
        break;
      default:
        enemyImageTexture = new Texture("");
        enemyNameLabel = new Label("", labelStyle);
        break;
    }

    Image enemyImage = new Image(enemyImageTexture);

    // Create labels for stats
    CombatStatsComponent stats = enemy.getComponent(CombatStatsComponent.class);
    Label healthLabel = new Label("Health: " + stats.getHealth() + "/" + stats.getMaxHealth(), labelStyle);
    Label strengthLabel = new Label("Strength: " + stats.getStrength(), labelStyle);
    Label defenseLabel = new Label("Defense: " + stats.getDefense(), labelStyle);
    Label speedLabel = new Label("Speed: " + stats.getSpeed(), labelStyle);

    // Table layout for positioning
    Table table = new Table();
    table.setFillParent(true);
    table.center();

    // Add elements to the table with spacing
    table.add(enemyImage).width(500).height(400);
    table.row();
    table.add(enemyNameLabel).padBottom(5);
    table.row();
    table.add(healthLabel).padBottom(5);
    table.row();
    table.add(strengthLabel).padBottom(5);
    table.row();
    table.add(defenseLabel).padBottom(5);
    table.row();
    table.add(speedLabel).padBottom(5);

    // Add table to the stage
    stage.addActor(table);

    // Set names for elements to easily find them later for animations
    enemyImage.setName("enemyImage");
    enemyNameLabel.setName("enemyNameLabel");
  }

  /**
   * Adds animations to the cutscene UI components, including sliding and fading effects.
   * This method animates the entry of the enemy image and name label into view, and applies a flash effect
   * to the table containing the stats labels to enhance the visual appeal of the cutscene.
   */
  private void addUIAnimations() {
    logger.debug("Adding animations to cutscene UI components");
    Stage stage = ServiceLocator.getRenderService().getStage();

    // Centered positions
    float centerX = (Gdx.graphics.getWidth() - 500) / 2f;
    float centerY = (Gdx.graphics.getHeight() - 150) / 2f;

    // Access UI elements by their names
    Image enemyImage = (Image) stage.getRoot().findActor("enemyImage");
    Label enemyNameLabel = (Label) stage.getRoot().findActor("enemyNameLabel");

    // Initial positions for sliding animations
    enemyImage.setPosition(0, centerY); // Start from off-screen left
    enemyNameLabel.setPosition(Gdx.graphics.getWidth(), centerY); // Start from off-screen right

    // Animate enemy image (slide-in effect)
    enemyImage.addAction(
            Actions.sequence(
                    Actions.moveTo(centerX + this.imageBuffer, centerY, 2f, Interpolation.pow5Out)
            )
    );

    // Animate enemy name label (slide-in effect)
    enemyNameLabel.addAction(
            Actions.sequence(
                    Actions.moveTo(centerX + this.labelBuffer, centerY - 10, 2f, Interpolation.pow5Out)
            )
    );

    // Add fade-in and flash effect to the enemy label
    enemyNameLabel.addAction(
            Actions.sequence(
                    Actions.alpha(0f),
                    Actions.repeat(5,
                            Actions.sequence(
                                    Actions.fadeIn(0.2f),
                                    Actions.fadeOut(0.2f)
                            )
                    ),
                    Actions.fadeIn(0.2f) // Finally, keep it visible
            )
    );
  }

  /**
   * Sets up and animates the cutscene UI elements, including the enemy image, name, and stats.
   * This method orchestrates the entire UI setup by calling the methods responsible for
   * setting up UI components, configuring and adding UI elements, and applying animations.
   */
  private void createUI() {
    setupUIComponents();
    configureAndAddUIElements();
    addUIAnimations();
  }

  public void setLabelBuffer(Entity.EnemyType enemy) {
    switch (enemy) {
      case FROG -> this.labelBuffer =  220;
      case BEAR -> {
        this.labelBuffer = 220;
        this.imageBuffer = -55;
      }
      case EEL -> {
        this.labelBuffer = 230;
        this.imageBuffer = -30;
      }
      default -> {
        this.labelBuffer =  200;
        this.imageBuffer = 0;
      }
    }
  }
}