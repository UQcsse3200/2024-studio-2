package com.csse3200.game.entities;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.components.Component;
import com.csse3200.game.ui.ChatOverlay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntityChatService {
    private static final Logger logger = LoggerFactory.getLogger(EntityChatService.class);

    private ChatOverlay currentOverlay;

    /**
     * Create a new chat overlay with the given hint text.
     *
     * @param hintText The text to display in the chat overlay.
     */
    public EntityChatService() {
        currentOverlay = null;
    }

    /**
     * Dispose of the current chat overlay if it exists.
     */
    public void disposeCurrentOverlay() {
        if (currentOverlay != null) {
            currentOverlay.dispose();
            currentOverlay = null;
            logger.debug("Disposed of current chat overlay.");
        }
    }

    /**
     * Update the current chat overlay if it exists.
     */
    public void updateText(String[] Text) {
        if (currentOverlay != null) {
            currentOverlay.dispose();
            currentOverlay = null;
        }

        currentOverlay = new ChatOverlay(Text);
    }
}
