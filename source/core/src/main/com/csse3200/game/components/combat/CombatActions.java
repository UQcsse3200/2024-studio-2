package com.csse3200.game.components.combat;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.services.ServiceContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class listens to events relevant to the combat screen and does something when one of the
 * events is triggered.
 */
public class CombatActions extends Component {
  private static final Logger logger = LoggerFactory.getLogger(CombatActions.class);
  private final GdxGame game;
  private final CombatManager manager;
  private final Screen previousScreen;
  private final ServiceContainer previousServices;
  private Stage stage;

  public CombatActions(GdxGame game, CombatManager manager, Screen previousScreen, ServiceContainer previousServices) {
    this.game = game;
    this.manager = manager;
    this.previousScreen = previousScreen;
    this.previousServices = previousServices;
  }

  @Override
  public void create() {
    entity.getEvents().addListener("combatWin", this::onCombatWin);
    entity.getEvents().addListener("combatLoss", this::onCombatLoss);
    entity.getEvents().addListener("Attack", this::onAttack);
    entity.getEvents().addListener("Guard", this::onGuard);
    entity.getEvents().addListener("Sleep", this::onSleep);
    entity.getEvents().addListener("Items", this::onItems);
  }

  /**
   * Swaps from combat screen to Main Game screen in the event of a won combat sequence.
   * 'Kills' enemy entity on return to combat screen.
   */
  private void onCombatWin() {
    logger.info("Returning to main game screen after combat win.");
    // Reset player's stamina.
    manager.getPlayer().getComponent(CombatStatsComponent.class).setStamina(100);
    game.setOldScreen(previousScreen, previousServices);
    entity.getEvents().trigger("onCombatWin", manager.getPlayerStats());
  }

  /**
   * Swaps from combat screen to Main Game screen in the event of a lost combat sequence.
   */
  private void onCombatLoss() {
    logger.info("Returning to main game screen after combat loss.");
    game.setScreen(GdxGame.ScreenType.GAME_OVER_LOSE);
  }

  private void onAttack(Screen screen, ServiceContainer container) {
      logger.info("Attack selected.");
      manager.onPlayerActionSelected("ATTACK");
      entity.getEvents().trigger("onAttack", manager.getPlayerStats(), manager.getEnemyStats());
  }

  private void onGuard(Screen screen, ServiceContainer container) {
    manager.onPlayerActionSelected("GUARD");
  }

  private void onSleep(Screen screen, ServiceContainer container) {
    manager.onPlayerActionSelected("SLEEP");
    entity.getEvents().trigger("onSleep", manager.getPlayerStats(), manager.getEnemyStats());
  }

  private void onItems(Screen screen, ServiceContainer container) {
    logger.info("Clicked Items");
  }

  /**
   * Called when the screen is disposed to free resources.
   */
  @Override
  public void dispose() {
    // Dispose of the stage to free up resources
  }
}
