package com.csse3200.game.components.combat;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.ForestGameArea;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityConverter;
import com.csse3200.game.screens.GameOverLoseScreen;
import com.csse3200.game.screens.MainGameScreen;
import com.csse3200.game.services.ServiceContainer;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

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
    entity.getEvents().addListener("combatLossBoss", this::onCombatLossBoss);
    entity.getEvents().addListener("Attack", this::onAttack);
    entity.getEvents().addListener("Guard", this::onGuard);
    entity.getEvents().addListener("Sleep", this::onSleep);
    entity.getEvents().addListener("Items", this::onItems);
    entity.getEvents().addListener("bossCombatWin", this::onBossCombatWin);
    entity.getEvents().addListener("waterBossDefeated", this::onWaterBossDefeated);
    entity.getEvents().addListener("airBossDefeated", this::onAirBossDefeated);
    entity.getEvents().addListener("finishedEndCombatDialogue", (Entity triggeredEntity) -> {
      game.returnFromCombat(previousScreen, previousServices, triggeredEntity);
    });
    entity.getEvents().addListener("finishedBossLossCombatDialogue", () -> {
      game.setScreen(GdxGame.ScreenType.GAME_OVER_LOSE);
    });
  }

  /**
   * Swaps from combat screen to Main Game screen in the event of a won combat sequence.
   * 'Kills' enemy entity on return to combat screen.
   */
  private void onCombatWin(Entity enemy) {
    logger.info("Returning to main game screen after combat win.");
    // Reset player's stamina.
    manager.getPlayer().getComponent(CombatStatsComponent.class).setStamina(100);
    this.manager.getPlayer().getEvents().trigger("defeatedEnemy",this.manager.getEnemy());
    // For CombatStatsDisplay to update
    entity.getEvents().trigger("onCombatWin", manager.getPlayerStats());

    // For CombatButtonDisplay DialogueBox
    entity.getEvents().trigger("endOfCombatDialogue", enemy, true);
    int enemyExp = enemy.getComponent(CombatStatsComponent.class).getExperience();
    manager.getPlayer().getComponent(CombatStatsComponent.class).addExperience(enemyExp);
  }

  /**
   * Swaps from combat screen to Game Over screen upon the event that the player is defeated in battle.
   */
  private void onCombatLoss(Entity enemy) {
    logger.info("Returning to main game screen after combat loss.");

    manager.getPlayer().getComponent(CombatStatsComponent.class).setStamina(100);

    // For CombatStatsDisplay to update
    entity.getEvents().trigger("onCombatLoss", manager.getPlayerStats());

    // For CombatButtonDisplay DialogueBox
    entity.getEvents().trigger("endOfCombatDialogue", enemy, false);
  }

  /**
   * Swaps from combat screen to Game Over screen upon the event that the player is defeated in battle.
   */
  private void onCombatLossBoss(Entity enemy) {
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
    logger.info("before Guard");
    manager.onPlayerActionSelected("GUARD");
    entity.getEvents().trigger("onGuard", manager.getPlayerStats(), manager.getEnemyStats());
  }

  /**
   * If boss is defeated, update player stats and display boss combat win dialogue.
   * Disposes of Boss entity after post combat dialogue.
   *
   * @param bossEnemy The boss entity defeated by the player
   * TODO: open up new area.
   */
    private void onBossCombatWin(Entity bossEnemy) {
    logger.info("Switching back to main game after defeating kangaroo boss.");

    // Reset player's stamina.
    manager.getPlayer().getComponent(CombatStatsComponent.class).setStamina(100);
    this.manager.getPlayer().getEvents().trigger("defeatedEnemy",this.manager.getEnemy());
    // For CombatStatsDisplay to update
    entity.getEvents().trigger("onCombatWin", manager.getPlayerStats());

    // For CombatButtonDisplay DialogueBox
    entity.getEvents().trigger("endOfBossCombatDialogue", bossEnemy, true);
    int enemyExp = bossEnemy.getComponent(CombatStatsComponent.class).getExperience();
    manager.getPlayer().getComponent(CombatStatsComponent.class).addExperience(enemyExp);
  }

  /**
   * Switches back to the main game after updating player stats and experience, upon defeating the Water Boss.
   * Disposes of Water Boss entity after post combat dialogue.
   */
  private void onWaterBossDefeated(Entity enemy) {
    logger.info("Switching back to main game after defeating kangaroo boss.");

    // Reset player's stamina.
    manager.getPlayer().getComponent(CombatStatsComponent.class).setStamina(100);
    this.manager.getPlayer().getEvents().trigger("defeatedEnemy",this.manager.getEnemy());
    // For CombatStatsDisplay to update
    entity.getEvents().trigger("onCombatWin", manager.getPlayerStats());

    // For CombatButtonDisplay DialogueBox
    entity.getEvents().trigger("endOfLandBossCombatDialogue", enemy, true);
    int enemyExp = enemy.getComponent(CombatStatsComponent.class).getExperience();
    manager.getPlayer().getComponent(CombatStatsComponent.class).addExperience(enemyExp);
  }

  /**
   * Switches back to the main game after updating player stats and experience, upon defeating the Air Boss.
   * Disposes of Air Boss entity after post combat dialogue.
   */
  private void onAirBossDefeated(Entity enemy) {
    logger.info("Switching back to main game after defeating kangaroo boss.");

    // Reset player's stamina.
    manager.getPlayer().getComponent(CombatStatsComponent.class).setStamina(100);
    this.manager.getPlayer().getEvents().trigger("defeatedEnemy",this.manager.getEnemy());
    // For CombatStatsDisplay to update
    entity.getEvents().trigger("onCombatWin", manager.getPlayerStats());

    // For CombatButtonDisplay DialogueBox
    entity.getEvents().trigger("endOfLandBossCombatDialogue", enemy, true);
    int enemyExp = enemy.getComponent(CombatStatsComponent.class).getExperience();
    manager.getPlayer().getComponent(CombatStatsComponent.class).addExperience(enemyExp);
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
