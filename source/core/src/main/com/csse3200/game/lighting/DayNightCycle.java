package com.csse3200.game.lighting;

import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;

import static com.csse3200.game.lighting.LightingUtils.interpolateColorCycle;

public class DayNightCycle {
    public static final long DAY_LENGTH = 24000; // one second per in-game hour
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
    private final long timeStart;
    GameTime timeSource;
    RayHandler rayHandler;

    public float getTimeOfDay() {
        long time = timeSource.getTime();
        return (float) ((time - timeStart) % DAY_LENGTH) / DAY_LENGTH;
    }

    public DayNightCycle(RayHandler rayHandler) {
        this.rayHandler = rayHandler;
        timeSource = ServiceLocator.getTimeSource();
        timeStart = timeSource.getTime();
    }

    private void setEffects(Color key) {
        rayHandler.setAmbientLight(key);
    }

    public void update() {
        float timeOfDay = getTimeOfDay();
        setEffects(interpolateColorCycle(keyTimes, timeOfDay));
    }
}
