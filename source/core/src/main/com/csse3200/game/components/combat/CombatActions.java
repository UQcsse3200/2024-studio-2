package com.csse3200.game.components.combat;

import com.badlogic.gdx.Screen;
import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.ForestGameArea;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityConverter;
import com.csse3200.game.services.ServiceContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class listens to events relevant to the combat screen and does something when one of the
 * events is triggered.
 */
public class CombatActions extends Component {
  private static final Logger logger = LoggerFactory.getLogger(CombatActions.class);
  private GdxGame game;
  private final CombatManager manager;

  public CombatActions(GdxGame game, CombatManager manager) {
    this.game = game;
    this.manager = manager;
  }

  @Override
  public void create() {
    entity.getEvents().addListener("returnToMainGame", this::onReturnToMainGame);
    entity.getEvents().addListener("combatWin", this::onCombatWin);
    entity.getEvents().addListener("combatLose", this::onCombatLoss);
    entity.getEvents().addListener("Attack", this::onAttack);
    entity.getEvents().addListener("Guard", this::onGuard);
    entity.getEvents().addListener("Sleep", this::onSleep);
    entity.getEvents().addListener("Items", this::onItems);
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
    EntityConverter.convertToFriendly(manager.getEnemy(), manager.getPlayer(), ForestGameArea.enemies);
    game.setScreen(GdxGame.ScreenType.GAME_OVER_WIN);
    entity.getEvents().trigger("onCombatWin", manager.getPlayerStats());
  }

  /**
   * Swaps from combat screen to Main Game screen in the event of a lost combat sequence.
   */
  private void onCombatLoss(Screen screen, ServiceContainer container) {
    logger.info("Returning to main game screen after combat loss.");
    // Set current screen to original MainGameScreen
    game.setScreen(GdxGame.ScreenType.GAME_OVER_LOSE);
  }
  private void onAttack(Screen screen, ServiceContainer container) {
    logger.info("Attack selected.");
    manager.onAttackSelected();
    entity.getEvents().trigger("onAttack", manager.getPlayerStats(), manager.getEnemyStats());
  }
  private void onGuard(Screen screen, ServiceContainer container) {
    logger.info("before Guard");
    // Perform Guard logic here, like increasing health

  }
  private void onSleep(Screen screen, ServiceContainer container) {
    logger.info("before Sleep");
    entity.getEvents().trigger("onSleep", manager.getPlayerStats(), manager.getEnemyStats());
    // Perform counter logic here.
  }
  private void onItems(Screen screen, ServiceContainer container) {
    logger.info("before Items");
    // Perform Guard logic here, like increasing health

  }
  /**
   * Called when the screen is disposed to free resources.
   */
  @Override
  public void dispose() {
    // Dispose of the stage to free up resources
    //stage.dispose(); // commented out because stage was returning null so could not be disposed of
  }
}
