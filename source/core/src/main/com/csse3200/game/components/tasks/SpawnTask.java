package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.ai.tasks.Task;

public class SpawnTask extends DefaultTask implements PriorityTask {
    private final Vector2 spawnPosition;
    private final float spawnDuration;
    private float elapsedTime;

    public SpawnTask(Vector2 spawnPosition, float spawnDuration) {
        this.spawnPosition = spawnPosition;
        this.spawnDuration = spawnDuration;
    }

    @Override
    public int getPriority() {
        return 0; // Highest priority for spawn task
    }

    @Override
    public void start() {
        super.start();
        elapsedTime = 0;
        // Position the NPC at the spawn location
        owner.getEntity().setPosition(spawnPosition);
        // Trigger the spawn animation or other spawn logic
    }

    @Override
    public void update() {
//        elapsedTime += getDeltaTime();
//        if (elapsedTime >= spawnDuration) {
//            completeTask();
//        }
    }

    private void completeTask() {
        this.stop();
        owner.getEntity().getEvents().trigger("spawnComplete");
    }

    @Override
    public void stop() {
        super.stop();
        // Clean up if necessary
    }
}
