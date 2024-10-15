package com.csse3200.game.minigames.maze.entities.mazenpc;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.minigames.maze.components.MazeTouchAttackComponent;
import com.csse3200.game.minigames.maze.components.NPCStunStatusEffect;
import com.csse3200.game.minigames.maze.components.StatusEffectComponent;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.FaceMoveDirectionXComponent;

/**
 * MazeEntity is an abstract base class for all non-playable character (NPC) entities
 * in the maze minigame. It provides common components and functionality for maze NPCs.
 */
public abstract class MazeEntity extends Entity {

    /**
     * Constructs a MazeEntity with common components for physics, movement, collision,
     * and attack interactions. This constructor sets up the basic entity configuration.
     */
    protected MazeEntity() {
        // Common components for all maze entities
        this.addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent())
                .addComponent(new ColliderComponent().setGroupIndex((short) -1)) // NPCs don’t collide with each other
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                .addComponent(new MazeTouchAttackComponent(PhysicsLayer.PLAYER, 15f))
                .addComponent(new FaceMoveDirectionXComponent())
                .addComponent(new StatusEffectComponent().registerStatusEffect("stun", new NPCStunStatusEffect()));

        this.getComponent(ColliderComponent.class).setDensity(1.5f);
    }
}
