package com.csse3200.game.components.combat.move;

import com.csse3200.game.components.CombatStatsComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The GuardMove class represents a move where the attacker guards, reducing damage from incoming attacks.
 * The move uses hunger and does not inflict damage but is used to mitigate damage from opponents.
 */
public class GuardMove extends CombatMove {
    private static final Logger logger = LoggerFactory.getLogger(GuardMove.class);

    /**
     * Constructor to initialize the GuardMove with its name and hunger cost.
     *
     * @param moveName   the name of the guard move.
     * @param hungerCost the hunger cost required to execute the guard move.
     */
    public GuardMove(String moveName, int hungerCost) {
        super(moveName, hungerCost);
    }

    /**
     * Executes the guard move, consuming hunger. This variant is used when there is no target, such as a defensive maneuver.
     *
     * @param attackerStats the combat stats of the entity performing the guard move.
     */
    @Override
    public StatsChange[] execute(CombatStatsComponent attackerStats) {
        StatsChange[] statsChanges = new StatsChange[1];
        if (attackerStats != null) {
            logger.info("{} guard using {} hunger.",
                    attackerStats.isPlayer() ? "PLAYER" : "ENEMY",
                    this.getHungerCost());
            int initialHunger = attackerStats.getHunger();
            attackerStats.addHunger(-(this.getHungerCost()));
            statsChanges[0] = new StatsChange(0, attackerStats.getHunger() - initialHunger);
        } else {
            logger.error("Entity does not have CombatStatsComponent.");
        }
        return statsChanges;
    }

    /**
     * Executes the guard move, which simply consumes hunger. The target is ignored in this case.
     *
     * @param attackerStats the combat stats of the entity performing the move.
     * @param targetStats   the combat stats of the target (ignored for guard moves).
     */
    @Override
    public StatsChange[] execute(CombatStatsComponent attackerStats, CombatStatsComponent targetStats) {
        return execute(attackerStats);
    }

    /**
     * Executes the guard move, consuming hunger. The target's guarding status is ignored in this context.
     *
     * @param attackerStats   the combat stats of the entity performing the move.
     * @param targetStats     the combat stats of the target (ignored for guard moves).
     * @param targetIsGuarded whether the target is guarding (ignored for guard moves).
     */
    @Override
    public StatsChange[] execute(CombatStatsComponent attackerStats, CombatStatsComponent targetStats, boolean targetIsGuarded) {
        return execute(attackerStats);
    }

    /**
     * Executes the guard move, consuming hunger. The number of hits landed is ignored for guard moves.
     *
     * @param attackerStats   the combat stats of the entity performing the move.
     * @param targetStats     the combat stats of the target (ignored for guard moves).
     * @param targetIsGuarded whether the target is guarding (ignored for guard moves).
     * @param numHitsLanded   the number of hits landed (ignored for guard moves).
     */
    @Override
    public StatsChange[] execute(CombatStatsComponent attackerStats, CombatStatsComponent targetStats, boolean targetIsGuarded,
                        int numHitsLanded) {
        return execute(attackerStats);
    }
}
