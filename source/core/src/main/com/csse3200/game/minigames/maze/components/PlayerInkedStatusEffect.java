package com.csse3200.game.minigames.maze.components;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.minigames.maze.components.tasks.MazeMovementUtils;
import com.csse3200.game.minigames.maze.rendering.AlphaTextureRenderComponent;
import com.csse3200.game.services.ServiceLocator;

/**
 * Class for the Octopus Ink affect on player.
 * When active, will show a black ink spill that covers the screen
 */
public class PlayerInkedStatusEffect implements StatusEffect {
    private static final float INK_TIME = 4f;
    private float timeLeft;
    Entity player;
    Entity ink;
    AlphaTextureRenderComponent alphaComponent;

    /**
     * Create the status effect and attach it to the entity.
     *
     * @param entity Entity to attach to
     */
    public void create(Entity entity) {
        player = entity;
        alphaComponent = new AlphaTextureRenderComponent("images/minigames/inksplat.png");
        ink =  new Entity().addComponent(alphaComponent);
        ink.setScale(0, 0);
        ServiceLocator.getEntityService().register(ink);
        ink.setEnabled(false);
    }

    /**
     * Start the effects of this status. Called whenever the status goes from inactive to active.
     */
    @Override
    public void start() {
        alphaComponent.setAlpha(1);
        ink.setScale(4, 4);
        timeLeft = INK_TIME;
    }

    /** Stop the effects of this status immediately. This can be called at any time. */
    @Override
    public void stop() {
        alphaComponent.setAlpha(0);
        ink.setScale(0, 0);
        timeLeft = 0;
    }

    /**
     * Updates the ink, setting its position to track the player and fading out the ink gradually.
     */
    @Override
    public void update() {
        timeLeft -= ServiceLocator.getTimeSource().getDeltaTime();
        float interpX = timeLeft / INK_TIME;
        if (interpX < .5f) {
            alphaComponent.setAlpha(Math.max(alphaComponent.getAlpha() * 4*interpX*interpX, 0));
        }
        ink.setPosition(MazeMovementUtils.adjustPos(player.getCenterPosition(), ink));
    }
}
