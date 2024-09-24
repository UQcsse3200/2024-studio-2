package com.csse3200.game.minigames.maze.components;

import com.csse3200.game.ai.tasks.TaskRunner;
import com.csse3200.game.entities.Entity;

/**
 * A status effect that can be applied to an entity.
 */
public interface StatusEffect {
    /**
     * Create the status effect and attach it to the entity.
     *
     * @param entity Entity to attach to
     */
    void create(Entity entity);

    /** Start the effects of this status. Called whenever the status goes from inactive to active. */
    void start();

    /** Run one frame of the status. Similar to the update() in Components. */
    void update();

    /** Stop the effects of this status immediately. This can be called at any time. */
    void stop();
}
