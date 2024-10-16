package com.csse3200.game.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.combat.CombatArea;
import com.csse3200.game.areas.terrain.CombatTerrainFactory;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.combat.*;
import com.csse3200.game.components.inventory.CombatInventoryDisplay;
import com.csse3200.game.components.inventory.InventoryComponent;
import com.csse3200.game.components.inventory.PlayerInventoryDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.input.InputService;
import com.csse3200.game.inventory.Inventory;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.services.*;
import com.csse3200.game.ui.terminal.Terminal;
import com.csse3200.game.ui.terminal.TerminalDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The game screen containing the combat feature.
 *
 * <p>Details on libGDX screens: https://happycoding.io/tutorials/libgdx/game-screens
 */
public class CombatScreen extends ResizableScreen {
  private static final Logger logger = LoggerFactory.getLogger(CombatScreen.class);
  private static final String[] combatTextures = {
          "images/heart.png","images/PauseOverlay/TitleBG.png","images/PauseOverlay/Button2.png", "images/grass_3.png",
          "images/combat_background_one.png", "images/hunger_bar.png",
          "images/dog.png", "images/croc.png", "images/bird.png", "images/health_bar_x1.png", "images/xp_bar.png",
          "images/statuses/bleeding_stat.png", "images/statuses/confused_stat.png",
          "images/statuses/poisoned_stat.png", "images/statuses/shocked_stat.png"

  };
  private boolean isPaused = false;
  private final GdxGame game;
  private final PhysicsEngine physicsEngine;
  private final Screen oldScreen;
  private final ServiceContainer oldScreenServices;
  private final Entity player;
  private final Entity enemy;
  private CombatStatsComponent playerCombatStats;
  private CombatStatsComponent enemyCombatStats;
  private final CombatArea combatArea;

  public CombatScreen(GdxGame game, Screen screen, ServiceContainer container, Entity player, Entity enemy) {
    super();
    
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
    renderer.getDebug().renderPhysicsWorld(physicsEngine.getWorld());

    // Load the DialogueBoxService Into Stage
    Stage stage = ServiceLocator.getRenderService().getStage();
    ServiceLocator.registerDialogueBoxService(new DialogueBoxService(stage));

    loadAssets();

    logger.debug("Initialising main game dup screen entities");
    CombatTerrainFactory combatTerrainFactory = new CombatTerrainFactory(renderer.getCamera()); // create new combat terrain factory
    this.combatArea = new CombatArea(player, enemy, game, combatTerrainFactory); // initialise game area, with entities
    combatArea.create();

    createUI();
  }

  @Override
  public void render(float delta) {
    if (!isPaused){
      physicsEngine.update();
      super.render(delta);
      checkEnemyDeath(); // Checking if enemy died
    }
  }

  @Override
  public void resize(int width, int height) {
    super.resize(width, height);
    combatArea.spawnTerrain();
  }

  /** Pause the game, eventually will need to pause music
   */
  @Override
  public void pause() {
    isPaused = true;
    logger.debug("Game paused");
  }

  /** Resume the game, unpause music, when implemented
   */
  @Override
  public void resume() {
    isPaused = false;
    logger.debug("Game resumed");
  }

  @Override
  public void dispose() {
    logger.debug("Disposing main game screen");

    unloadAssets();
    ServiceLocator.getResourceService().dispose();
    
    super.dispose();
  }

  private void loadAssets() {
    logger.debug("Loading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.loadTextures(combatTextures);
    ServiceLocator.getResourceService().loadAll();
  }

  private void unloadAssets() {
    logger.debug("Unloading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.unloadAssets(combatTextures);
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

    // Initialise combat manager with instances of player and enemy to be passed into combat actions
    CombatManager manager = new CombatManager(player, enemy);
    Inventory playerInv = player.getComponent(InventoryComponent.class).getInventory();
    int numCols = player.getComponent(PlayerInventoryDisplay.class).getNumCols();

    Entity ui = new Entity();
    ui.addComponent(new InputDecorator(stage, 10))



        .addComponent(new CombatInventoryDisplay(playerInv, numCols, 0,game))

        .addComponent(manager)
        .addComponent(new CombatActions(this.game, manager, oldScreen, oldScreenServices))
        .addComponent(new CombatStatsDisplay(playerCombatStats, enemyCombatStats))
        .addComponent(new Terminal())
        .addComponent(inputComponent)
        .addComponent(playerCombatStats)
        .addComponent(enemyCombatStats)
        .addComponent(new TerminalDisplay())
        .addComponent(new CombatButtonDisplay(oldScreen, oldScreenServices, combatArea));

    ServiceLocator.getEntityService().register(ui);
  }
  /**
   * Checks if the enemy has died and transitions to the EnemyTransitionCutSceneScreen if true.
   */
  private void checkEnemyDeath() {
    if (enemyCombatStats.getHealth() <= 0) {
      logger.debug("Enemy has been defeated, transitioning to EnemyTransitionCutSceneScreen screen.");
      game.setScreen(new EnemyTransitionCutSceneScreen(game, this.enemy)); // Transition to EnemyTransitionCutSceneScreen
    }
  }

}
