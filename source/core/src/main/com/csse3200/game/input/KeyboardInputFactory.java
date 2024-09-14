package com.csse3200.game.input;

import com.csse3200.game.components.combat.KeyboardCombatInputComponent;
import com.csse3200.game.components.player.KeyboardPlayerInputComponent;
import com.csse3200.game.ui.DialogueBox.TouchDialogueBoxInputComponent;
import com.csse3200.game.ui.terminal.KeyboardTerminalInputComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * KeyboardInputFactory creates input handlers that process keyboard and touch support.
 */
public class KeyboardInputFactory extends InputFactory {
    private static final Logger logger = LoggerFactory.getLogger(KeyboardInputFactory.class);

    /**
     * Creates an input handler for the player.
     * @return Player input handler
     */
    @Override
    public InputComponent createForPlayer() {
        logger.debug("Creating player input handler");
        return new KeyboardPlayerInputComponent();
    }

    /**
     * Creates an input handler for the terminal.
     *
     * @return Terminal input handler
     */
    public InputComponent createForTerminal() {
        logger.debug("Creating terminal input handler");
        return new KeyboardTerminalInputComponent();
    }

    /**
     * Creates an input handler for combat.
     *
     * @return combat input handler
     */
    public InputComponent createForCombat() {
        logger.debug("Creating combat input handler");
        return new KeyboardCombatInputComponent();
    }

    /**
     * Creates an input handler for combat.
     *
     * @return combat input handler
     */
    public InputComponent createForDialogue() {
        logger.debug("Creating dialogue box input handler");
        return new TouchDialogueBoxInputComponent();
    }
}
