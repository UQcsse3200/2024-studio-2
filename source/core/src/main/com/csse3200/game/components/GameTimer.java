package com.csse3200.game.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.GdxGame;

/**
 * Manages a countdown timer for the game, allowing for timed events such as
 * countdowns before transitioning to a combat screen.
 */
public class GameTimer {
    private final Timer timer;
    private final GdxGame game;

    /**
     * Constructs a new GameTimer with a reference to the main game instance.
     *
     * @param game The main game instance.
     */
    public GameTimer(GdxGame game) {
        this.game = game;
        this.timer = new Timer();
    }

    /**
     * Callback interface for handling countdown events.
     * onTick is called every second during the countdown,
     * and onFinish is called when the countdown reaches zero.
     */
    public interface CountdownCallback {
        void onTick(int secondsLeft);
        void onFinish();
    }

    /**
     * Starts a countdown for a specified number of seconds.
     * Triggers callbacks for each second and when the countdown is finished.
     *
     * @param seconds  The number of seconds for the countdown.
     * @param callback The callback to handle tick and finish events.
     */
    public void startCountdown(int seconds, CountdownCallback callback) {
        for (int i = seconds; i >= 0; i--) {
            final int secondsLeft = i;
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    Gdx.app.postRunnable(() -> {
                        if (secondsLeft > 0) {
                            callback.onTick(secondsLeft);
                        } else {
                            callback.onFinish();
                        }
                    });
                }
            }, seconds - i);
        }
    }

    /**
     * Cancels the ongoing timer, clearing all scheduled tasks.
     */
    public void cancel() {
        timer.clear();
    }
}