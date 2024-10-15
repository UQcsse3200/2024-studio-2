package com.csse3200.game.components.combat.quicktimeevent;

import com.badlogic.gdx.Screen;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.combat.move.CombatMoveComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.screens.CombatScreen;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceContainer;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuickTimeEventActions extends Component {
    private static final Logger logger = LoggerFactory.getLogger(QuickTimeEventActions.class);
    private static GdxGame game;
    private GameTime gameTime;
    public int count = 0;
    private static final int MAX_COUNT = 3;
    private long lastUpdate;
    private static Screen oldScreen;
    private static ServiceContainer oldScreenServices;
    private static Entity player;
    private CombatMoveComponent playerMove;
    private static Entity enemy;
    private int score = 0;
    public int QTE_exitScore = 0;
    public boolean QTE_hitFlag = false;
    public QuickTimeEventDisplay QTE_display;


    public QuickTimeEventActions(GdxGame game,Screen oldScreen, ServiceContainer oldScreenServices, Entity player, Entity enemy, QuickTimeEventDisplay QteDisplay ) {
        this.game = game;
        this.oldScreenServices = oldScreenServices;
        this.oldScreen = oldScreen;
        this.player = player;
        this.enemy = enemy;
        this.QTE_display = QteDisplay;
    }

    @Override
    public void create() {
        gameTime = ServiceLocator.getTimeSource();
        entity.getEvents().addListener("start", this::onStart);

        entity.getEvents().trigger("editLabel", count + "");
        lastUpdate = gameTime.getTime();

        logger.info("QuickTimeEventActions::create() before on exit");
        entity.getEvents().addListener("exit", this::onExit);
        logger.info("QuickTimeEventActions::create() after on exit");
    }

    @Override
    public void update() {

        // Handle count down to quick-time event start
        if (count != 0 && gameTime.getTimeSince(lastUpdate) >= 1000) {
            // 1 second has passed - update counter
            count -= 1;
            if (count != 0) {
                entity.getEvents().trigger("editLabel", count + "");
            } else {
                entity.getEvents().trigger("startQuickTime", quickTimeEventsDemo());
            }
            lastUpdate = gameTime.getTime();
        }

        if(count == 0) {
            game.setScreen(new CombatScreen(game, oldScreen, oldScreenServices, player, enemy, QTE_display.getQTEScore()));
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
        QTE_hitFlag = true;
       // game.setScreen(GdxGame.ScreenType.COMBAT);
//        entity.getEvents().trigger(game.setScreen(new CombatScreen(game, oldScreen, oldScreenServices, player, enemy, score)) , 0 );
//        int score = 0;



    }

    /**
     * Creates a demo list of four quick-time events
     * with different durations
     */
    private static QuickTimeEvent[] quickTimeEventsDemo() {
        float delay = 0.2f;
        float[] durations = {0.7f, 0.65f, 0.55f, 0.45f};
        QuickTimeEvent[] quickTimeEvents = new QuickTimeEvent[durations.length];
        for (int i = 0; i < durations.length; i++) {
            quickTimeEvents[i] = new QuickTimeEvent(durations[i], delay);
        }

        return quickTimeEvents;

    }
}