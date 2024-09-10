package com.csse3200.game.components.combat.move;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.combat.CombatManager;
import com.csse3200.game.entities.Entity;

import java.util.List;

/**
 * The CombatMoveComponent class manages a set of combat moves for an entity.
 * It allows for the execution of specific combat moves based on the action type and target.
 */
public class CombatMoveComponent extends Component {
    private final List<CombatMove> moveSet;  // The list of available combat moves for the entity.

    /**
     * Constructor to initialize the combat move component with a set of moves.
     *
     * @param moveSet the list of combat moves available to this entity.
     */
    public CombatMoveComponent(List<CombatMove> moveSet) {
        this.moveSet = moveSet;
    }

    /**
     * Executes a move based on the provided action. Uses the entity's own stats as the attacker.
     *
     * @param action the action that specifies which move to execute.
     */
    public void executeMove(CombatManager.Action action) {
        CombatMove move = getMoveAction(action);
        if (move != null) {
            move.execute(entity.getComponent(CombatStatsComponent.class));
        }
    }

    /**
     * Executes a move based on the provided action and target. Uses the entity's own stats as the attacker.
     *
     * @param action the action that specifies which move to execute.
     * @param target the target's combat stats component.
     */
    public void executeMove(CombatManager.Action action, CombatStatsComponent target) {
        CombatMove move = getMoveAction(action);
        if (move != null) {
            move.execute(entity.getComponent(CombatStatsComponent.class), target); // entity is the attacker
        }
    }

    /**
     * Executes a move based on the provided action, target, and whether the target is guarding.
     * Uses the entity's own stats as the attacker.
     *
     * @param action         the action that specifies which move to execute.
     * @param target         the target's combat stats component.
     * @param targetIsGuarded whether the target is guarding, reducing the effectiveness of the attack.
     */
    public void executeMove(CombatManager.Action action, CombatStatsComponent target, boolean targetIsGuarded) {
        CombatMove move = getMoveAction(action);
        if (move != null) {
            move.execute(entity.getComponent(CombatStatsComponent.class), target, targetIsGuarded); // entity is the attacker
        }
    }

    /**
     * Executes a move based on the provided action, target, guarding status, and number of hits landed.
     * Uses the entity's own stats as the attacker.
     *
     * @param action         the action that specifies which move to execute.
     * @param target         the target's combat stats component.
     * @param targetIsGuarded whether the target is guarding.
     * @param numHitsLanded  the number of hits landed in a multi-hit move.
     */
    public void executeMove(CombatManager.Action action, CombatStatsComponent target, boolean targetIsGuarded,
                            int numHitsLanded) {
        CombatMove move = getMoveAction(action);
        if (move != null) {
            move.execute(entity.getComponent(CombatStatsComponent.class), target, targetIsGuarded, numHitsLanded); // entity is the attacker
        }
    }

    /**
     * Retrieves the appropriate combat move based on the action type.
     *
     * @param action the action that specifies which move to retrieve.
     * @return the combat move corresponding to the action, or null if none is found.
     */
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

    /**
     * Gets the list of combat moves associated with this component.
     *
     * @return the list of combat moves.
     */
    public List<CombatMove> getMoveSet() {
        return moveSet;
    }
}
