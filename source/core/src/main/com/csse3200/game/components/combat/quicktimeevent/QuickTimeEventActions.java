package com.csse3200.game.components.combat.quicktimeevent;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuickTimeEventActions extends Component {
    private static final Logger logger = LoggerFactory.getLogger(QuickTimeEventActions.class);
    private int count = 0;
    private static final int MAX_COUNT = 3;
    private long lastUpdate;
    private GameTime gameTime;
    private GdxGame game;

    public QuickTimeEventActions(GdxGame game) {
        this.game = game;
    }

    @Override
    public void create() {
        gameTime = ServiceLocator.getTimeSource();
        entity.getEvents().addListener("start", this::onStart);
        entity.getEvents().addListener("exit", this::onExit);
    }

    @Override
    public void update() {
        // Handle count down to quick-time event start
        if (count != 0 && gameTime.getTimeSince(lastUpdate) > 1000) {
            // 1 second has passed - update counter
            count -= 1;
            if (count != 0) {
                entity.getEvents().trigger("editLabel", count + "");
            } else {
                entity.getEvents().trigger("editLabel", "");
                // Trigger a listener in QuickTimeEventHandler
            }
            lastUpdate = gameTime.getTime();
        }
    }

    /**
     * Initiates count-down to quick-time event start
     */
    private void onStart() {
        count = MAX_COUNT;
        entity.getEvents().trigger("editLabel", count + "");
        lastUpdate = gameTime.getTime();
    }

    /**
     * Exits to the main menu
     */
    private void onExit() {
        logger.info("Exit QuickTimeEvent screen");
        game.setScreen(GdxGame.ScreenType.MAIN_MENU);
    }
}
