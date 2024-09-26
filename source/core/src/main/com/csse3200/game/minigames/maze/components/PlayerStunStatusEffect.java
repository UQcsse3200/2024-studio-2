package com.csse3200.game.minigames.maze.components;

import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.minigames.maze.components.player.MazePlayerActions;
import com.csse3200.game.minigames.maze.components.tasks.IdleTask;

/**
 * Class for the NPC stun affect
 */
public class PlayerStunStatusEffect implements StatusEffect {
    Entity player;

    /**
     * Create the status effect and attach it to the entity.
     *
     * @param entity Entity to attach to
     */
    public void create(Entity entity) {
        player = entity;
    }

    /**
     * Start the effects of this status. Called whenever the status goes from inactive to active.
     */
    @Override
    public void start() {
        player.getComponent(MazePlayerActions.class).setEnabled(false);
    }

    /** Stop the effects of this status immediately. This can be called at any time. */
    @Override
    public void stop() {
        player.getComponent(MazePlayerActions.class).setEnabled(true);
    }

    /**
     * unused at the moment
     */
    @Override
    public void update() {}
}
