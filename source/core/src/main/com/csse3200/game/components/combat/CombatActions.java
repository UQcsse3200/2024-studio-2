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
public class CombatActions extends Component {
  private static final Logger logger = LoggerFactory.getLogger(CombatActions.class);
  private GdxGame game;
  private Entity enemy; // Each combat can only have one enemy.
  private Stage stage;
  private CombatButtonDisplay Display;
  private Screen screen;
  private ServiceContainer container;


  public CombatActions(GdxGame game, Entity enemy) {
    this.game = game;
    this.enemy = enemy;
  }

  /**
   * Called when the screen is resized.
   *
   * @param width  The new width of the screen.
   * @param height The new height of the screen.
   */
  @Override
  public void create() {
    ServiceLocator.getEventService().getGlobalEventHandler().addListener("exit", this::onExit);
    entity.getEvents().addListener("returnToMainGame", this::onReturnToMainGame);
    entity.getEvents().addListener("combatWin", this::onCombatWin);
    entity.getEvents().addListener("combatLose", this::onCombatLoss);
    entity.getEvents().addListener("Attack", this::onAttack);
    entity.getEvents().addListener("Guard", this::onGuard);
    entity.getEvents().addListener("Counter", this::onCounter);
    //Display.create();
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

  }
  private void onGuard(Screen screen, ServiceContainer container) {
    logger.info("onGuard before");
    // Perform Guard logic here, like increasing health

  }
  private void onCounter(Screen screen, ServiceContainer container) {
    logger.info("before Counter");
    // Perform counter logic here.
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
