package com.csse3200.game.components.combat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.combatButtons.CombatButtonActions;
import com.csse3200.game.components.combatButtons.CombatButtonDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.components.Component;
import com.csse3200.game.services.ServiceContainer;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.PopUpDialogBox.PopUpHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class listens to events relevant to the Main Game Screen and does something when one of the
 * events is triggered.
 */
public class
CombatActions extends Component {
  private static final Logger logger = LoggerFactory.getLogger(CombatActions.class);
  private final GdxGame game;
  private Entity enemy; // Each combat can only have one enemy.

  private Stage stage;
  private CombatButtonActions actions;
  private CombatButtonDisplay display;



  //private TextButton attackButton;
  //private TextButton boostButton;

  public CombatActions(GdxGame game, Entity enemy) {
    this.game = game;
    this.enemy = enemy;
  }
  public CombatActions(GdxGame game) {
    this.game = game;
      stage = new Stage();

    Gdx.input.setInputProcessor(stage);  // Set the stage as the input processor to handle user input

    // Load the skin for UI elements from the specified JSON file
    Skin skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));

    // Initialize the dialog helper with the skin and stage
    PopUpHelper dialogHelper = new PopUpHelper(skin, stage);

    // Set up the display component for animal selection, passing in the stage and skin
    display = new CombatButtonDisplay(stage, skin);

    // Set up actions for handling UI interactions, passing the display, dialog helper, and game instance
    actions = new CombatButtonActions(display, dialogHelper, game);
  }
  @Override
  public void render(float delta) {
    // Clear the screen to prevent overlapping frames
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    // Update and draw the stage (and all its actors) based on the current delta time
    stage.act(Gdx.graphics.getDeltaTime());
    stage.draw();
  }

  /**
   * Called when the screen is resized.
   *
   * @param width  The new width of the screen.
   * @param height The new height of the screen.
   */
  @Override
  public void resize(int width, int height) {
    // Update the stage's viewport to the new screen size, centering the stage
    stage.getViewport().update(width, height, true);
  }

  @Override
  public void create() {
    ServiceLocator.getEventService().getGlobalEventHandler().addListener("exit", this::onExit);
    entity.getEvents().addListener("returnToMainGame", this::onReturnToMainGame);
    entity.getEvents().addListener("combatWin", this::onCombatWin);
    entity.getEvents().addListener("combatLose", this::onCombatLoss);
    entity.getEvents().addListener("Attack", this::onAttack);
    entity.getEvents().addListener("Boast", this::onBoast);
  }

  /**
   * Swaps to the Main Menu screen.
   */
  private void onExit() {
    logger.info("Exiting main game screen");
    game.setScreen(GdxGame.ScreenType.MAIN_MENU);
  }

  private void onReturnToMainGame(Screen screen, ServiceContainer container) {
    logger.info("Returning to main game screen");
    // change to new GDXgame function
    game.setOldScreen(screen, container);
  }

  /**
   * Swaps from combat screen to Main Game screen in the event of a won combat sequence.
   * 'Kills' enemy entity on return to combat screen.
   */
  private void onCombatWin(Screen screen, ServiceContainer container) {
    logger.info("Returning to main game screen after combat win.");
    // Kill enemy.
    //this.enemy.dispose();
    //this.enemy.update();
    //container.getEntityService().unregister(enemy);
    //container.getEntityService().update();
    // Set current screen to original MainGameScreen
//    game.setOldScreen(screen, container);
    game.setScreen(GdxGame.ScreenType.GAME_OVER_WIN);
  }

  /**
   * Swaps from combat screen to Main Game screen in the event of a lost combat sequence.
   */
  private void onCombatLoss(Screen screen, ServiceContainer container) {
    logger.info("Returning to main game screen after combat loss.");
    // Set current screen to original MainGameScreen
//    game.setOldScreen(screen, container);
    game.setScreen(GdxGame.ScreenType.GAME_OVER_LOSE);
  }
  private void onAttack(Screen screen, ServiceContainer container) {
    logger.info("onAttack before");
    // Perform attack logic here, like decreasing health
    game.setScreen(GdxGame.ScreenType.GAME_OVER_WIN);

    logger.info("Attack button after");
  }
  private void onBoast(Screen screen, ServiceContainer container) {
    logger.info("onBoast before");
    // Perform boost logic here, like increasing health
    // maybe like entity.getComponent(CombatStatsComponent.class).increaseHealth(10);
    //game.setScreen(GdxGame.ScreenType.GAME_OVER_WIN);

    logger.info("Boost button after");
  }
  /**
   * Called when the screen is disposed to free resources.
   */
  @Override
  public void dispose() {
    // Dispose of the stage to free up resources
    stage.dispose();
  }
}
