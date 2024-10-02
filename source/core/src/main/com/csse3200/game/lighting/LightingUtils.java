package com.csse3200.game.lighting;

import com.badlogic.gdx.graphics.Color;

/**
 * Some helpful utils to work with colors and lighting.
 */
public class LightingUtils {
    /**
     * Interpolate a colour from a wheel of colours. The colours are equally spaced around the
     * wheel starting at x = 0.0 and wrapping back around at x = 1.0
     *
     * @param colors Array of colors on the wheel.
     * @param x Position on color wheel.
     * @return Interpolated color.
     */
    public static Color interpolateColorCycle(Color[] colors, float x) {
        int keyPrevious = (int) Math.floor(x * colors.length);
        int keyNext = (keyPrevious + 1) % colors.length;
        float lerpAmount = x * colors.length - keyPrevious;
        return colors[keyPrevious].cpy().lerp(colors[keyNext], lerpAmount);
    }
}
