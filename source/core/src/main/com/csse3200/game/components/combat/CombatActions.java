package com.csse3200.game.components.combat;

import com.badlogic.gdx.Screen;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.Component;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.entities.Entity;
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
  private Entity enemy;
  private Screen previousScreen;
  private ServiceContainer previousServices;

    public CombatActions(GdxGame game, CombatManager manager, Entity enemy, Screen previousScreen, ServiceContainer previousServices) {
    this.game = game;
    this.enemy = enemy;
    this.manager = manager;
    this.previousServices = previousServices;
    this.previousScreen = previousScreen;
  }

  @Override
  public void create() {
    entity.getEvents().addListener("combatWin", this::onCombatWin);
    entity.getEvents().addListener("combatLose", this::onCombatLoss);
    entity.getEvents().addListener("Attack", this::onAttack);
    entity.getEvents().addListener("Guard", this::onGuard);
    entity.getEvents().addListener("Sleep", this::onSleep);
    entity.getEvents().addListener("Items", this::onItems);
    entity.getEvents().addListener("kangaDefeated", this::onKangaDefeated);

  }

  /**
   * Swaps from combat screen to Main Game screen in the event of a won combat sequence.
   * 'Kills' enemy entity on return to combat screen.
   */
  private void onCombatWin() {
    logger.info("Returning to main game screen after combat win.");
    entity.getEvents().trigger("onCombatWin", manager.getPlayerStats());
    game.returnFromCombat(previousScreen, previousServices, enemy);
  }

  /**
   * Swaps from combat screen to Main Game screen in the event of a lost combat sequence.
   */
  private void onCombatLoss(Screen screen, ServiceContainer container) {
    logger.info("Returning to main game screen after combat loss.");
    // Set current screen to original MainGameScreen
    game.setScreen(GdxGame.ScreenType.MAIN_GAME);
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

  /**
   * Switches to the end game stats screen upon defeating the final Kanga Boss.
   */
  private void onKangaDefeated() {
    logger.info("Switching to end game stats screen.");
    game.setScreen(GdxGame.ScreenType.END_GAME_STATS);
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
