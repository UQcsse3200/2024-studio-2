package com.csse3200.game.lighting;

import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
import com.csse3200.game.services.ServiceLocator;

import static com.csse3200.game.lighting.LightingUtils.interpolateColorCycle;

/**
 * Class that handles ambient lighting for the game to produce a day/night cycle affect.
 * Controls time of day and day length.
 */
public class DayNightCycle {
    public static final long DAY_LENGTH = 24000; // 1 second per in-game hour, will be increased.
    public static final Color[] keyTimes = {
            new Color(10f/255, 70f/255, 200f/255, 1f), // mid night
            new Color(10f/255, 80f/255, 220f/255, 1f), // late night
            new Color(220f/255, 200f/255, 175f/255, 1f), // dawn
            new Color(250f/255, 235f/255, 200f/255, 1f), // morning
            new Color(255f/255, 250f/255, 230f/255, 1f), // noon
            new Color(250f/255, 240f/255, 200f/255, 1f), // late after-noon
            new Color(215f/255, 150f/255, 165f/255, 1f), // dusk
            new Color(10f/255, 80f/255, 220f/255, 1f)  // early night
    };
    RayHandler rayHandler;

    /**
     * Returns a float representing the time of day. 0.0 is midnight and 1.0 is midnight the next day.
     * Uses the game time source in the Service locator.
     * @return the time of day
     */
    public static float getTimeOfDay() {
        long time = ServiceLocator.getTimeSource().getTime();
        return (float) (time % DAY_LENGTH) / DAY_LENGTH;
    }

    public DayNightCycle(RayHandler rayHandler) {
        this.rayHandler = rayHandler;
    }

    /**
     * Sets the ambient lighting to a specified colour.
     * @param key the current ambient light color for the day/night cycle
     */
    private void setEffects(Color key) {
        rayHandler.setAmbientLight(key);
    }

    /**
     * Updates the day/night cycle ambient lighting effect.
     */
    public void update() {
        float timeOfDay = getTimeOfDay();
        setEffects(interpolateColorCycle(keyTimes, timeOfDay));
    }
}
