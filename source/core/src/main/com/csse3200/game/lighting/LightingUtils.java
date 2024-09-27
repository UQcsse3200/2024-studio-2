package com.csse3200.game.lighting;

import com.badlogic.gdx.graphics.Color;

public class LightingUtils {
    public static Color interpolateColorCycle(Color[] colors, float x) {
        int keyPrevious = (int) Math.floor(x * colors.length);
        int keyNext = (keyPrevious + 1) % colors.length;
        float lerpAmount = x * colors.length - keyPrevious;
        return colors[keyPrevious].lerp(colors[keyNext], lerpAmount);
    }
}
