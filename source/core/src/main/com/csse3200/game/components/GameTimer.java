package com.csse3200.game.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.GdxGame;

public class GameTimer {
    private final Timer timer;
    private final GdxGame game;

    public GameTimer(GdxGame game) {
        this.game = game;
        this.timer = new Timer();
    }

    public interface CountdownCallback {
        void onTick(int secondsLeft);
        void onFinish();
    }

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

    public void cancel() {
        timer.clear();
    }
}