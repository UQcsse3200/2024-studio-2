package com.csse3200.game.lighting;

import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
import com.csse3200.game.services.InGameTime;
import com.csse3200.game.services.ServiceLocator;

/**
 * Handles ambient lighting for the game to produce a day/night cycle effect.
 * Controls time of day and day length, and can pause and resume the cycle.
 */
public class DayNightCycle {
    public static final long DAY_LENGTH = 24000; // milliseconds in a day
    public static final Color[] keyTimes = {
            new Color(10f / 255, 70f / 255, 200f / 255, 1f), // midnight
            new Color(10f / 255, 80f / 255, 220f / 255, 1f), // late night
            new Color(220f / 255, 200f / 255, 175f / 255, 1f), // dawn
            new Color(250f / 255, 235f / 255, 200f / 255, 1f), // morning
            new Color(255f / 255, 250f / 255, 230f / 255, 1f), // noon
            new Color(250f / 255, 240f / 255, 200f / 255, 1f), // late afternoon
            new Color(215f / 255, 150f / 255, 165f / 255, 1f), // dusk
            new Color(10f / 255, 80f / 255, 220f / 255, 1f)   // early night
    };

    private final RayHandler rayHandler;
    private long currentTime; // in-game time in milliseconds
    private long lastUpdateTime; // last update timestamp in milliseconds
    private boolean isPaused;

    private final InGameTime inGameTime;

    public DayNightCycle(RayHandler rayHandler) {
        this.rayHandler = rayHandler;
        this.inGameTime = ServiceLocator.getInGameTime();
    }

    /**
     * Pauses the day/night cycle.
     */
    public void pause() {
        isPaused = true;
    }

    /**
     * Resumes the day/night cycle.
     */
    public void resume() {
        isPaused = false;
        // Reset lastUpdateTime to prevent time jump after resuming
        lastUpdateTime = ServiceLocator.getTimeSource().getTime();
    }

    /**
     * Returns a float representing the time of day. 0.0 is midnight and 1.0 is the next midnight.
     *
     * @return the time of day as a float between 0.0 and 1.0
     */
    public float getTimeOfDay() {
        long currentTime = inGameTime.getTime();
        return ((float) (currentTime % DAY_LENGTH)) / DAY_LENGTH;
    }

    /**
     * Updates the day/night cycle ambient lighting effect.
     */
    public void update() {
        float timeOfDay = getTimeOfDay();
        setEffects(LightingUtils.interpolateColorCycle(keyTimes, timeOfDay));
    }


    /**
     * Sets the ambient lighting to the specified color.
     *
     * @param color the current ambient light color for the day/night cycle
     */
    private void setEffects(Color color) {
        rayHandler.setAmbientLight(color);
    }
}
