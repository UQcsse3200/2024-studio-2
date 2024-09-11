package com.csse3200.game.components.combat.quicktimeevent;

import com.csse3200.game.GdxGame;
import com.csse3200.game.components.Component;
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
                entity.getEvents().trigger("startQuickTime", quickTimeEventsDemo());
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

    /**
     * Creates a quick-time events demo
     *
     * @returns list of quick-time events
     */
    private static QuickTimeEvent[] quickTimeEventsDemo() {
        float delay = 0.1f;
        float[] durations = {0.7f, 0.65f, 0.55f, 0.45f};
        QuickTimeEvent[] quickTimeEvents = new QuickTimeEvent[durations.length];
        for (int i = 0; i < durations.length; i++) {
            quickTimeEvents[i] = new QuickTimeEvent(durations[i], delay);
        }
        return quickTimeEvents;
    }
}