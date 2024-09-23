package com.csse3200.game.minigames.maze.entities.mazenpc;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.minigames.maze.components.MazeTouchAttackComponent;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.FaceMoveDirectionXComponent;

public abstract class MazeEntity extends Entity {
    public MazeEntity() {
        // Common components for all maze entities
        this.addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent())
                .addComponent(new ColliderComponent().setGroupIndex((short) -1)) // NPCs don’t collide with each other
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                .addComponent(new MazeTouchAttackComponent(PhysicsLayer.PLAYER, .8f))
                .addComponent(new FaceMoveDirectionXComponent());

        PhysicsUtils.setScaledCollider(this, 0.9f, 0.4f);
        this.getComponent(ColliderComponent.class).setDensity(1.5f);
    }
}
