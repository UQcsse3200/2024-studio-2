package com.csse3200.game.components.combat;

import com.badlogic.gdx.Screen;
import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.ForestGameArea;
import com.csse3200.game.components.CombatStatsComponent;
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
  private final GdxGame game;
  private final CombatManager manager;
  private final Screen previousScreen;
  private final ServiceContainer previousServices;

  public CombatActions(GdxGame game, CombatManager manager, Screen previousScreen, ServiceContainer previousServices) {
    this.game = game;
    this.manager = manager;
    this.previousScreen = previousScreen;
    this.previousServices = previousServices;
  }

  /**
   * Initialises the event listeners.
   */
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
    entity.getEvents().trigger("onCombatWin", manager.getPlayerStats());
    EntityConverter.convertToFriendly(manager.getEnemy(), manager.getPlayer(), ForestGameArea.enemies);
    game.setOldScreen(previousScreen, previousServices);
  }

  /**
   * Swaps from combat screen to Game Over screen upon the event that the player is defeated in battle.
   */
  private void onCombatLoss() {
    logger.info("Returning to main game screen after combat loss.");
    game.setScreen(GdxGame.ScreenType.GAME_OVER_LOSE);
  }

  /**
   * Signalled by clicking attack button, to then signal the ATTACK combat move in the manager.
   */
  private void onAttack(Screen screen, ServiceContainer container) {
      manager.onPlayerActionSelected("ATTACK");
      entity.getEvents().trigger("onAttack", manager.getPlayerStats(), manager.getEnemyStats());
  }

  /**
   * Signalled by clicking guard button, to then signal the GUARD combat move in the manager.
   */
  private void onGuard(Screen screen, ServiceContainer container) {
    manager.onPlayerActionSelected("GUARD");
    entity.getEvents().trigger("onGuard", manager.getPlayerStats(), manager.getEnemyStats());
  }

  /**
   * Signalled by clicking sleep button, to then signal the SLEEP combat move in the manager.
   */
  private void onSleep(Screen screen, ServiceContainer container) {
    manager.onPlayerActionSelected("SLEEP");
    entity.getEvents().trigger("onSleep", manager.getPlayerStats(), manager.getEnemyStats());
  }

  /**
   * Signalled by clicking items button.
   */
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
