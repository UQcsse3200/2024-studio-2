package com.csse3200.game.components.combat.move;

import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;

import java.util.List;

public class CombatMoveComponent extends Component {
    private List<CombatMove> moveSet;
    public enum MoveAction { ATTACK, GUARD, COUNTER, SPECIAL }

    public CombatMoveComponent(List<CombatMove> moveSet) {
        this.moveSet = moveSet;
    }

    // Execute the move based on MoveAction enum
    public void executeMove(MoveAction action, Entity target) {
        CombatMove move = getMoveAction(action);
        if (move != null) {
            move.execute(entity, target); // entity is the attacker
        }
    }

    private CombatMove getMoveAction(MoveAction action) {
        switch (action) {
            case ATTACK:
                return moveSet.get(0);
            case GUARD:
                return moveSet.get(1);
            case COUNTER:
                return moveSet.get(2);
            case SPECIAL:
                return moveSet.get(3);
            default:
                return null;
        }
    }

    public List<CombatMove> getMoveSet() {
        return moveSet;
    }
}
