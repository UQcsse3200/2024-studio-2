package com.csse3200.game.components.combat;

import com.badlogic.gdx.Screen;
import com.csse3200.game.GdxGame;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.components.Component;
import com.csse3200.game.services.ServiceContainer;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class listens to events relevant to the Main Game Screen and does something when one of the
 * events is triggered.
 */
public class CombatActions extends Component {
  private static final Logger logger = LoggerFactory.getLogger(CombatActions.class);
  private final GdxGame game;
  private final Entity enemy; // Each combat can only have one enemy.

  public CombatActions(GdxGame game, Entity enemy) {
    this.game = game;
    this.enemy = enemy;
  }

  @Override
  public void create() {
    ServiceLocator.getEventService().getGlobalEventHandler().addListener("exit", this::onExit);
    entity.getEvents().addListener("returnToMainGame", this::onReturnToMainGame);
    entity.getEvents().addListener("combatWin", this::onCombatWin);
    entity.getEvents().addListener("combatLose", this::onCombatLoss);
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
}
