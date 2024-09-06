package com.csse3200.game.components.combat.move;

import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;

import java.util.List;

public class CombatMoveComponent extends Component {
    private List<CombatMove> moveSet;

    public CombatMoveComponent(List<CombatMove> moveSet) {
        this.moveSet = moveSet;
    }

    public void executeMove(int moveIndex, Entity target) {
        if (moveIndex >= 0 && moveIndex < moveSet.size()) {
            CombatMove move = moveSet.get(moveIndex);
            move.execute(entity, target);
        }
    }

    public List<CombatMove> getMoveSet() {
        return moveSet;
    }
}
