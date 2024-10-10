package com.csse3200.game.components.combat.move;

import com.csse3200.game.components.CombatStatsComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The GuardMove class represents a move where the attacker guards, reducing damage from incoming attacks.
 * The move uses stamina and does not inflict damage but is used to mitigate damage from opponents.
 */
public class GuardMove extends CombatMove {
    private static final Logger logger = LoggerFactory.getLogger(GuardMove.class);

    /**
     * Constructor to initialize the GuardMove with its name and stamina cost.
     *
     * @param moveName   the name of the guard move.
     * @param staminaCost the stamina cost required to execute the guard move.
     */
    public GuardMove(String moveName, int staminaCost) {
        super(moveName, staminaCost);
    }

    /**
     * Executes the guard move, consuming stamina. This variant is used when there is no target, such as a defensive maneuver.
     *
     * @param attackerStats the combat stats of the entity performing the guard move.
     */
    @Override
    public void execute(CombatStatsComponent attackerStats) {
        if (attackerStats != null) {
            logger.info("{} guard using {} stamina.",
                    attackerStats.isPlayer() ? "PLAYER" : "ENEMY",
                    this.getStaminaCost());

            attackerStats.addStamina(-(this.getStaminaCost()));
        } else {
            logger.error("Entity does not have CombatStatsComponent.");
        }
    }

    /**
     * Executes the guard move, which simply consumes stamina. The target is ignored in this case.
     *
     * @param attackerStats the combat stats of the entity performing the move.
     * @param targetStats   the combat stats of the target (ignored for guard moves).
     */
    @Override
    public void execute(CombatStatsComponent attackerStats, CombatStatsComponent targetStats) {
        execute(attackerStats);
    }

    /**
     * Executes the guard move, consuming stamina. The target's guarding status is ignored in this context.
     *
     * @param attackerStats   the combat stats of the entity performing the move.
     * @param targetStats     the combat stats of the target (ignored for guard moves).
     * @param targetIsGuarded whether the target is guarding (ignored for guard moves).
     */
    @Override
    public void execute(CombatStatsComponent attackerStats, CombatStatsComponent targetStats, boolean targetIsGuarded) {
        execute(attackerStats);
    }

    /**
     * Executes the guard move, consuming stamina. The number of hits landed is ignored for guard moves.
     *
     * @param attackerStats   the combat stats of the entity performing the move.
     * @param targetStats     the combat stats of the target (ignored for guard moves).
     * @param targetIsGuarded whether the target is guarding (ignored for guard moves).
     * @param numHitsLanded   the number of hits landed (ignored for guard moves).
     */
    @Override
    public void execute(CombatStatsComponent attackerStats, CombatStatsComponent targetStats, boolean targetIsGuarded,
                        int numHitsLanded) {
        execute(attackerStats);
    }
}
