package com.csse3200.game.services;

import com.csse3200.game.ui.dialoguebox.DialogueBox;
import com.badlogic.gdx.scenes.scene2d.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DialogueBoxService {
    private static final Logger logger = LoggerFactory.getLogger(DialogueBoxService.class);
    private DialogueBox currentOverlay;
    private String[][] hints;
    private String curDialogueType;

    /**
     * Create a new chat overlay with the given hint text.
     */
    public DialogueBoxService(Stage stage) {
        currentOverlay = new DialogueBox(stage);
        hints = null;
        curDialogueType = null;
    }

    /**
     * Retrieves a copy of the hints array used in the chat overlay.
     * This method returns a copy of the internal hints array to
     * prevent external modification of the original data.
     *
     * @return a copy of the hints array as a String[]
     */
    public String[][] getHints() {
        // Return a copy of the hints array to prevent modification of the original array
        return hints.clone();
    }

    /**
     * Returns the currentOverlay which can be null or not null
     * */
    public DialogueBox getCurrentOverlay() {
        return this.currentOverlay;
    }

    /**
     * Dispose of the current chat overlay if it exists.
     */
    public void disposeCurrentOverlay() {
        if (currentOverlay != null) {
            currentOverlay.dispose();
            currentOverlay = null;
            logger.debug("Disposed of current chat overlay.");
            hints = null;
        }
    }

    /**
     * Returns if the current dialgoue box is visible
     */
    public Boolean getIsVisible() {
        return currentOverlay.getIsVisible();
    }

    /**
     * Returns if the current dialogue can be changed to the new one specified in the argument.
     */
    public Boolean checkDialoguePriority(String callerClassName) {
        String[] callerClassNameSplited = callerClassName.split("\\.");
        callerClassName = callerClassNameSplited[callerClassNameSplited.length - 1];
        String newDialogueType = "";
        if (callerClassName.equals("NPCFactory")) {
            newDialogueType = "FriendlyNPC";
        } else if (callerClassName.equals("PlayerInventoryDisplay") || callerClassName.equals("ItemProximityTask")) {
            newDialogueType = "Item";
        }

        if (curDialogueType != null && (curDialogueType.equals("FriendlyNPC") && newDialogueType.equals("Item"))) {
            return false;
        }
        curDialogueType = newDialogueType;
        return true;
    }

    /**
     * Dispose of the current chat overlay if it exists.
     */
    public void hideCurrentOverlay() {
        if (currentOverlay != null) {
            curDialogueType = null;
            currentOverlay.hideDialogueBox();
            hints = null;
        }
    }

    /**
     * Update the current chat overlay if it exists.
     */
    public void updateText(String[][] text) {
        hints = text;
        String callerClassName = Thread.currentThread().getStackTrace()[2].getClassName();
        if (Boolean.TRUE.equals(checkDialoguePriority(callerClassName))) {
            if (currentOverlay == null) {
                // handling if it ever gets deleted when not supposed to
                currentOverlay = new DialogueBox(hints);
            } else {
                currentOverlay.showDialogueBox(text);
            }
        }

    }

    /**
     * Resizes the DialogueBox in the event the screen changes size
     */
    public void resizeElements() {
        currentOverlay.resizeElements();
    }
}