package com.csse3200.game.input;

import com.csse3200.game.components.combat.TouchCombatInputComponent;
import com.csse3200.game.components.player.TouchPlayerInputComponent;
import com.csse3200.game.ui.DialogueBox.TouchDialogueBoxInputComponent;
import com.csse3200.game.ui.terminal.TouchTerminalInputComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TouchInputFactory extends InputFactory {
  private static final Logger logger = LoggerFactory.getLogger(TouchInputFactory.class);

  /**
   * Creates an input handler for the player
   *
   * @return Player input handler
   */
  @Override
  public InputComponent createForPlayer() {
    logger.debug("Creating player input handler");
    return new TouchPlayerInputComponent();
  }

  /**
   * Creates an input handler for the terminal
   *
   * @return Terminal input handler
   */
  @Override
  public InputComponent createForTerminal() {
    logger.debug("Creating terminal input handler");
    return new TouchTerminalInputComponent();
  }

  /**
   * Creates an input handler for combat.
   *
   * @return combat input handler
   */
  @Override
  public InputComponent createForCombat() {
    logger.debug("Creating combat input handler");
    return new TouchCombatInputComponent();
  }

  /**
   * Dialogue Box Input Component
   */
  @Override
  public InputComponent createForDialogue() {
    logger.debug("Creating Dialgoue Box input handler");
    return new TouchDialogueBoxInputComponent();
  }
}
