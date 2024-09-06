package com.csse3200.game.entities;

import com.csse3200.game.ui.DialogueBox;
import com.badlogic.gdx.scenes.scene2d.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.csse3200.game.concurrency.JobSystem;
import java.util.concurrent.CompletableFuture;

public class EntityChatService {
    private static final Logger logger = LoggerFactory.getLogger(EntityChatService.class);
    private DialogueBox currentOverlay;
    private String[] hints;
    private boolean isVisible;

    /**
     * Create a new chat overlay with the given hint text.
     */
    public EntityChatService() {
        currentOverlay = null;
        hints = null;
    }

    /**
     * Create a new chat overlay with the given hint text.
     */
    public EntityChatService(Stage stage) {
        currentOverlay = new DialogueBox(stage);
        hints = null;
    }

    /**
     * Retrieves a copy of the hints array used in the chat overlay.
     * This method returns a copy of the internal hints array to
     * prevent external modification of the original data.
     *
     * @return a copy of the hints array as a String[]
     */
    public String[] getHints() {
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
     * Dispose of the current chat overlay if it exists.
     */
    public void hideCurrentOverlay() {
        if (currentOverlay != null) {
            currentOverlay.hideDialogueBox();
            hints = null;
        }
    }

    /**
     * Update the current chat overlay if it exists.
     */
    public void updateText(String[] text) {
        hints = text;
        if (currentOverlay == null) {
            // handling if it ever gets deleted when not supposed to
            currentOverlay = new DialogueBox(hints);
        } else {
            currentOverlay.showDialogueBox(text);
        }

    }
}
