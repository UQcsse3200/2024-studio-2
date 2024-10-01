package com.csse3200.game.components.combat;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.inventory.PlayerInventoryDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.inventory.items.AbstractItem;
import com.csse3200.game.services.ServiceContainer;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * This class listens to events relevant to the combat screen and does something when one of the
 * events is triggered.
 */
public class CombatActions extends Component {
  private static final Logger logger = LoggerFactory.getLogger(CombatActions.class);
  private GdxGame game;
  private final CombatManager manager;
  private final Screen previousScreen;
  private final ServiceContainer previousServices;

  public CombatActions(GdxGame game, CombatManager manager, Screen previousScreen, ServiceContainer previousServices) {
    this.game = game;
    //this.enemy = enemy;
    this.manager = manager;
    this.previousServices = previousServices;
    this.previousScreen = previousScreen;
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
    entity.getEvents().addListener("kangaDefeated", this::onKangaDefeated);
    entity.getEvents().addListener("finishedEndCombatDialogue", (Entity triggeredEntity) -> {
      game.returnFromCombat(previousScreen, previousServices, triggeredEntity);
    });

  }

  /**
   * Swaps from combat screen to Main Game screen in the event of a won combat sequence.
   * 'Kills' enemy entity on return to combat screen.
   */
  private void onCombatWin(Entity enemy) {
    logger.debug("Returning to main game screen after combat win.");
    // Reset player's stamina.
    manager.getPlayer().getComponent(CombatStatsComponent.class).setStamina(100);
    this.manager.getPlayer().getEvents().trigger("defeatedEnemy",this.manager.getEnemy());
    this.manager.getPlayer().getComponent(PlayerInventoryDisplay.class).regenerateDisplay();
    // For CombatStatsDisplay to update
    entity.getEvents().trigger("onCombatWin", manager.getPlayerStats());
    // For CombatButtonDisplay DialogueBox
    entity.getEvents().trigger("endOfCombatDialogue", enemy, true);

    // CODE REQUIRED BY TEAM 4 TO IMPLEMENT CONVERSIONS:
//    if (previousScreen instanceof MainGameScreen mainGameScreen) {
//      ForestGameArea gameArea = mainGameScreen.getGameArea();
//      List<Entity> enemies = gameArea.getEnemies();
//
//      EntityConverter.convertToFriendly(manager.getEnemy(), manager.getPlayer(), enemies);
//    }
//    game.returnFromCombat(previousScreen, previousServices, enemy);
  }

  /**
   * Swaps from combat screen to Game Over screen upon the event that the player is defeated in battle.
   */
  private void onCombatLoss(Entity enemy) {
    logger.debug("Returning to main game screen after combat loss.");
    // For CombatStatsDisplay to update
    entity.getEvents().trigger("onCombatLoss", manager.getPlayerStats());
    // For CombatButtonDisplay DialogueBox
    entity.getEvents().trigger("endOfCombatDialogue", enemy, false);
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
    logger.debug("before Guard");
    manager.onPlayerActionSelected("GUARD");
    entity.getEvents().trigger("onGuard", manager.getPlayerStats(), manager.getEnemyStats());
  }

  /**
   * Switches to the end game stats screen upon defeating the final Kanga Boss.
   */
  private void onKangaDefeated() {
    logger.debug("Switching to end game stats screen.");
    game.setScreen(GdxGame.ScreenType.END_GAME_STATS);

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
    logger.debug("Clicked Items");
    entity.getEvents().trigger("toggleCombatInventory");
    entity.getComponent(CombatInventoryDisplay.class).regenerateInventory();
    if (Objects.equals(entity.getEvents().getLastTriggeredEvent(), "itemUsed")) {
      manager.onPlayerActionSelected("ITEM");
      entity.getEvents().trigger("onItems", manager.getPlayerStats(), manager.getEnemyStats());
    }
  }

  /**
   * Called when the screen is disposed to free resources.
   */
  @Override
  public void dispose() {
    // Dispose of the stage to free up resources
  }
}
