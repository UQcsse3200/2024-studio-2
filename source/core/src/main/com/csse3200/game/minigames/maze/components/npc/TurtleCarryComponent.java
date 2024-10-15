package com.csse3200.game.minigames.maze.components.npc;

import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.minigames.maze.components.tasks.MazeMovementUtils;

/**
 * A component that let's turtles carry around an object on their backs.
 */
public class TurtleCarryComponent extends Component {
    private Entity carry;

    public TurtleCarryComponent(Entity carry) {
        super();
        this.carry = carry;
    }

    @Override
    public void update() {
        carry.setPosition(MazeMovementUtils.adjustPos(entity.getCenterPosition().add(0,entity.getScale().y*0.3f), carry));
    }
}
