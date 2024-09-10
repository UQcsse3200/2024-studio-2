package com.csse3200.game.components.combat.move;

import com.csse3200.game.components.CombatStatsComponent;
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
    public void executeMove(CombatManager.Action action) {
        CombatMove move = getMoveAction(action);
        if (move != null) {
            move.execute(entity.getComponent(CombatStatsComponent.class));
        }
    }

    // Execute the move based on MoveAction enum
    public void executeMove(CombatManager.Action action, CombatStatsComponent target) {
        CombatMove move = getMoveAction(action);
        if (move != null) {
            move.execute(entity.getComponent(CombatStatsComponent.class), target); // entity is the attacker
        }
    }

    // Execute the move based on MoveAction enum
    public void executeMove(CombatManager.Action action, CombatStatsComponent target, boolean targetIsGuarded) {
        CombatMove move = getMoveAction(action);
        if (move != null) {
            move.execute(entity.getComponent(CombatStatsComponent.class), target, targetIsGuarded); // entity is the attacker
        }
    }

    // Execute the move based on MoveAction enum
    public void executeMove(CombatManager.Action action, CombatStatsComponent target, boolean targetIsGuarded,
                            int numHitsLanded) {
        CombatMove move = getMoveAction(action);
        if (move != null) {
            move.execute(entity.getComponent(CombatStatsComponent.class), target, targetIsGuarded, numHitsLanded); // entity is the attacker
        }
    }

    private CombatMove getMoveAction(CombatManager.Action action) {
        for (CombatMove move : moveSet) {
            switch (action) {
                case ATTACK:
                    if (move instanceof AttackMove) {
                        return move;
                    }
                    break;
                case GUARD:
                    if (move instanceof GuardMove) {
                        return move;
                    }
                    break;
                case SLEEP:
                    if (move instanceof SleepMove) {
                        return move;
                    }
                    break;
                case SPECIAL:
                    if (move instanceof SpecialMove) {
                        return move;
                    }
                    break;
                default:
                    return null;
            }
        }
        return null;
    }

    public List<CombatMove> getMoveSet() {
        return moveSet;
    }
}
