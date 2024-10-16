package com.csse3200.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.csse3200.game.GdxGame;
import com.csse3200.game.overlays.*;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;

public class PausableScreen extends ScreenAdapter {
    private static final Logger logger = LoggerFactory.getLogger(PausableScreen.class);
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
        if (activeOverlayTypes.get(overlayType) == null){
            return;
        }
        if (enabledOverlays.isEmpty()) {
            this.rest();
        }
        else {
            enabledOverlays.getFirst().rest();
        }
        switch (overlayType) {
            case QUEST_OVERLAY -> enabledOverlays.addFirst(new QuestOverlay(this));
            case PAUSE_OVERLAY -> enabledOverlays.addFirst(new PauseOverlay(this, game));
            case PLAYER_STATS_OVERLAY -> enabledOverlays.addFirst(new PlayerStatsOverlay(this));
            case SETTINGS_OVERLAY -> enabledOverlays.addFirst(new SettingsOverlay(this));
            default -> logger.warn("Unknown Overlay type: {}", overlayType);
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
        // Pause the InGameTime and DayNightCycle
        if (ServiceLocator.getInGameTime() != null) {
            ServiceLocator.getInGameTime().pause();
        }
        if (ServiceLocator.getDayNightCycle() != null) {
            ServiceLocator.getDayNightCycle().pause();
        }
        ServiceLocator.getEntityService().restWholeScreen();
    }

    /**
     * Wakes the screen from a resting state.
     */
    public void wake() {
        logger.info("Screen is Awake");
        resting = false;

        // Resume the InGameTime and DayNightCycle
        if (ServiceLocator.getInGameTime() != null) {
            ServiceLocator.getInGameTime().resume();
        }
        if (ServiceLocator.getDayNightCycle() != null) {
            ServiceLocator.getDayNightCycle().resume();
        }

        ServiceLocator.getEntityService().wakeWholeScreen();
    }
}
