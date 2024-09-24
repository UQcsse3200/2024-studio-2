package com.csse3200.game.lighting;

import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;

public class DayNightCycle {
    private static final long DAY_LENGTH = 24000; // one second per in-game hour
    private static final KeyTime[] keyTimes = {
            new KeyTime(10, 70, 200), // mid night
            new KeyTime(10, 80, 220), // late night
            new KeyTime(220, 200, 175), // dawn
            new KeyTime(250, 235, 200), // morning
            new KeyTime(255, 250, 230), // noon
            new KeyTime(250, 240, 200), // late after-noon
            new KeyTime(215, 150, 165), // dusk
            new KeyTime(10, 80, 220)  // early night
    };
    private final long timeStart;
    GameTime timeSource;
    RayHandler rayHandler;

    static class KeyTime {
        public float r, g, b;
        public KeyTime(float r, float g, float b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }
    }

    public float getTimeOfDay() {
        long time = timeSource.getTime();
        return (float) ((time - timeStart) % DAY_LENGTH) / DAY_LENGTH;
    }

    public DayNightCycle(RayHandler rayHandler) {
        this.rayHandler = rayHandler;
        timeSource = ServiceLocator.getTimeSource();
        timeStart = timeSource.getTime();
    }

    private void setEffects(KeyTime key) {
        rayHandler.setAmbientLight(new Color(key.r/255,key.g/255,key.b/255,1f));
    }

    public static float lerp(float a, float b, float lerpAmount) {
        return a * (1 - lerpAmount) + b * lerpAmount;
    }

    public void update() {
        float timeOfDay = getTimeOfDay();
        int keyPrevious = (int) Math.floor(timeOfDay * keyTimes.length);
        int keyNext = (keyPrevious + 1) % keyTimes.length;
        float lerpAmount = timeOfDay * keyTimes.length - keyPrevious;
        KeyTime mixed = new KeyTime(
                lerp(keyTimes[keyPrevious].r, keyTimes[keyNext].r, lerpAmount),
                lerp(keyTimes[keyPrevious].g, keyTimes[keyNext].g, lerpAmount),
                lerp(keyTimes[keyPrevious].b, keyTimes[keyNext].b, lerpAmount)
        );
        setEffects(mixed);
    }
}
