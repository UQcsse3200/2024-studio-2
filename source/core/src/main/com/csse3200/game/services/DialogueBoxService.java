package com.csse3200.game.services;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.ui.dialoguebox.DialogueBox;
import com.badlogic.gdx.scenes.scene2d.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DialogueBoxService {

    public enum DialoguePriority {;
        public static final int BATTLE = -3;
        public static final int FRIENDLYNPC = -2;
        public static final int ITEMINVENTORY = -1;
        public static final int NONEDEFAULT = 0;
    }

    private static final Logger logger = LoggerFactory.getLogger(DialogueBoxService.class);
    private DialogueBox currentOverlay;
    private String[][] hints;
    private int curPriority;
    private Entity currentEntity;

    /**
     * Create a new chat overlay with the given hint text.
     */
    public DialogueBoxService(Stage stage) {
        currentOverlay = new DialogueBox(stage);
        hints = null;
        curPriority = DialoguePriority.NONEDEFAULT;
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
     * Dispose of the current chat overlay if it exists.
     */
    public void hideCurrentOverlay() {
        if (currentOverlay != null) {
            curPriority = DialoguePriority.NONEDEFAULT;
            currentOverlay.hideDialogueBox();
            hints = null;
        }
    }

    /**
     * Update the current chat overlay if it exists also responsible for unhighlighting an entity if it exists.
     */
    public void updateText(String[][] text, int priority) {
        hints = text;
        if (priority <= curPriority) {
            curPriority = priority;
            if (currentOverlay == null) {
                // handling if it ever gets deleted when not supposed to
                currentOverlay = new DialogueBox(hints);
            } else {
                updateTextHelper();
            }
        }
        if (currentEntity != null) {
            AnimationRenderComponent animator =  currentEntity.getComponent(AnimationRenderComponent.class);
            if (animator != null) {
                animator.startAnimation("float");
            }
            currentEntity = null;
        }
    }

    /**
     * Update the current chat overlay if it exists also responsible for highlighting an entity.
     */
    public void updateText(String[][] text, Entity entity, int priority) {
        hints = text;
        if (priority <= curPriority) {
            curPriority = priority;
            if (currentOverlay == null) {
                // handling if it ever gets deleted when not supposed to
                currentOverlay = new DialogueBox(hints);
            } else {
                updateTextHelper();
            }
        }
        if (currentEntity != null) {
            AnimationRenderComponent animator =  currentEntity.getComponent(AnimationRenderComponent.class);
            if (animator != null) {
                animator.startAnimation("float");
            }
        }
        currentEntity = entity;
        AnimationRenderComponent animator =  currentEntity.getComponent(AnimationRenderComponent.class);
        if (animator != null) {
            animator.startAnimation("selected");
        }
    }

    /**
     * Resizes the DialogueBox in the event the screen changes size
     */
    public void resizeElements() {
        currentOverlay.resizeElements();
    }

    /**
     * Update the current chat overlay if it exists.
     */
    private void updateTextHelper() {
        if (currentOverlay == null) {
            // handling if it ever gets deleted when not supposed to
            currentOverlay = new DialogueBox(hints);
        } else {
            currentOverlay.showDialogueBox(hints);
        }
    }
}