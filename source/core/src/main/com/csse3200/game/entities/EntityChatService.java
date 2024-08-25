package com.csse3200.game.entities;

import com.csse3200.game.ui.ChatOverlay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntityChatService {
    private static final Logger logger = LoggerFactory.getLogger(EntityChatService.class);

    private ChatOverlay currentOverlay;

    /**
     * Create a new chat overlay with the given hint text.
     */
    public EntityChatService() {
        currentOverlay = null;
    }

    /**
     * Returns the currentOverlay which can be null or not null
     * */
    public ChatOverlay getCurrentOverlay() {
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
        }
    }

    /**
     * Update the current chat overlay if it exists.
     */
    public void updateText(String[] text) {
        if (currentOverlay != null) {
            currentOverlay.dispose();
            currentOverlay = null;
        }

        currentOverlay = new ChatOverlay(text);
    }
}
