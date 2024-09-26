package com.csse3200.game.minigames.maze.components;

import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.minigames.maze.components.tasks.IdleTask;

/**
 * Class for the NPC stun affect
 */
public class NPCStunStatusEffect implements StatusEffect {
    private IdleTask idleTask;

    /**
     * Create the status effect and attach it to the entity.
     *
     * @param entity Entity to attach to
     */
    public void create(Entity entity) {
        idleTask = new IdleTask(99);
        idleTask.setInactive();
        entity.getComponent(AITaskComponent.class).addTask(idleTask);
    }

    /**
     * Start the effects of this status. Called whenever the status goes from inactive to active.
     */
    @Override
    public void start() {
        idleTask.setActive();
    }

    /** Stop the effects of this status immediately. This can be called at any time. */
    @Override
    public void stop() {
        idleTask.setInactive();
    }

    /**
     * unused at the moment
     */
    @Override
    public void update() {}
}
