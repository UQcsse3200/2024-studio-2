package com.csse3200.game.services;

/**
 * Manages the in-game time, which can be paused and resumed independently
 * of the global game time.
 */
public class InGameTime {
    private long startTime; // Time when the in-game clock started
    private long pausedTime; // Total time accumulated while paused
    private long pauseStart; // Timestamp when the pause started
    private boolean isPaused;

    public InGameTime() {
        this.startTime = System.currentTimeMillis();
        this.pausedTime = 0;
        this.isPaused = false;
    }

    /**
     * Pauses the in-game time.
     */
    public void pause() {
        if (!isPaused) {
            isPaused = true;
            pauseStart = System.currentTimeMillis();
        }
    }

    /**
     * Resumes the in-game time.
     */
    public void resume() {
        if (isPaused) {
            isPaused = false;
            pausedTime += System.currentTimeMillis() - pauseStart;
        }
    }

    /**
     * Returns the current in-game time in milliseconds.
     *
     * @return the in-game time in milliseconds.
     */
    public long getTime() {
        if (isPaused) {
            return pauseStart - startTime - pausedTime;
        } else {
            return System.currentTimeMillis() - startTime - pausedTime;
        }
    }

    /**
     * Returns the delta time between frames in milliseconds.
     *
     * @return the delta time in milliseconds.
     */
    public long getDeltaTime() {
        // Since we're using system time, delta time is not directly applicable.
        // We'll handle delta time within the DayNightCycle using the in-game time.
        return 0; // Placeholder
    }
}
