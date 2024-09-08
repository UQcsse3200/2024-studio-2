package com.csse3200.game.components.combat.move;

import com.csse3200.game.components.Component;
import com.csse3200.game.components.combat.CombatManager;
import com.csse3200.game.entities.Entity;

import java.util.List;

public class CombatMoveComponent extends Component {
    private final List<CombatMove> moveSet;

    public CombatMoveComponent(List<CombatMove> moveSet) {
        this.moveSet = moveSet;
    }

    // Execute the move based on MoveAction enum
    public void executeMove(CombatManager.Action action, Entity target) {
        CombatMove move = getMoveAction(action);
        if (move != null) {
            move.execute(entity, target); // entity is the attacker
        }
    }

    private CombatMove getMoveAction(CombatManager.Action action) {
        return switch (action) {
            case ATTACK -> moveSet.get(0);
            case GUARD -> moveSet.get(1);
            case SLEEP -> moveSet.get(2);
            case SPECIAL -> moveSet.get(3);
            default -> null;
        };
    }

    public List<CombatMove> getMoveSet() {
        return moveSet;
    }
}
