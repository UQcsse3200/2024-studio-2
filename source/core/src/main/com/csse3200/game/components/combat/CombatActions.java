package com.csse3200.game.components.combat;

import com.csse3200.game.GdxGame;
import com.csse3200.game.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class listens to events relevant to the Main Game Screen and does something when one of the
 * events is triggered.
 */
public class CombatActions extends Component {
  private static final Logger logger = LoggerFactory.getLogger(CombatActions.class);
  private GdxGame game;

  public CombatActions(GdxGame game) {
    this.game = game;
  }

  @Override
  public void create() {
    entity.getEvents().addListener("run", this::onRun);
  }

  /**
   * Swaps to the Main Menu screen.
   */
  private void onRun() {
    logger.info("Exiting combat screen to return to main game screen");
    game.setScreen(GdxGame.ScreenType.MAIN_GAME);
  }
}