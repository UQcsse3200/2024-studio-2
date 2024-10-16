package com.csse3200.game.components.combat;

import com.badlogic.gdx.Screen;
import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.MapHandler;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.inventory.CombatInventoryDisplay;
import com.csse3200.game.components.inventory.PlayerInventoryDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

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
    entity.getEvents().addListener("bossCombatWin", this::onBossCombatWin);
    entity.getEvents().addListener("bossCombatLoss", this::onBossCombatLoss);
    entity.getEvents().addListener("Attack", this::onAttack);
    entity.getEvents().addListener("Guard", this::onGuard);
    entity.getEvents().addListener("Sleep", this::onSleep);
    entity.getEvents().addListener("Items", this::onItems);
    entity.getEvents().addListener("finishedEndCombatDialogue", (Entity triggeredEntity) ->
      game.returnFromCombat(previousScreen, previousServices, triggeredEntity));
    entity.getEvents().addListener("finishedBossLossCombatDialogue", () ->
      game.setScreen(GdxGame.ScreenType.GAME_OVER_LOSE));
    entity.getEvents().addListener("finishedFinalCombatDialogue", () ->
      game.setScreen(GdxGame.ScreenType.END_GAME_STATS));
  }

  /**
   * Swaps from combat screen to Main Game screen in the event of a won combat sequence.
   * 'Kills' enemy entity on return to combat screen.
   */
  private void onCombatWin(Entity enemy) {
    logger.debug("Returning to main game screen after combat win.");
    game.setEnemyWasBeaten(true);
    this.manager.getPlayer().getEvents().trigger("defeatedEnemy",this.manager.getEnemy());
    this.manager.getPlayer().getComponent(PlayerInventoryDisplay.class).regenerateDisplay();

    int enemyExp = enemy.getComponent(CombatStatsComponent.class).getExperience();
    manager.getPlayer().getComponent(CombatStatsComponent.class).addExperience(enemyExp);

    // For CombatStatsDisplay to update
    entity.getEvents().trigger("onCombatWin", manager.getPlayerStats());

    // For CombatButtonDisplay DialogueBox
    entity.getEvents().trigger("endOfCombatDialogue", enemy, true);
  }

  /**
   * Swaps from combat screen to Game Over screen upon the event that the player is defeated in battle.
   * Also handles the consequences of losing (inventory and experience loss)
   */
  private void onCombatLoss(Entity enemy) {
    logger.debug("Returning to main game screen after combat loss.");
    int lossExp = 0;
    int maxPlayerHealth = manager.getPlayerStats().getMaxHealth();
    int maxPlayerHunger = manager.getPlayerStats().getMaxHunger();
    int statusRestoreMult = 2;
    // Clear the player inventory and wipe their current experience progress
    manager.getPlayerStats().setExperience(lossExp);
    this.manager.getPlayer().getComponent(PlayerInventoryDisplay.class).clearInventory();
    this.manager.getPlayer().getComponent(PlayerInventoryDisplay.class).regenerateDisplay();

    // Reset player health and hunger back to half
    manager.getPlayerStats().setHealth(maxPlayerHealth/statusRestoreMult);
    manager.getPlayerStats().setHunger(maxPlayerHunger/statusRestoreMult);

    // For CombatStatsDisplay to update
    entity.getEvents().trigger("onCombatLoss", manager.getPlayerStats());

    // For CombatButtonDisplay DialogueBox
    entity.getEvents().trigger("endOfCombatDialogue", enemy, false);
  }

  /**
   * If boss is defeated, update player stats and display boss combat win dialogue.
   * Disposes of Boss entity after post combat dialogue.
   *
   * @param bossEnemy The boss entity defeated by the player
   */
  private void onBossCombatWin(Entity bossEnemy) {
    logger.info("Boss combat complete.");

    if (bossEnemy.getEnemyType() == Entity.EnemyType.KANGAROO) {
      entity.getEvents().trigger("landBossDefeated");
      MapHandler.unlockNextArea();
    } else if (bossEnemy.getEnemyType() == Entity.EnemyType.WATER_BOSS) {
      entity.getEvents().trigger("waterBossDefeated");
      MapHandler.unlockNextArea();
    } else if (bossEnemy.getEnemyType() == Entity.EnemyType.AIR_BOSS) {
      entity.getEvents().trigger("airBossDefeated");
    }

    this.manager.getPlayer().getEvents().trigger("defeatedEnemy",this.manager.getEnemy());
    // For CombatStatsDisplay to update
    entity.getEvents().trigger("onCombatWin", manager.getPlayerStats());

    // For CombatButtonDisplay DialogueBox
    entity.getEvents().trigger("endOfBossCombatDialogue", bossEnemy, true);
    int enemyExp = bossEnemy.getComponent(CombatStatsComponent.class).getExperience();
    manager.getPlayer().getComponent(CombatStatsComponent.class).addExperience(enemyExp);
  }

  /**
   * Swaps from combat screen to Game Over screen upon the event that the player is defeated in battle.
   */
  private void onBossCombatLoss(Entity enemy) {
    logger.info("Returning to main game screen after combat loss.");

    // For CombatButtonDisplay DialogueBox
    entity.getEvents().trigger("endOfBossCombatDialogue", enemy, false);
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
    entity.getComponent(CombatInventoryDisplay.class).regenerateDisplay();
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
