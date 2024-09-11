package com.csse3200.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.player.KeyboardPlayerInputComponent;
import com.csse3200.game.overlays.Overlay;
import com.csse3200.game.overlays.PauseOverlay;
import com.csse3200.game.overlays.QuestOverlay;
import com.csse3200.game.overlays.PlayerStatsOverlay;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;

public class PausableScreen extends ScreenAdapter {
    private static final Logger logger = LoggerFactory.getLogger(MainGameScreen.class);
    /**
     * Reference to the main game instance.
     */
    protected final GdxGame game;
    /**
     * Queue of currently enabled overlays in the game screen.
     */
    private final Deque<Overlay> enabledOverlays = new LinkedList<>();
    /**
     * Flag indicating whether the screen is in a resting state.
     */
    protected boolean resting = false;
    /**
     * Map of active overlay types and their statuses.
     */
    private final Map<Overlay.OverlayType, Boolean> activeOverlayTypes = Overlay.getNewActiveOverlayList();

    public PausableScreen(GdxGame game) {
        this.game = game;
    }


    /**
     * Adds an overlay to the screen.
     * @param overlayType The type of overlay to add.
     */
    public void addOverlay(Overlay.OverlayType overlayType){
        logger.debug("Attempting to Add {} Overlay", overlayType);
        if (activeOverlayTypes.get(overlayType)){
            return;
        }
        if (enabledOverlays.isEmpty()) {
            this.rest();
        }
        else {
            enabledOverlays.getFirst().rest();
        }
        switch (overlayType) {
            case QUEST_OVERLAY:
                enabledOverlays.addFirst(new QuestOverlay(this));
                break;
            case PAUSE_OVERLAY:
                enabledOverlays.addFirst(new PauseOverlay(this, game));
                break;
            case PLAYER_STATS_OVERLAY:
                enabledOverlays.addFirst(new PlayerStatsOverlay(this));
                break;
            default:
                logger.warn("Unknown Overlay type: {}", overlayType);
                break;
        }
        logger.info("Added {} Overlay", overlayType);
        activeOverlayTypes.put(overlayType,true);
    }

    /**
     * Removes the topmost overlay from the screen.
     */

    public void removeOverlay(){
        logger.info("Removing top Overlay");

        if (enabledOverlays.isEmpty()){
            this.wake();
            return;
        }
        Overlay currentFirst = enabledOverlays.getFirst();
        activeOverlayTypes.put(currentFirst.overlayType,false);
        currentFirst.remove();
        enabledOverlays.removeFirst();

        if (enabledOverlays.isEmpty()){
            this.wake();

        } else {
            enabledOverlays.getFirst().wake();
        }
    }

    /**
     * Puts the screen into a resting state, pausing music and resting all entities.
     */
    public void rest() {
        logger.info("Screen is resting");
        resting = true;
        ServiceLocator.getEntityService().restWholeScreen();
    }

    /**
     * Wakes the screen from a resting state.
     */
    public void wake() {
        logger.info("Screen is Awake");
        resting = false;
        ServiceLocator.getEntityService().wakeWholeScreen();
    }
}
