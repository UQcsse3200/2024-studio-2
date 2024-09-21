package com.csse3200.game.minigames.maze.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;

public class MovementRelativeToCenterPos {
    public static Vector2 adjustPos(Vector2 pos, Entity entity) {
        return pos.cpy().sub(entity.getCenterPosition().sub(entity.getPosition()));
    }
}
