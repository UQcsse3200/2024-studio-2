package com.csse3200.game.components.combat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.components.Component;
import com.csse3200.game.screens.LoadingScreen;
import com.csse3200.game.services.ServiceContainer;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

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
  private CombatButtonStagDisplay Display;


  //private TextButton attackButton;
  //private TextButton boostButton;

  public CombatActions(GdxGame game, Entity enemy) {
    this.game = game;
    this.enemy = enemy;
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
    entity.getEvents().addListener("Boost", this::onBoost);
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
    int healthCheck=0;
    if (healthCheck!= 0) {
      logger.info("CombatActions::onAttack, before Attack- health check!=0");
      Display= new CombatButtonStagDisplay( healthCheck, true,true);
      game.setScreen(new LoadingScreen(game));
      logger.info("CombatActions::onAttack, after Attack- health check!=0");
    } else {
      Display= new CombatButtonStagDisplay( healthCheck, false,true);
      logger.info("CombatActions::onAttack, Attack- health check = 0 big L");
      //AttackButton.setVisible(false);// cannot attack no more
    }
    //game.setScreen(GdxGame.ScreenType.GAME_OVER_WIN);
    logger.info("Attack button after");
  }
  private void onBoost(Screen screen, ServiceContainer container) {
    logger.info("onBoost before");
    // Perform boost logic here, like increasing health
    int healthCheck=100;
    if (healthCheck!=100) {
      logger.info("CombatActions::onBoost, Boost- health check!=100");
      Display= new CombatButtonStagDisplay( healthCheck, true,true);
      game.setScreen(new LoadingScreen(game));
    } else {
      logger.info("CombatActions::onBoost, Boost- health check = sigma");
      Display= new CombatButtonStagDisplay( healthCheck, true,false);
      //BoostButton.setVisible(false);// Maximum boost my guy
    }
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
