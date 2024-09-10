package com.csse3200.game.components.combat;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
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
  private final GdxGame game;
  private final CombatManager manager;
  private final Screen previousScreen;
  private final ServiceContainer previousServices;
  private Stage stage;
  private Entity enemy;

  public CombatActions(GdxGame game, CombatManager manager, Screen previousScreen, ServiceContainer previousServices, Entity enemy) {
    this.game = game;
    this.manager = manager;
    this.previousScreen = previousScreen;
    this.previousServices = previousServices;
    this.enemy = enemy;
  }

  @Override
  public void create() {
    entity.getEvents().addListener("combatWin", this::onCombatWin);
    entity.getEvents().addListener("combatLose", this::onCombatLoss);
    entity.getEvents().addListener("Attack", this::onAttack);
    entity.getEvents().addListener("Guard", this::onGuard);
    entity.getEvents().addListener("Counter", this::onCounter);

    logger.info("Player health start: {}", manager.getPlayer().getComponent(CombatStatsComponent.class).getHealth());
    logger.info("Enemy health start: {}", manager.getEnemy().getComponent(CombatStatsComponent.class).getHealth());
  }

  /**
   * Swaps from combat screen to Main Game screen in the event of a won combat sequence.
   * 'Kills' enemy entity on return to combat screen.
   */
  private void onCombatWin() {
    logger.info("Returning to main game screen after combat win.");
    // Reset player's stamina.
    game.returnFromCombat(previousScreen, previousServices, enemy);
  }

  /**
   * Swaps from combat screen to Main Game screen in the event of a lost combat sequence.
   */
  private void onCombatLoss(Screen screen, ServiceContainer container) {
    logger.info("Returning to main game screen after combat loss.");
    game.setOldScreen(previousScreen, previousServices);
  }

  private void onAttack(Screen screen, ServiceContainer container) {
    logger.info("Attack clicked.");
    manager.onPlayerActionSelected("ATTACK");

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
