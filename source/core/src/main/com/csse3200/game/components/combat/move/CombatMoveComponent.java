package com.csse3200.game.components.combat.move;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.combat.CombatManager;

import java.util.List;

/**
 * The CombatMoveComponent class manages a set of combat moves for an entity.
 * It allows for the execution of specific combat moves based on the action type and target.
 */
public class CombatMoveComponent extends Component {
    private final List<CombatMove> moveSet;  // The list of available combat moves for the entity.
    private String name; // The name of the entity.

    /**
     * Constructor to initialize the combat move component with a set of moves.
     *
     * @param moveSet the list of combat moves available to this entity.
     */
    public CombatMoveComponent(List<CombatMove> moveSet) {
        this.moveSet = moveSet;
        // Issues with CombatMoveComponent not being associated with an entity on initialisation.
        // This seemed to fix it.
        this.name = "Uninitialised";
    }

    private String[] getMoveContextStrings(CombatMove move, String statChanges) {
        // Get the name of the entity.
        if (name == "Uninitialised") {
            if (entity.isPlayer()) {
                name = "You";
            } else {
                name = entity.getEnemyType().name();
            }
        }
        String moveDescription = String.format("%s decided to %s.", name, move.getMoveName());

        // Execute the move and get the stat changes.
        String moveOutcome;
        if (move != null) {
            moveOutcome = String.format("%s %s", name, statChanges);
        } else {
            moveOutcome = "No move found.";
        }

        return new String[]{moveDescription, moveOutcome};
    }

    /**
     * Executes a move based on the provided action. Uses the entity's own stats as the attacker.
     *
     * @param action the action that specifies which move to execute.
     */
    public String[] executeMove(CombatManager.Action action) {
        CombatMove move = getMoveAction(action);
        String statChanges;
        if (move != null) {
            statChanges = move.execute(entity.getComponent(CombatStatsComponent.class));
        } else {
            statChanges = "No move found.";
        }
        return getMoveContextStrings(move, statChanges);
    }

    /**
     * Executes a move based on the provided action and target. Uses the entity's own stats as the attacker.
     *
     * @param action the action that specifies which move to execute.
     * @param target the target's combat stats component.
     */
    public String[] executeMove(CombatManager.Action action, CombatStatsComponent target) {
        CombatMove move = getMoveAction(action);
        String statChanges;
        if (move != null) {
            statChanges = move.execute(entity.getComponent(CombatStatsComponent.class), target); // entity is the attacker
        } else {
            statChanges = "No move found.";
        }
        return getMoveContextStrings(move, statChanges);
    }

    /**
     * Executes a move based on the provided action, target, and whether the target is guarding.
     * Uses the entity's own stats as the attacker.
     *
     * @param action         the action that specifies which move to execute.
     * @param target         the target's combat stats component.
     * @param targetIsGuarded whether the target is guarding, reducing the effectiveness of the attack.
     */
    public String[] executeMove(CombatManager.Action action, CombatStatsComponent target, boolean targetIsGuarded) {
        CombatMove move = getMoveAction(action);
        String statChanges;
        if (move != null) {
            statChanges = move.execute(entity.getComponent(CombatStatsComponent.class), target, targetIsGuarded); // entity is the attacker
        } else {
            statChanges = "No move found.";
        }
        return getMoveContextStrings(move, statChanges);
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
    public String[] executeMove(CombatManager.Action action, CombatStatsComponent target, boolean targetIsGuarded,
                            int numHitsLanded) {
        CombatMove move = getMoveAction(action);
        String statChanges;
        if (move != null) {
            statChanges = move.execute(entity.getComponent(CombatStatsComponent.class), target, targetIsGuarded, numHitsLanded); // entity is the attacker
        } else {
            statChanges = "No move found.";
        }
        return getMoveContextStrings(move, statChanges);
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
                case ITEM:
                    if (move instanceof ItemMove) {
                        return move;
                    }
                default:
                    return null;
            }
        }
        return null;
    }

    /**
     * Checks if the entity has a special move in its move set.
     *
     * @return true if the entity has a special move, false otherwise.
     */
    public boolean hasSpecialMove() {
        for (CombatMove move : moveSet) {
            if (move instanceof SpecialMove) {
                return true;
            }
        }
        return false;
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
